import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../material-receipt-detail.test-samples';

import { MaterialReceiptDetailFormService } from './material-receipt-detail-form.service';

describe('MaterialReceiptDetail Form Service', () => {
  let service: MaterialReceiptDetailFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MaterialReceiptDetailFormService);
  });

  describe('Service methods', () => {
    describe('createMaterialReceiptDetailFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMaterialReceiptDetailFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            note: expect.any(Object),
            importPrice: expect.any(Object),
            quantity: expect.any(Object),
            material: expect.any(Object),
            receipt: expect.any(Object),
          }),
        );
      });

      it('passing IMaterialReceiptDetail should create a new form with FormGroup', () => {
        const formGroup = service.createMaterialReceiptDetailFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            note: expect.any(Object),
            importPrice: expect.any(Object),
            quantity: expect.any(Object),
            material: expect.any(Object),
            receipt: expect.any(Object),
          }),
        );
      });
    });

    describe('getMaterialReceiptDetail', () => {
      it('should return NewMaterialReceiptDetail for default MaterialReceiptDetail initial value', () => {
        const formGroup = service.createMaterialReceiptDetailFormGroup(sampleWithNewData);

        const materialReceiptDetail = service.getMaterialReceiptDetail(formGroup) as any;

        expect(materialReceiptDetail).toMatchObject(sampleWithNewData);
      });

      it('should return NewMaterialReceiptDetail for empty MaterialReceiptDetail initial value', () => {
        const formGroup = service.createMaterialReceiptDetailFormGroup();

        const materialReceiptDetail = service.getMaterialReceiptDetail(formGroup) as any;

        expect(materialReceiptDetail).toMatchObject({});
      });

      it('should return IMaterialReceiptDetail', () => {
        const formGroup = service.createMaterialReceiptDetailFormGroup(sampleWithRequiredData);

        const materialReceiptDetail = service.getMaterialReceiptDetail(formGroup) as any;

        expect(materialReceiptDetail).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMaterialReceiptDetail should not enable id FormControl', () => {
        const formGroup = service.createMaterialReceiptDetailFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMaterialReceiptDetail should disable id FormControl', () => {
        const formGroup = service.createMaterialReceiptDetailFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
