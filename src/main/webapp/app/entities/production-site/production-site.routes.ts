import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ProductionSiteComponent } from './list/production-site.component';
import { ProductionSiteDetailComponent } from './detail/production-site-detail.component';
import { ProductionSiteUpdateComponent } from './update/production-site-update.component';
import ProductionSiteResolve from './route/production-site-routing-resolve.service';

const productionSiteRoute: Routes = [
  {
    path: '',
    component: ProductionSiteComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProductionSiteDetailComponent,
    resolve: {
      productionSite: ProductionSiteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProductionSiteUpdateComponent,
    resolve: {
      productionSite: ProductionSiteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProductionSiteUpdateComponent,
    resolve: {
      productionSite: ProductionSiteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default productionSiteRoute;
