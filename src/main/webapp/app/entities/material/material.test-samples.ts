import { IMaterial, NewMaterial } from './material.model';

export const sampleWithRequiredData: IMaterial = {
  id: 26111,
};

export const sampleWithPartialData: IMaterial = {
  id: 23517,
  name: 'boohoo serialise',
  unit: 'in when hence',
  code: 'crossly caulk',
  description: 'dreary hallmark',
  imgUrl: 'how all',
};

export const sampleWithFullData: IMaterial = {
  id: 23512,
  name: 'since throughout',
  unit: 'after guitar',
  code: 'hound interconnect',
  description: 'meager before ouch',
  imgUrl: 'apropos sensitise creepy',
};

export const sampleWithNewData: NewMaterial = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
