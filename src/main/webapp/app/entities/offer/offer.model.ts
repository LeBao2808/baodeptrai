import dayjs from 'dayjs/esm';
import { ICustomer } from 'app/entities/customer/customer.model';
import { IUser } from 'app/entities/user/user.model';

export interface IOffer {
  id: number;
  date?: dayjs.Dayjs | null;
  status?: number | null;
  code?: string | null;
  customer?: Pick<ICustomer, 'id'> | null;
  user?: Pick<IUser, 'id'> | null;
}

export type NewOffer = Omit<IOffer, 'id'> & { id: null };
