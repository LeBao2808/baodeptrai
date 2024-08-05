import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';

export interface IProductReceipt {
  id: number;
  creationDate?: dayjs.Dayjs | null;
  paymentDate?: dayjs.Dayjs | null;
  receiptDate?: dayjs.Dayjs | null;
  status?: number | null;
  code?: string | null;
  created?: Pick<IUser, 'id'> | null;
}

export type NewProductReceipt = Omit<IProductReceipt, 'id'> & { id: null };
