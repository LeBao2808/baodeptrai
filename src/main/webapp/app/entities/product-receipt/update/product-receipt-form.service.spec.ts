import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../product-receipt.test-samples';

import { ProductReceiptFormService } from './product-receipt-form.service';

describe('ProductReceipt Form Service', () => {
  let service: ProductReceiptFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProductReceiptFormService);
  });

  describe('Service methods', () => {
    describe('createProductReceiptFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductReceiptFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            creationDate: expect.any(Object),
            paymentDate: expect.any(Object),
            receiptDate: expect.any(Object),
            status: expect.any(Object),
            code: expect.any(Object),
            created: expect.any(Object),
          }),
        );
      });

      it('passing IProductReceipt should create a new form with FormGroup', () => {
        const formGroup = service.createProductReceiptFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            creationDate: expect.any(Object),
            paymentDate: expect.any(Object),
            receiptDate: expect.any(Object),
            status: expect.any(Object),
            code: expect.any(Object),
            created: expect.any(Object),
          }),
        );
      });
    });

    describe('getProductReceipt', () => {
      it('should return NewProductReceipt for default ProductReceipt initial value', () => {
        const formGroup = service.createProductReceiptFormGroup(sampleWithNewData);

        const productReceipt = service.getProductReceipt(formGroup) as any;

        expect(productReceipt).toMatchObject(sampleWithNewData);
      });

      it('should return NewProductReceipt for empty ProductReceipt initial value', () => {
        const formGroup = service.createProductReceiptFormGroup();

        const productReceipt = service.getProductReceipt(formGroup) as any;

        expect(productReceipt).toMatchObject({});
      });

      it('should return IProductReceipt', () => {
        const formGroup = service.createProductReceiptFormGroup(sampleWithRequiredData);

        const productReceipt = service.getProductReceipt(formGroup) as any;

        expect(productReceipt).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProductReceipt should not enable id FormControl', () => {
        const formGroup = service.createProductReceiptFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProductReceipt should disable id FormControl', () => {
        const formGroup = service.createProductReceiptFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
