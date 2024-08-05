import dayjs from 'dayjs/esm';

import { IMaterialReceipt, NewMaterialReceipt } from './material-receipt.model';

export const sampleWithRequiredData: IMaterialReceipt = {
  id: 5261,
};

export const sampleWithPartialData: IMaterialReceipt = {
  id: 26368,
  creationDate: dayjs('2024-08-05T09:30'),
  paymentDate: dayjs('2024-08-05T05:26'),
  status: 26248,
  code: 'boom partner',
};

export const sampleWithFullData: IMaterialReceipt = {
  id: 24524,
  creationDate: dayjs('2024-08-05T09:08'),
  paymentDate: dayjs('2024-08-04T17:50'),
  receiptDate: dayjs('2024-08-04T14:55'),
  status: 3661,
  code: 'regarding gadzooks regarding',
};

export const sampleWithNewData: NewMaterialReceipt = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
