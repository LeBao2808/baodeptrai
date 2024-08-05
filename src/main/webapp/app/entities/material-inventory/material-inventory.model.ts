import { IMaterial } from 'app/entities/material/material.model';

export interface IMaterialInventory {
  id: number;
  quantityOnHand?: number | null;
  inventoryMonth?: number | null;
  inventoryYear?: number | null;
  type?: number | null;
  price?: number | null;
  material?: Pick<IMaterial, 'id'> | null;
}

export type NewMaterialInventory = Omit<IMaterialInventory, 'id'> & { id: null };
