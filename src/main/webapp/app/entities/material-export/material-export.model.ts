import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IProductionSite } from 'app/entities/production-site/production-site.model';

export interface IMaterialExport {
  id: number;
  creationDate?: dayjs.Dayjs | null;
  exportDate?: dayjs.Dayjs | null;
  status?: number | null;
  code?: string | null;
  createdByUser?: Pick<IUser, 'id'> | null;
  productionSite?: Pick<IProductionSite, 'id'> | null;
}

export type NewMaterialExport = Omit<IMaterialExport, 'id'> & { id: null };
