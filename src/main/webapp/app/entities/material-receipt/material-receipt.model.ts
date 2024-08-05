import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IPlanning } from 'app/entities/planning/planning.model';

export interface IMaterialReceipt {
  id: number;
  creationDate?: dayjs.Dayjs | null;
  paymentDate?: dayjs.Dayjs | null;
  receiptDate?: dayjs.Dayjs | null;
  status?: number | null;
  code?: string | null;
  createdByUser?: Pick<IUser, 'id'> | null;
  quantificationOrder?: Pick<IPlanning, 'id'> | null;
}

export type NewMaterialReceipt = Omit<IMaterialReceipt, 'id'> & { id: null };
