import { IProductReceiptDetail, NewProductReceiptDetail } from './product-receipt-detail.model';

export const sampleWithRequiredData: IProductReceiptDetail = {
  id: 14885,
};

export const sampleWithPartialData: IProductReceiptDetail = {
  id: 21063,
  note: 'overdevelop',
};

export const sampleWithFullData: IProductReceiptDetail = {
  id: 14737,
  note: 'well-lit',
  quantity: 12822,
};

export const sampleWithNewData: NewProductReceiptDetail = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
