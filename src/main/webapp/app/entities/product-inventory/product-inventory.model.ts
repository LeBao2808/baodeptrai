import { IProduct } from 'app/entities/product/product.model';

export interface IProductInventory {
  id: number;
  quantityOnHand?: number | null;
  inventoryMonth?: number | null;
  inventoryYear?: number | null;
  type?: number | null;
  price?: number | null;
  product?: Pick<IProduct, 'id'> | null;
}

export type NewProductInventory = Omit<IProductInventory, 'id'> & { id: null };
