import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ProductOrderDetailComponent } from './list/product-order-detail.component';
import { ProductOrderDetailDetailComponent } from './detail/product-order-detail-detail.component';
import { ProductOrderDetailUpdateComponent } from './update/product-order-detail-update.component';
import ProductOrderDetailResolve from './route/product-order-detail-routing-resolve.service';

const productOrderDetailRoute: Routes = [
  {
    path: '',
    component: ProductOrderDetailComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProductOrderDetailDetailComponent,
    resolve: {
      productOrderDetail: ProductOrderDetailResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProductOrderDetailUpdateComponent,
    resolve: {
      productOrderDetail: ProductOrderDetailResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProductOrderDetailUpdateComponent,
    resolve: {
      productOrderDetail: ProductOrderDetailResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default productOrderDetailRoute;
