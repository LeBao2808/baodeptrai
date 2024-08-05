import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../product-order-detail.test-samples';

import { ProductOrderDetailFormService } from './product-order-detail-form.service';

describe('ProductOrderDetail Form Service', () => {
  let service: ProductOrderDetailFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProductOrderDetailFormService);
  });

  describe('Service methods', () => {
    describe('createProductOrderDetailFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductOrderDetailFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            orderCreationDate: expect.any(Object),
            quantity: expect.any(Object),
            unitPrice: expect.any(Object),
            order: expect.any(Object),
            product: expect.any(Object),
          }),
        );
      });

      it('passing IProductOrderDetail should create a new form with FormGroup', () => {
        const formGroup = service.createProductOrderDetailFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            orderCreationDate: expect.any(Object),
            quantity: expect.any(Object),
            unitPrice: expect.any(Object),
            order: expect.any(Object),
            product: expect.any(Object),
          }),
        );
      });
    });

    describe('getProductOrderDetail', () => {
      it('should return NewProductOrderDetail for default ProductOrderDetail initial value', () => {
        const formGroup = service.createProductOrderDetailFormGroup(sampleWithNewData);

        const productOrderDetail = service.getProductOrderDetail(formGroup) as any;

        expect(productOrderDetail).toMatchObject(sampleWithNewData);
      });

      it('should return NewProductOrderDetail for empty ProductOrderDetail initial value', () => {
        const formGroup = service.createProductOrderDetailFormGroup();

        const productOrderDetail = service.getProductOrderDetail(formGroup) as any;

        expect(productOrderDetail).toMatchObject({});
      });

      it('should return IProductOrderDetail', () => {
        const formGroup = service.createProductOrderDetailFormGroup(sampleWithRequiredData);

        const productOrderDetail = service.getProductOrderDetail(formGroup) as any;

        expect(productOrderDetail).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProductOrderDetail should not enable id FormControl', () => {
        const formGroup = service.createProductOrderDetailFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProductOrderDetail should disable id FormControl', () => {
        const formGroup = service.createProductOrderDetailFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
