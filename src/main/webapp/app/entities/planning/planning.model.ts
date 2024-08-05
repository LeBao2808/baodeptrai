import dayjs from 'dayjs/esm';
import { IQuantification } from 'app/entities/quantification/quantification.model';

export interface IPlanning {
  id: number;
  orderCreationDate?: dayjs.Dayjs | null;
  status?: number | null;
  code?: string | null;
  quantification?: Pick<IQuantification, 'id'> | null;
}

export type NewPlanning = Omit<IPlanning, 'id'> & { id: null };
