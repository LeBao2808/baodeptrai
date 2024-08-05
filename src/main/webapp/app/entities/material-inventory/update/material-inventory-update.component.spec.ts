import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IMaterial } from 'app/entities/material/material.model';
import { MaterialService } from 'app/entities/material/service/material.service';
import { MaterialInventoryService } from '../service/material-inventory.service';
import { IMaterialInventory } from '../material-inventory.model';
import { MaterialInventoryFormService } from './material-inventory-form.service';

import { MaterialInventoryUpdateComponent } from './material-inventory-update.component';

describe('MaterialInventory Management Update Component', () => {
  let comp: MaterialInventoryUpdateComponent;
  let fixture: ComponentFixture<MaterialInventoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let materialInventoryFormService: MaterialInventoryFormService;
  let materialInventoryService: MaterialInventoryService;
  let materialService: MaterialService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MaterialInventoryUpdateComponent],
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
      .overrideTemplate(MaterialInventoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MaterialInventoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    materialInventoryFormService = TestBed.inject(MaterialInventoryFormService);
    materialInventoryService = TestBed.inject(MaterialInventoryService);
    materialService = TestBed.inject(MaterialService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Material query and add missing value', () => {
      const materialInventory: IMaterialInventory = { id: 456 };
      const material: IMaterial = { id: 26555 };
      materialInventory.material = material;

      const materialCollection: IMaterial[] = [{ id: 2351 }];
      jest.spyOn(materialService, 'query').mockReturnValue(of(new HttpResponse({ body: materialCollection })));
      const additionalMaterials = [material];
      const expectedCollection: IMaterial[] = [...additionalMaterials, ...materialCollection];
      jest.spyOn(materialService, 'addMaterialToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ materialInventory });
      comp.ngOnInit();

      expect(materialService.query).toHaveBeenCalled();
      expect(materialService.addMaterialToCollectionIfMissing).toHaveBeenCalledWith(
        materialCollection,
        ...additionalMaterials.map(expect.objectContaining),
      );
      expect(comp.materialsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const materialInventory: IMaterialInventory = { id: 456 };
      const material: IMaterial = { id: 23800 };
      materialInventory.material = material;

      activatedRoute.data = of({ materialInventory });
      comp.ngOnInit();

      expect(comp.materialsSharedCollection).toContain(material);
      expect(comp.materialInventory).toEqual(materialInventory);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaterialInventory>>();
      const materialInventory = { id: 123 };
      jest.spyOn(materialInventoryFormService, 'getMaterialInventory').mockReturnValue(materialInventory);
      jest.spyOn(materialInventoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ materialInventory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: materialInventory }));
      saveSubject.complete();

      // THEN
      expect(materialInventoryFormService.getMaterialInventory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(materialInventoryService.update).toHaveBeenCalledWith(expect.objectContaining(materialInventory));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaterialInventory>>();
      const materialInventory = { id: 123 };
      jest.spyOn(materialInventoryFormService, 'getMaterialInventory').mockReturnValue({ id: null });
      jest.spyOn(materialInventoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ materialInventory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: materialInventory }));
      saveSubject.complete();

      // THEN
      expect(materialInventoryFormService.getMaterialInventory).toHaveBeenCalled();
      expect(materialInventoryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaterialInventory>>();
      const materialInventory = { id: 123 };
      jest.spyOn(materialInventoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ materialInventory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(materialInventoryService.update).toHaveBeenCalled();
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
  });
});
