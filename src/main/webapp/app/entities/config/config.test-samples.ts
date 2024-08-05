import { IConfig, NewConfig } from './config.model';

export const sampleWithRequiredData: IConfig = {
  id: 6999,
};

export const sampleWithPartialData: IConfig = {
  id: 22388,
  key: 'anti incidentally',
  value: 'lead index nail',
};

export const sampleWithFullData: IConfig = {
  id: 4011,
  key: 'indeed duh anenst',
  value: 'heart image',
};

export const sampleWithNewData: NewConfig = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
