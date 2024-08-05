import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOffer } from '../offer.model';
import { OfferService } from '../service/offer.service';

const offerResolve = (route: ActivatedRouteSnapshot): Observable<null | IOffer> => {
  const id = route.params['id'];
  if (id) {
    return inject(OfferService)
      .find(id)
      .pipe(
        mergeMap((offer: HttpResponse<IOffer>) => {
          if (offer.body) {
            return of(offer.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default offerResolve;
