import { IMaterialReceiptDetail, NewMaterialReceiptDetail } from './material-receipt-detail.model';

export const sampleWithRequiredData: IMaterialReceiptDetail = {
  id: 17335,
};

export const sampleWithPartialData: IMaterialReceiptDetail = {
  id: 21895,
  quantity: 26434.5,
};

export const sampleWithFullData: IMaterialReceiptDetail = {
  id: 31072,
  note: 'astride motivate at',
  importPrice: 16646.36,
  quantity: 16530.54,
};

export const sampleWithNewData: NewMaterialReceiptDetail = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
