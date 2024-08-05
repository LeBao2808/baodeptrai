import dayjs from 'dayjs/esm';

import { IProductOrderDetail, NewProductOrderDetail } from './product-order-detail.model';

export const sampleWithRequiredData: IProductOrderDetail = {
  id: 11354,
};

export const sampleWithPartialData: IProductOrderDetail = {
  id: 30558,
  orderCreationDate: dayjs('2024-08-05T05:33'),
  quantity: 1923,
  unitPrice: 24838.3,
};

export const sampleWithFullData: IProductOrderDetail = {
  id: 12878,
  orderCreationDate: dayjs('2024-08-05T00:27'),
  quantity: 23614,
  unitPrice: 20322.67,
};

export const sampleWithNewData: NewProductOrderDetail = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
