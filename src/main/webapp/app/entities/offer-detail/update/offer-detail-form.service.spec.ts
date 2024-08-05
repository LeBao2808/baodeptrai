import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../offer-detail.test-samples';

import { OfferDetailFormService } from './offer-detail-form.service';

describe('OfferDetail Form Service', () => {
  let service: OfferDetailFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OfferDetailFormService);
  });

  describe('Service methods', () => {
    describe('createOfferDetailFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOfferDetailFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            feedback: expect.any(Object),
            product: expect.any(Object),
            offer: expect.any(Object),
          }),
        );
      });

      it('passing IOfferDetail should create a new form with FormGroup', () => {
        const formGroup = service.createOfferDetailFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            feedback: expect.any(Object),
            product: expect.any(Object),
            offer: expect.any(Object),
          }),
        );
      });
    });

    describe('getOfferDetail', () => {
      it('should return NewOfferDetail for default OfferDetail initial value', () => {
        const formGroup = service.createOfferDetailFormGroup(sampleWithNewData);

        const offerDetail = service.getOfferDetail(formGroup) as any;

        expect(offerDetail).toMatchObject(sampleWithNewData);
      });

      it('should return NewOfferDetail for empty OfferDetail initial value', () => {
        const formGroup = service.createOfferDetailFormGroup();

        const offerDetail = service.getOfferDetail(formGroup) as any;

        expect(offerDetail).toMatchObject({});
      });

      it('should return IOfferDetail', () => {
        const formGroup = service.createOfferDetailFormGroup(sampleWithRequiredData);

        const offerDetail = service.getOfferDetail(formGroup) as any;

        expect(offerDetail).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOfferDetail should not enable id FormControl', () => {
        const formGroup = service.createOfferDetailFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOfferDetail should disable id FormControl', () => {
        const formGroup = service.createOfferDetailFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
