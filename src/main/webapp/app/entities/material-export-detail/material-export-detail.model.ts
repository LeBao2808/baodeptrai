import { IMaterialExport } from 'app/entities/material-export/material-export.model';
import { IMaterial } from 'app/entities/material/material.model';

export interface IMaterialExportDetail {
  id: number;
  note?: string | null;
  exportPrice?: number | null;
  quantity?: number | null;
  materialExport?: Pick<IMaterialExport, 'id'> | null;
  material?: Pick<IMaterial, 'id'> | null;
}

export type NewMaterialExportDetail = Omit<IMaterialExportDetail, 'id'> & { id: null };
