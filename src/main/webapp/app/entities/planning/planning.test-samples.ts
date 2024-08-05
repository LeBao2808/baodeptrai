import dayjs from 'dayjs/esm';

import { IPlanning, NewPlanning } from './planning.model';

export const sampleWithRequiredData: IPlanning = {
  id: 14680,
};

export const sampleWithPartialData: IPlanning = {
  id: 8532,
  status: 1855,
  code: 'unimpressively brr rundown',
};

export const sampleWithFullData: IPlanning = {
  id: 15876,
  orderCreationDate: dayjs('2024-08-05T07:45'),
  status: 17786,
  code: 'excluding provided',
};

export const sampleWithNewData: NewPlanning = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
