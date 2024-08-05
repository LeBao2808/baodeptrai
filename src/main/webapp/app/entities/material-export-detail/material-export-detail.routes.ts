import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { MaterialExportDetailComponent } from './list/material-export-detail.component';
import { MaterialExportDetailDetailComponent } from './detail/material-export-detail-detail.component';
import { MaterialExportDetailUpdateComponent } from './update/material-export-detail-update.component';
import MaterialExportDetailResolve from './route/material-export-detail-routing-resolve.service';

const materialExportDetailRoute: Routes = [
  {
    path: '',
    component: MaterialExportDetailComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MaterialExportDetailDetailComponent,
    resolve: {
      materialExportDetail: MaterialExportDetailResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MaterialExportDetailUpdateComponent,
    resolve: {
      materialExportDetail: MaterialExportDetailResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MaterialExportDetailUpdateComponent,
    resolve: {
      materialExportDetail: MaterialExportDetailResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default materialExportDetailRoute;
