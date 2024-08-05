import { IProduct } from 'app/entities/product/product.model';
import { IMaterial } from 'app/entities/material/material.model';

export interface IQuantification {
  id: number;
  quantity?: number | null;
  product?: Pick<IProduct, 'id'> | null;
  material?: Pick<IMaterial, 'id'> | null;
}

export type NewQuantification = Omit<IQuantification, 'id'> & { id: null };
