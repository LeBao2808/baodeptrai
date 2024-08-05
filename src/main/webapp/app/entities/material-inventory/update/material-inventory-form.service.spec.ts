import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../material-inventory.test-samples';

import { MaterialInventoryFormService } from './material-inventory-form.service';

describe('MaterialInventory Form Service', () => {
  let service: MaterialInventoryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MaterialInventoryFormService);
  });

  describe('Service methods', () => {
    describe('createMaterialInventoryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMaterialInventoryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            quantityOnHand: expect.any(Object),
            inventoryMonth: expect.any(Object),
            inventoryYear: expect.any(Object),
            type: expect.any(Object),
            price: expect.any(Object),
            material: expect.any(Object),
          }),
        );
      });

      it('passing IMaterialInventory should create a new form with FormGroup', () => {
        const formGroup = service.createMaterialInventoryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            quantityOnHand: expect.any(Object),
            inventoryMonth: expect.any(Object),
            inventoryYear: expect.any(Object),
            type: expect.any(Object),
            price: expect.any(Object),
            material: expect.any(Object),
          }),
        );
      });
    });

    describe('getMaterialInventory', () => {
      it('should return NewMaterialInventory for default MaterialInventory initial value', () => {
        const formGroup = service.createMaterialInventoryFormGroup(sampleWithNewData);

        const materialInventory = service.getMaterialInventory(formGroup) as any;

        expect(materialInventory).toMatchObject(sampleWithNewData);
      });

      it('should return NewMaterialInventory for empty MaterialInventory initial value', () => {
        const formGroup = service.createMaterialInventoryFormGroup();

        const materialInventory = service.getMaterialInventory(formGroup) as any;

        expect(materialInventory).toMatchObject({});
      });

      it('should return IMaterialInventory', () => {
        const formGroup = service.createMaterialInventoryFormGroup(sampleWithRequiredData);

        const materialInventory = service.getMaterialInventory(formGroup) as any;

        expect(materialInventory).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMaterialInventory should not enable id FormControl', () => {
        const formGroup = service.createMaterialInventoryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMaterialInventory should disable id FormControl', () => {
        const formGroup = service.createMaterialInventoryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
