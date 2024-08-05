import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { OfferDetailComponent } from './list/offer-detail.component';
import { OfferDetailDetailComponent } from './detail/offer-detail-detail.component';
import { OfferDetailUpdateComponent } from './update/offer-detail-update.component';
import OfferDetailResolve from './route/offer-detail-routing-resolve.service';

const offerDetailRoute: Routes = [
  {
    path: '',
    component: OfferDetailComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OfferDetailDetailComponent,
    resolve: {
      offerDetail: OfferDetailResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OfferDetailUpdateComponent,
    resolve: {
      offerDetail: OfferDetailResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OfferDetailUpdateComponent,
    resolve: {
      offerDetail: OfferDetailResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default offerDetailRoute;
