import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { MaterialInventoryComponent } from './list/material-inventory.component';
import { MaterialInventoryDetailComponent } from './detail/material-inventory-detail.component';
import { MaterialInventoryUpdateComponent } from './update/material-inventory-update.component';
import MaterialInventoryResolve from './route/material-inventory-routing-resolve.service';

const materialInventoryRoute: Routes = [
  {
    path: '',
    component: MaterialInventoryComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MaterialInventoryDetailComponent,
    resolve: {
      materialInventory: MaterialInventoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MaterialInventoryUpdateComponent,
    resolve: {
      materialInventory: MaterialInventoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MaterialInventoryUpdateComponent,
    resolve: {
      materialInventory: MaterialInventoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default materialInventoryRoute;
