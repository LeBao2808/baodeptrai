import dayjs from 'dayjs/esm';

import { IMaterialExport, NewMaterialExport } from './material-export.model';

export const sampleWithRequiredData: IMaterialExport = {
  id: 13575,
};

export const sampleWithPartialData: IMaterialExport = {
  id: 28794,
  creationDate: dayjs('2024-08-05T10:25'),
  exportDate: dayjs('2024-08-05T09:46'),
};

export const sampleWithFullData: IMaterialExport = {
  id: 21059,
  creationDate: dayjs('2024-08-05T12:10'),
  exportDate: dayjs('2024-08-04T21:09'),
  status: 20250,
  code: 'considerate if',
};

export const sampleWithNewData: NewMaterialExport = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
