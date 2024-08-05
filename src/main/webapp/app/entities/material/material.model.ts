export interface IMaterial {
  id: number;
  name?: string | null;
  unit?: string | null;
  code?: string | null;
  description?: string | null;
  imgUrl?: string | null;
}

export type NewMaterial = Omit<IMaterial, 'id'> & { id: null };
