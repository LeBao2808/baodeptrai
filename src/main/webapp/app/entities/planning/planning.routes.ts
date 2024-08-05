import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PlanningComponent } from './list/planning.component';
import { PlanningDetailComponent } from './detail/planning-detail.component';
import { PlanningUpdateComponent } from './update/planning-update.component';
import PlanningResolve from './route/planning-routing-resolve.service';

const planningRoute: Routes = [
  {
    path: '',
    component: PlanningComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PlanningDetailComponent,
    resolve: {
      planning: PlanningResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PlanningUpdateComponent,
    resolve: {
      planning: PlanningResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PlanningUpdateComponent,
    resolve: {
      planning: PlanningResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default planningRoute;
