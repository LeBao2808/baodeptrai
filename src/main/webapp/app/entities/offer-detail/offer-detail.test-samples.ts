import { IOfferDetail, NewOfferDetail } from './offer-detail.model';

export const sampleWithRequiredData: IOfferDetail = {
  id: 24979,
};

export const sampleWithPartialData: IOfferDetail = {
  id: 12333,
};

export const sampleWithFullData: IOfferDetail = {
  id: 1153,
  feedback: 'occupation veterinarian yearly',
};

export const sampleWithNewData: NewOfferDetail = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
