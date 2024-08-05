import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { OfferComponent } from './list/offer.component';
import { OfferDetailComponent } from './detail/offer-detail.component';
import { OfferUpdateComponent } from './update/offer-update.component';
import OfferResolve from './route/offer-routing-resolve.service';

const offerRoute: Routes = [
  {
    path: '',
    component: OfferComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OfferDetailComponent,
    resolve: {
      offer: OfferResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OfferUpdateComponent,
    resolve: {
      offer: OfferResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OfferUpdateComponent,
    resolve: {
      offer: OfferResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default offerRoute;
