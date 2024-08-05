import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../quantification.test-samples';

import { QuantificationFormService } from './quantification-form.service';

describe('Quantification Form Service', () => {
  let service: QuantificationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(QuantificationFormService);
  });

  describe('Service methods', () => {
    describe('createQuantificationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createQuantificationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            quantity: expect.any(Object),
            product: expect.any(Object),
            material: expect.any(Object),
          }),
        );
      });

      it('passing IQuantification should create a new form with FormGroup', () => {
        const formGroup = service.createQuantificationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            quantity: expect.any(Object),
            product: expect.any(Object),
            material: expect.any(Object),
          }),
        );
      });
    });

    describe('getQuantification', () => {
      it('should return NewQuantification for default Quantification initial value', () => {
        const formGroup = service.createQuantificationFormGroup(sampleWithNewData);

        const quantification = service.getQuantification(formGroup) as any;

        expect(quantification).toMatchObject(sampleWithNewData);
      });

      it('should return NewQuantification for empty Quantification initial value', () => {
        const formGroup = service.createQuantificationFormGroup();

        const quantification = service.getQuantification(formGroup) as any;

        expect(quantification).toMatchObject({});
      });

      it('should return IQuantification', () => {
        const formGroup = service.createQuantificationFormGroup(sampleWithRequiredData);

        const quantification = service.getQuantification(formGroup) as any;

        expect(quantification).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IQuantification should not enable id FormControl', () => {
        const formGroup = service.createQuantificationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewQuantification should disable id FormControl', () => {
        const formGroup = service.createQuantificationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
