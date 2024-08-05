import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../product.test-samples';

import { ProductFormService } from './product-form.service';

describe('Product Form Service', () => {
  let service: ProductFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProductFormService);
  });

  describe('Service methods', () => {
    describe('createProductFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            code: expect.any(Object),
            unit: expect.any(Object),
            description: expect.any(Object),
            weight: expect.any(Object),
            height: expect.any(Object),
            width: expect.any(Object),
            length: expect.any(Object),
            imageUrl: expect.any(Object),
            type: expect.any(Object),
            color: expect.any(Object),
            cbm: expect.any(Object),
            price: expect.any(Object),
            construction: expect.any(Object),
            masterPackingQty: expect.any(Object),
            masterNestCode: expect.any(Object),
            innerQty: expect.any(Object),
            packSize: expect.any(Object),
            nestCode: expect.any(Object),
            countryOfOrigin: expect.any(Object),
            vendorName: expect.any(Object),
            numberOfSet: expect.any(Object),
            priceFOB: expect.any(Object),
            qty40HC: expect.any(Object),
            d57Qty: expect.any(Object),
            category: expect.any(Object),
            labels: expect.any(Object),
            planningNotes: expect.any(Object),
            factTag: expect.any(Object),
            packagingLength: expect.any(Object),
            packagingHeight: expect.any(Object),
            packagingWidth: expect.any(Object),
            setIdProduct: expect.any(Object),
            parentProduct: expect.any(Object),
            material: expect.any(Object),
          }),
        );
      });

      it('passing IProduct should create a new form with FormGroup', () => {
        const formGroup = service.createProductFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            code: expect.any(Object),
            unit: expect.any(Object),
            description: expect.any(Object),
            weight: expect.any(Object),
            height: expect.any(Object),
            width: expect.any(Object),
            length: expect.any(Object),
            imageUrl: expect.any(Object),
            type: expect.any(Object),
            color: expect.any(Object),
            cbm: expect.any(Object),
            price: expect.any(Object),
            construction: expect.any(Object),
            masterPackingQty: expect.any(Object),
            masterNestCode: expect.any(Object),
            innerQty: expect.any(Object),
            packSize: expect.any(Object),
            nestCode: expect.any(Object),
            countryOfOrigin: expect.any(Object),
            vendorName: expect.any(Object),
            numberOfSet: expect.any(Object),
            priceFOB: expect.any(Object),
            qty40HC: expect.any(Object),
            d57Qty: expect.any(Object),
            category: expect.any(Object),
            labels: expect.any(Object),
            planningNotes: expect.any(Object),
            factTag: expect.any(Object),
            packagingLength: expect.any(Object),
            packagingHeight: expect.any(Object),
            packagingWidth: expect.any(Object),
            setIdProduct: expect.any(Object),
            parentProduct: expect.any(Object),
            material: expect.any(Object),
          }),
        );
      });
    });

    describe('getProduct', () => {
      it('should return NewProduct for default Product initial value', () => {
        const formGroup = service.createProductFormGroup(sampleWithNewData);

        const product = service.getProduct(formGroup) as any;

        expect(product).toMatchObject(sampleWithNewData);
      });

      it('should return NewProduct for empty Product initial value', () => {
        const formGroup = service.createProductFormGroup();

        const product = service.getProduct(formGroup) as any;

        expect(product).toMatchObject({});
      });

      it('should return IProduct', () => {
        const formGroup = service.createProductFormGroup(sampleWithRequiredData);

        const product = service.getProduct(formGroup) as any;

        expect(product).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProduct should not enable id FormControl', () => {
        const formGroup = service.createProductFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProduct should disable id FormControl', () => {
        const formGroup = service.createProductFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
