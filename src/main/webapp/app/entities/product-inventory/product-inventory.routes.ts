import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ProductInventoryComponent } from './list/product-inventory.component';
import { ProductInventoryDetailComponent } from './detail/product-inventory-detail.component';
import { ProductInventoryUpdateComponent } from './update/product-inventory-update.component';
import ProductInventoryResolve from './route/product-inventory-routing-resolve.service';

const productInventoryRoute: Routes = [
  {
    path: '',
    component: ProductInventoryComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProductInventoryDetailComponent,
    resolve: {
      productInventory: ProductInventoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProductInventoryUpdateComponent,
    resolve: {
      productInventory: ProductInventoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProductInventoryUpdateComponent,
    resolve: {
      productInventory: ProductInventoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default productInventoryRoute;
