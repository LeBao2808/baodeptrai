import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IMaterialExport } from 'app/entities/material-export/material-export.model';
import { MaterialExportService } from 'app/entities/material-export/service/material-export.service';
import { IMaterial } from 'app/entities/material/material.model';
import { MaterialService } from 'app/entities/material/service/material.service';
import { IMaterialExportDetail } from '../material-export-detail.model';
import { MaterialExportDetailService } from '../service/material-export-detail.service';
import { MaterialExportDetailFormService } from './material-export-detail-form.service';

import { MaterialExportDetailUpdateComponent } from './material-export-detail-update.component';

describe('MaterialExportDetail Management Update Component', () => {
  let comp: MaterialExportDetailUpdateComponent;
  let fixture: ComponentFixture<MaterialExportDetailUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let materialExportDetailFormService: MaterialExportDetailFormService;
  let materialExportDetailService: MaterialExportDetailService;
  let materialExportService: MaterialExportService;
  let materialService: MaterialService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MaterialExportDetailUpdateComponent],
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
      .overrideTemplate(MaterialExportDetailUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MaterialExportDetailUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    materialExportDetailFormService = TestBed.inject(MaterialExportDetailFormService);
    materialExportDetailService = TestBed.inject(MaterialExportDetailService);
    materialExportService = TestBed.inject(MaterialExportService);
    materialService = TestBed.inject(MaterialService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call MaterialExport query and add missing value', () => {
      const materialExportDetail: IMaterialExportDetail = { id: 456 };
      const materialExport: IMaterialExport = { id: 5076 };
      materialExportDetail.materialExport = materialExport;

      const materialExportCollection: IMaterialExport[] = [{ id: 28786 }];
      jest.spyOn(materialExportService, 'query').mockReturnValue(of(new HttpResponse({ body: materialExportCollection })));
      const additionalMaterialExports = [materialExport];
      const expectedCollection: IMaterialExport[] = [...additionalMaterialExports, ...materialExportCollection];
      jest.spyOn(materialExportService, 'addMaterialExportToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ materialExportDetail });
      comp.ngOnInit();

      expect(materialExportService.query).toHaveBeenCalled();
      expect(materialExportService.addMaterialExportToCollectionIfMissing).toHaveBeenCalledWith(
        materialExportCollection,
        ...additionalMaterialExports.map(expect.objectContaining),
      );
      expect(comp.materialExportsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Material query and add missing value', () => {
      const materialExportDetail: IMaterialExportDetail = { id: 456 };
      const material: IMaterial = { id: 19265 };
      materialExportDetail.material = material;

      const materialCollection: IMaterial[] = [{ id: 14747 }];
      jest.spyOn(materialService, 'query').mockReturnValue(of(new HttpResponse({ body: materialCollection })));
      const additionalMaterials = [material];
      const expectedCollection: IMaterial[] = [...additionalMaterials, ...materialCollection];
      jest.spyOn(materialService, 'addMaterialToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ materialExportDetail });
      comp.ngOnInit();

      expect(materialService.query).toHaveBeenCalled();
      expect(materialService.addMaterialToCollectionIfMissing).toHaveBeenCalledWith(
        materialCollection,
        ...additionalMaterials.map(expect.objectContaining),
      );
      expect(comp.materialsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const materialExportDetail: IMaterialExportDetail = { id: 456 };
      const materialExport: IMaterialExport = { id: 8787 };
      materialExportDetail.materialExport = materialExport;
      const material: IMaterial = { id: 12594 };
      materialExportDetail.material = material;

      activatedRoute.data = of({ materialExportDetail });
      comp.ngOnInit();

      expect(comp.materialExportsSharedCollection).toContain(materialExport);
      expect(comp.materialsSharedCollection).toContain(material);
      expect(comp.materialExportDetail).toEqual(materialExportDetail);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaterialExportDetail>>();
      const materialExportDetail = { id: 123 };
      jest.spyOn(materialExportDetailFormService, 'getMaterialExportDetail').mockReturnValue(materialExportDetail);
      jest.spyOn(materialExportDetailService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ materialExportDetail });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: materialExportDetail }));
      saveSubject.complete();

      // THEN
      expect(materialExportDetailFormService.getMaterialExportDetail).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(materialExportDetailService.update).toHaveBeenCalledWith(expect.objectContaining(materialExportDetail));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaterialExportDetail>>();
      const materialExportDetail = { id: 123 };
      jest.spyOn(materialExportDetailFormService, 'getMaterialExportDetail').mockReturnValue({ id: null });
      jest.spyOn(materialExportDetailService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ materialExportDetail: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: materialExportDetail }));
      saveSubject.complete();

      // THEN
      expect(materialExportDetailFormService.getMaterialExportDetail).toHaveBeenCalled();
      expect(materialExportDetailService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaterialExportDetail>>();
      const materialExportDetail = { id: 123 };
      jest.spyOn(materialExportDetailService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ materialExportDetail });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(materialExportDetailService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMaterialExport', () => {
      it('Should forward to materialExportService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(materialExportService, 'compareMaterialExport');
        comp.compareMaterialExport(entity, entity2);
        expect(materialExportService.compareMaterialExport).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareMaterial', () => {
      it('Should forward to materialService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(materialService, 'compareMaterial');
        comp.compareMaterial(entity, entity2);
        expect(materialService.compareMaterial).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
