import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { MaterialExportComponent } from './list/material-export.component';
import { MaterialExportDetailComponent } from './detail/material-export-detail.component';
import { MaterialExportUpdateComponent } from './update/material-export-update.component';
import MaterialExportResolve from './route/material-export-routing-resolve.service';

const materialExportRoute: Routes = [
  {
    path: '',
    component: MaterialExportComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MaterialExportDetailComponent,
    resolve: {
      materialExport: MaterialExportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MaterialExportUpdateComponent,
    resolve: {
      materialExport: MaterialExportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MaterialExportUpdateComponent,
    resolve: {
      materialExport: MaterialExportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default materialExportRoute;
