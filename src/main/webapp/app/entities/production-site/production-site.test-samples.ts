import { IProductionSite, NewProductionSite } from './production-site.model';

export const sampleWithRequiredData: IProductionSite = {
  id: 11254,
};

export const sampleWithPartialData: IProductionSite = {
  id: 16683,
  name: 'count',
  address: 'brr',
  phone: '028 6297 5448',
};

export const sampleWithFullData: IProductionSite = {
  id: 4350,
  name: 'provided',
  address: 'hence whether where',
  phone: '023 0214 6925',
};

export const sampleWithNewData: NewProductionSite = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
