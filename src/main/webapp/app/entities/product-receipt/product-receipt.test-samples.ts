import dayjs from 'dayjs/esm';

import { IProductReceipt, NewProductReceipt } from './product-receipt.model';

export const sampleWithRequiredData: IProductReceipt = {
  id: 15481,
};

export const sampleWithPartialData: IProductReceipt = {
  id: 8805,
  creationDate: dayjs('2024-08-05T09:54'),
  paymentDate: dayjs('2024-08-04T19:59'),
  receiptDate: dayjs('2024-08-04T22:50'),
  status: 4236,
};

export const sampleWithFullData: IProductReceipt = {
  id: 23493,
  creationDate: dayjs('2024-08-04T23:58'),
  paymentDate: dayjs('2024-08-04T14:06'),
  receiptDate: dayjs('2024-08-05T05:45'),
  status: 31794,
  code: 'mist',
};

export const sampleWithNewData: NewProductReceipt = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
