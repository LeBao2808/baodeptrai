import { IMaterial } from 'app/entities/material/material.model';

export interface IProduct {
  id: number;
  name?: string | null;
  code?: string | null;
  unit?: string | null;
  description?: string | null;
  weight?: number | null;
  height?: number | null;
  width?: number | null;
  length?: number | null;
  imageUrl?: string | null;
  type?: number | null;
  color?: string | null;
  cbm?: number | null;
  price?: number | null;
  construction?: number | null;
  masterPackingQty?: number | null;
  masterNestCode?: number | null;
  innerQty?: number | null;
  packSize?: number | null;
  nestCode?: string | null;
  countryOfOrigin?: string | null;
  vendorName?: string | null;
  numberOfSet?: number | null;
  priceFOB?: number | null;
  qty40HC?: number | null;
  d57Qty?: number | null;
  category?: string | null;
  labels?: string | null;
  planningNotes?: string | null;
  factTag?: string | null;
  packagingLength?: number | null;
  packagingHeight?: number | null;
  packagingWidth?: number | null;
  setIdProduct?: Pick<IProduct, 'id'> | null;
  parentProduct?: Pick<IProduct, 'id'> | null;
  material?: Pick<IMaterial, 'id'> | null;
}

export type NewProduct = Omit<IProduct, 'id'> & { id: null };
