import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { QuantificationComponent } from './list/quantification.component';
import { QuantificationDetailComponent } from './detail/quantification-detail.component';
import { QuantificationUpdateComponent } from './update/quantification-update.component';
import QuantificationResolve from './route/quantification-routing-resolve.service';

const quantificationRoute: Routes = [
  {
    path: '',
    component: QuantificationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: QuantificationDetailComponent,
    resolve: {
      quantification: QuantificationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: QuantificationUpdateComponent,
    resolve: {
      quantification: QuantificationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: QuantificationUpdateComponent,
    resolve: {
      quantification: QuantificationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default quantificationRoute;
