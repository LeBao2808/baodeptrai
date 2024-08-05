import dayjs from 'dayjs/esm';

import { IProductOrder, NewProductOrder } from './product-order.model';

export const sampleWithRequiredData: IProductOrder = {
  id: 8449,
};

export const sampleWithPartialData: IProductOrder = {
  id: 7273,
  paymentMethod: 'despite',
  orderDate: dayjs('2024-08-05T12:06'),
  warehouseReleaseDate: dayjs('2024-08-05T10:58'),
  code: 'laboratory',
};

export const sampleWithFullData: IProductOrder = {
  id: 32559,
  paymentMethod: 'slight',
  note: 'whirlwind firsthand',
  status: 20889,
  orderDate: dayjs('2024-08-04T22:00'),
  paymentDate: dayjs('2024-08-05T03:37'),
  warehouseReleaseDate: dayjs('2024-08-04T14:44'),
  code: 'blue quarrelsomely',
};

export const sampleWithNewData: NewProductOrder = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
