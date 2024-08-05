import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IMaterial } from 'app/entities/material/material.model';
import { MaterialService } from 'app/entities/material/service/material.service';
import { IMaterialReceipt } from 'app/entities/material-receipt/material-receipt.model';
import { MaterialReceiptService } from 'app/entities/material-receipt/service/material-receipt.service';
import { IMaterialReceiptDetail } from '../material-receipt-detail.model';
import { MaterialReceiptDetailService } from '../service/material-receipt-detail.service';
import { MaterialReceiptDetailFormService } from './material-receipt-detail-form.service';

import { MaterialReceiptDetailUpdateComponent } from './material-receipt-detail-update.component';

describe('MaterialReceiptDetail Management Update Component', () => {
  let comp: MaterialReceiptDetailUpdateComponent;
  let fixture: ComponentFixture<MaterialReceiptDetailUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let materialReceiptDetailFormService: MaterialReceiptDetailFormService;
  let materialReceiptDetailService: MaterialReceiptDetailService;
  let materialService: MaterialService;
  let materialReceiptService: MaterialReceiptService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MaterialReceiptDetailUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(MaterialReceiptDetailUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MaterialReceiptDetailUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    materialReceiptDetailFormService = TestBed.inject(MaterialReceiptDetailFormService);
    materialReceiptDetailService = TestBed.inject(MaterialReceiptDetailService);
    materialService = TestBed.inject(MaterialService);
    materialReceiptService = TestBed.inject(MaterialReceiptService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Material query and add missing value', () => {
      const materialReceiptDetail: IMaterialReceiptDetail = { id: 456 };
      const material: IMaterial = { id: 22077 };
      materialReceiptDetail.material = material;

      const materialCollection: IMaterial[] = [{ id: 32406 }];
      jest.spyOn(materialService, 'query').mockReturnValue(of(new HttpResponse({ body: materialCollection })));
      const additionalMaterials = [material];
      const expectedCollection: IMaterial[] = [...additionalMaterials, ...materialCollection];
      jest.spyOn(materialService, 'addMaterialToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ materialReceiptDetail });
      comp.ngOnInit();

      expect(materialService.query).toHaveBeenCalled();
      expect(materialService.addMaterialToCollectionIfMissing).toHaveBeenCalledWith(
        materialCollection,
        ...additionalMaterials.map(expect.objectContaining),
      );
      expect(comp.materialsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call MaterialReceipt query and add missing value', () => {
      const materialReceiptDetail: IMaterialReceiptDetail = { id: 456 };
      const receipt: IMaterialReceipt = { id: 26802 };
      materialReceiptDetail.receipt = receipt;

      const materialReceiptCollection: IMaterialReceipt[] = [{ id: 27682 }];
      jest.spyOn(materialReceiptService, 'query').mockReturnValue(of(new HttpResponse({ body: materialReceiptCollection })));
      const additionalMaterialReceipts = [receipt];
      const expectedCollection: IMaterialReceipt[] = [...additionalMaterialReceipts, ...materialReceiptCollection];
      jest.spyOn(materialReceiptService, 'addMaterialReceiptToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ materialReceiptDetail });
      comp.ngOnInit();

      expect(materialReceiptService.query).toHaveBeenCalled();
      expect(materialReceiptService.addMaterialReceiptToCollectionIfMissing).toHaveBeenCalledWith(
        materialReceiptCollection,
        ...additionalMaterialReceipts.map(expect.objectContaining),
      );
      expect(comp.materialReceiptsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const materialReceiptDetail: IMaterialReceiptDetail = { id: 456 };
      const material: IMaterial = { id: 31381 };
      materialReceiptDetail.material = material;
      const receipt: IMaterialReceipt = { id: 6502 };
      materialReceiptDetail.receipt = receipt;

      activatedRoute.data = of({ materialReceiptDetail });
      comp.ngOnInit();

      expect(comp.materialsSharedCollection).toContain(material);
      expect(comp.materialReceiptsSharedCollection).toContain(receipt);
      expect(comp.materialReceiptDetail).toEqual(materialReceiptDetail);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaterialReceiptDetail>>();
      const materialReceiptDetail = { id: 123 };
      jest.spyOn(materialReceiptDetailFormService, 'getMaterialReceiptDetail').mockReturnValue(materialReceiptDetail);
      jest.spyOn(materialReceiptDetailService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ materialReceiptDetail });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: materialReceiptDetail }));
      saveSubject.complete();

      // THEN
      expect(materialReceiptDetailFormService.getMaterialReceiptDetail).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(materialReceiptDetailService.update).toHaveBeenCalledWith(expect.objectContaining(materialReceiptDetail));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaterialReceiptDetail>>();
      const materialReceiptDetail = { id: 123 };
      jest.spyOn(materialReceiptDetailFormService, 'getMaterialReceiptDetail').mockReturnValue({ id: null });
      jest.spyOn(materialReceiptDetailService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ materialReceiptDetail: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: materialReceiptDetail }));
      saveSubject.complete();

      // THEN
      expect(materialReceiptDetailFormService.getMaterialReceiptDetail).toHaveBeenCalled();
      expect(materialReceiptDetailService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaterialReceiptDetail>>();
      const materialReceiptDetail = { id: 123 };
      jest.spyOn(materialReceiptDetailService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ materialReceiptDetail });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(materialReceiptDetailService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMaterial', () => {
      it('Should forward to materialService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(materialService, 'compareMaterial');
        comp.compareMaterial(entity, entity2);
        expect(materialService.compareMaterial).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareMaterialReceipt', () => {
      it('Should forward to materialReceiptService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(materialReceiptService, 'compareMaterialReceipt');
        comp.compareMaterialReceipt(entity, entity2);
        expect(materialReceiptService.compareMaterialReceipt).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
