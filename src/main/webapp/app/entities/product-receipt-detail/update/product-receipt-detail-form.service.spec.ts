import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../product-receipt-detail.test-samples';

import { ProductReceiptDetailFormService } from './product-receipt-detail-form.service';

describe('ProductReceiptDetail Form Service', () => {
  let service: ProductReceiptDetailFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProductReceiptDetailFormService);
  });

  describe('Service methods', () => {
    describe('createProductReceiptDetailFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductReceiptDetailFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            note: expect.any(Object),
            quantity: expect.any(Object),
            product: expect.any(Object),
            receipt: expect.any(Object),
          }),
        );
      });

      it('passing IProductReceiptDetail should create a new form with FormGroup', () => {
        const formGroup = service.createProductReceiptDetailFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            note: expect.any(Object),
            quantity: expect.any(Object),
            product: expect.any(Object),
            receipt: expect.any(Object),
          }),
        );
      });
    });

    describe('getProductReceiptDetail', () => {
      it('should return NewProductReceiptDetail for default ProductReceiptDetail initial value', () => {
        const formGroup = service.createProductReceiptDetailFormGroup(sampleWithNewData);

        const productReceiptDetail = service.getProductReceiptDetail(formGroup) as any;

        expect(productReceiptDetail).toMatchObject(sampleWithNewData);
      });

      it('should return NewProductReceiptDetail for empty ProductReceiptDetail initial value', () => {
        const formGroup = service.createProductReceiptDetailFormGroup();

        const productReceiptDetail = service.getProductReceiptDetail(formGroup) as any;

        expect(productReceiptDetail).toMatchObject({});
      });

      it('should return IProductReceiptDetail', () => {
        const formGroup = service.createProductReceiptDetailFormGroup(sampleWithRequiredData);

        const productReceiptDetail = service.getProductReceiptDetail(formGroup) as any;

        expect(productReceiptDetail).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProductReceiptDetail should not enable id FormControl', () => {
        const formGroup = service.createProductReceiptDetailFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProductReceiptDetail should disable id FormControl', () => {
        const formGroup = service.createProductReceiptDetailFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
