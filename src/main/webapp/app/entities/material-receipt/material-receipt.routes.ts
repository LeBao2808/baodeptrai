import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { MaterialReceiptComponent } from './list/material-receipt.component';
import { MaterialReceiptDetailComponent } from './detail/material-receipt-detail.component';
import { MaterialReceiptUpdateComponent } from './update/material-receipt-update.component';
import MaterialReceiptResolve from './route/material-receipt-routing-resolve.service';

const materialReceiptRoute: Routes = [
  {
    path: '',
    component: MaterialReceiptComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MaterialReceiptDetailComponent,
    resolve: {
      materialReceipt: MaterialReceiptResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MaterialReceiptUpdateComponent,
    resolve: {
      materialReceipt: MaterialReceiptResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MaterialReceiptUpdateComponent,
    resolve: {
      materialReceipt: MaterialReceiptResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default materialReceiptRoute;
