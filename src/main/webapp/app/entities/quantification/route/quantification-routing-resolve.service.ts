import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IQuantification } from '../quantification.model';
import { QuantificationService } from '../service/quantification.service';

const quantificationResolve = (route: ActivatedRouteSnapshot): Observable<null | IQuantification> => {
  const id = route.params['id'];
  if (id) {
    return inject(QuantificationService)
      .find(id)
      .pipe(
        mergeMap((quantification: HttpResponse<IQuantification>) => {
          if (quantification.body) {
            return of(quantification.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default quantificationResolve;
