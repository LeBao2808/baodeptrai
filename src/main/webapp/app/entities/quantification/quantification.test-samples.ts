import { IQuantification, NewQuantification } from './quantification.model';

export const sampleWithRequiredData: IQuantification = {
  id: 25064,
};

export const sampleWithPartialData: IQuantification = {
  id: 28475,
  quantity: 20427.75,
};

export const sampleWithFullData: IQuantification = {
  id: 9235,
  quantity: 3223.09,
};

export const sampleWithNewData: NewQuantification = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
