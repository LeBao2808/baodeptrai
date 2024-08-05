import { IMaterialExportDetail, NewMaterialExportDetail } from './material-export-detail.model';

export const sampleWithRequiredData: IMaterialExportDetail = {
  id: 8890,
};

export const sampleWithPartialData: IMaterialExportDetail = {
  id: 1063,
  note: 'yearly hierarchy',
};

export const sampleWithFullData: IMaterialExportDetail = {
  id: 18129,
  note: 'among limp till',
  exportPrice: 27434.04,
  quantity: 20253.09,
};

export const sampleWithNewData: NewMaterialExportDetail = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
