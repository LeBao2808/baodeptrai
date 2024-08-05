import dayjs from 'dayjs/esm';
import { IPlanning } from 'app/entities/planning/planning.model';
import { ICustomer } from 'app/entities/customer/customer.model';
import { IUser } from 'app/entities/user/user.model';

export interface IProductOrder {
  id: number;
  paymentMethod?: string | null;
  note?: string | null;
  status?: number | null;
  orderDate?: dayjs.Dayjs | null;
  paymentDate?: dayjs.Dayjs | null;
  warehouseReleaseDate?: dayjs.Dayjs | null;
  code?: string | null;
  quantificationOrder?: Pick<IPlanning, 'id'> | null;
  customer?: Pick<ICustomer, 'id'> | null;
  createdByUser?: Pick<IUser, 'id'> | null;
}

export type NewProductOrder = Omit<IProductOrder, 'id'> & { id: null };
