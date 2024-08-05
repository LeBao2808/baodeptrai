import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOfferDetail } from '../offer-detail.model';
import { OfferDetailService } from '../service/offer-detail.service';

const offerDetailResolve = (route: ActivatedRouteSnapshot): Observable<null | IOfferDetail> => {
  const id = route.params['id'];
  if (id) {
    return inject(OfferDetailService)
      .find(id)
      .pipe(
        mergeMap((offerDetail: HttpResponse<IOfferDetail>) => {
          if (offerDetail.body) {
            return of(offerDetail.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default offerDetailResolve;
