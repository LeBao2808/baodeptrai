import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ProductReceiptComponent } from './list/product-receipt.component';
import { ProductReceiptDetailComponent } from './detail/product-receipt-detail.component';
import { ProductReceiptUpdateComponent } from './update/product-receipt-update.component';
import ProductReceiptResolve from './route/product-receipt-routing-resolve.service';

const productReceiptRoute: Routes = [
  {
    path: '',
    component: ProductReceiptComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProductReceiptDetailComponent,
    resolve: {
      productReceipt: ProductReceiptResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProductReceiptUpdateComponent,
    resolve: {
      productReceipt: ProductReceiptResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProductReceiptUpdateComponent,
    resolve: {
      productReceipt: ProductReceiptResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default productReceiptRoute;
