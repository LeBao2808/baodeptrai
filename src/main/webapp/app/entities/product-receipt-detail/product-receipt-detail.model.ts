import { IProduct } from 'app/entities/product/product.model';
import { IProductReceipt } from 'app/entities/product-receipt/product-receipt.model';

export interface IProductReceiptDetail {
  id: number;
  note?: string | null;
  quantity?: number | null;
  product?: Pick<IProduct, 'id'> | null;
  receipt?: Pick<IProductReceipt, 'id'> | null;
}

export type NewProductReceiptDetail = Omit<IProductReceiptDetail, 'id'> & { id: null };
