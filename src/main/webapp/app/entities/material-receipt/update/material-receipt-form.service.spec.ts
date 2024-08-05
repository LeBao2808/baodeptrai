import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../material-receipt.test-samples';

import { MaterialReceiptFormService } from './material-receipt-form.service';

describe('MaterialReceipt Form Service', () => {
  let service: MaterialReceiptFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MaterialReceiptFormService);
  });

  describe('Service methods', () => {
    describe('createMaterialReceiptFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMaterialReceiptFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            creationDate: expect.any(Object),
            paymentDate: expect.any(Object),
            receiptDate: expect.any(Object),
            status: expect.any(Object),
            code: expect.any(Object),
            createdByUser: expect.any(Object),
            quantificationOrder: expect.any(Object),
          }),
        );
      });

      it('passing IMaterialReceipt should create a new form with FormGroup', () => {
        const formGroup = service.createMaterialReceiptFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            creationDate: expect.any(Object),
            paymentDate: expect.any(Object),
            receiptDate: expect.any(Object),
            status: expect.any(Object),
            code: expect.any(Object),
            createdByUser: expect.any(Object),
            quantificationOrder: expect.any(Object),
          }),
        );
      });
    });

    describe('getMaterialReceipt', () => {
      it('should return NewMaterialReceipt for default MaterialReceipt initial value', () => {
        const formGroup = service.createMaterialReceiptFormGroup(sampleWithNewData);

        const materialReceipt = service.getMaterialReceipt(formGroup) as any;

        expect(materialReceipt).toMatchObject(sampleWithNewData);
      });

      it('should return NewMaterialReceipt for empty MaterialReceipt initial value', () => {
        const formGroup = service.createMaterialReceiptFormGroup();

        const materialReceipt = service.getMaterialReceipt(formGroup) as any;

        expect(materialReceipt).toMatchObject({});
      });

      it('should return IMaterialReceipt', () => {
        const formGroup = service.createMaterialReceiptFormGroup(sampleWithRequiredData);

        const materialReceipt = service.getMaterialReceipt(formGroup) as any;

        expect(materialReceipt).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMaterialReceipt should not enable id FormControl', () => {
        const formGroup = service.createMaterialReceiptFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMaterialReceipt should disable id FormControl', () => {
        const formGroup = service.createMaterialReceiptFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
