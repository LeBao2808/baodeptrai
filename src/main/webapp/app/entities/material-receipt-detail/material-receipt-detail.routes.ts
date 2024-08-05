import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { MaterialReceiptDetailComponent } from './list/material-receipt-detail.component';
import { MaterialReceiptDetailDetailComponent } from './detail/material-receipt-detail-detail.component';
import { MaterialReceiptDetailUpdateComponent } from './update/material-receipt-detail-update.component';
import MaterialReceiptDetailResolve from './route/material-receipt-detail-routing-resolve.service';

const materialReceiptDetailRoute: Routes = [
  {
    path: '',
    component: MaterialReceiptDetailComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MaterialReceiptDetailDetailComponent,
    resolve: {
      materialReceiptDetail: MaterialReceiptDetailResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MaterialReceiptDetailUpdateComponent,
    resolve: {
      materialReceiptDetail: MaterialReceiptDetailResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MaterialReceiptDetailUpdateComponent,
    resolve: {
      materialReceiptDetail: MaterialReceiptDetailResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default materialReceiptDetailRoute;
