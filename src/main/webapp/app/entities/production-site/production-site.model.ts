import { IProduct } from 'app/entities/product/product.model';

export interface IProductionSite {
  id: number;
  name?: string | null;
  address?: string | null;
  phone?: string | null;
  productId?: Pick<IProduct, 'id'> | null;
}

export type NewProductionSite = Omit<IProductionSite, 'id'> & { id: null };
