import dayjs from 'dayjs/esm';
import { IProductOrder } from 'app/entities/product-order/product-order.model';
import { IProduct } from 'app/entities/product/product.model';

export interface IProductOrderDetail {
  id: number;
  orderCreationDate?: dayjs.Dayjs | null;
  quantity?: number | null;
  unitPrice?: number | null;
  order?: Pick<IProductOrder, 'id'> | null;
  product?: Pick<IProduct, 'id'> | null;
}

export type NewProductOrderDetail = Omit<IProductOrderDetail, 'id'> & { id: null };
