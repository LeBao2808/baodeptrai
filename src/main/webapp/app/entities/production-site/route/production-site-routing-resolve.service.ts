import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProductionSite } from '../production-site.model';
import { ProductionSiteService } from '../service/production-site.service';

const productionSiteResolve = (route: ActivatedRouteSnapshot): Observable<null | IProductionSite> => {
  const id = route.params['id'];
  if (id) {
    return inject(ProductionSiteService)
      .find(id)
      .pipe(
        mergeMap((productionSite: HttpResponse<IProductionSite>) => {
          if (productionSite.body) {
            return of(productionSite.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default productionSiteResolve;
