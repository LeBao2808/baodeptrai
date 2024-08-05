import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ProductReceiptDetailComponent } from './list/product-receipt-detail.component';
import { ProductReceiptDetailDetailComponent } from './detail/product-receipt-detail-detail.component';
import { ProductReceiptDetailUpdateComponent } from './update/product-receipt-detail-update.component';
import ProductReceiptDetailResolve from './route/product-receipt-detail-routing-resolve.service';

const productReceiptDetailRoute: Routes = [
  {
    path: '',
    component: ProductReceiptDetailComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProductReceiptDetailDetailComponent,
    resolve: {
      productReceiptDetail: ProductReceiptDetailResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProductReceiptDetailUpdateComponent,
    resolve: {
      productReceiptDetail: ProductReceiptDetailResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProductReceiptDetailUpdateComponent,
    resolve: {
      productReceiptDetail: ProductReceiptDetailResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default productReceiptDetailRoute;
