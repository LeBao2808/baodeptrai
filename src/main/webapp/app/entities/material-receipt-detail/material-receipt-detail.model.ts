import { IMaterial } from 'app/entities/material/material.model';
import { IMaterialReceipt } from 'app/entities/material-receipt/material-receipt.model';

export interface IMaterialReceiptDetail {
  id: number;
  note?: string | null;
  importPrice?: number | null;
  quantity?: number | null;
  material?: Pick<IMaterial, 'id'> | null;
  receipt?: Pick<IMaterialReceipt, 'id'> | null;
}

export type NewMaterialReceiptDetail = Omit<IMaterialReceiptDetail, 'id'> & { id: null };
