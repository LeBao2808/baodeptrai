import { ISupplier, NewSupplier } from './supplier.model';

export const sampleWithRequiredData: ISupplier = {
  id: 6852,
};

export const sampleWithPartialData: ISupplier = {
  id: 2343,
};

export const sampleWithFullData: ISupplier = {
  id: 32074,
  name: 'midst shimmering',
  address: 'moustache whoa',
  phone: '0212 9321 3329',
};

export const sampleWithNewData: NewSupplier = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
