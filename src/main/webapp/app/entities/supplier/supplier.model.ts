import { IMaterial } from 'app/entities/material/material.model';

export interface ISupplier {
  id: number;
  name?: string | null;
  address?: string | null;
  phone?: string | null;
  materialId?: Pick<IMaterial, 'id'> | null;
}

export type NewSupplier = Omit<ISupplier, 'id'> & { id: null };
