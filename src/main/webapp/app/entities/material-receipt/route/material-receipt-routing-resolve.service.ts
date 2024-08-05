import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMaterialReceipt } from '../material-receipt.model';
import { MaterialReceiptService } from '../service/material-receipt.service';

const materialReceiptResolve = (route: ActivatedRouteSnapshot): Observable<null | IMaterialReceipt> => {
  const id = route.params['id'];
  if (id) {
    return inject(MaterialReceiptService)
      .find(id)
      .pipe(
        mergeMap((materialReceipt: HttpResponse<IMaterialReceipt>) => {
          if (materialReceipt.body) {
            return of(materialReceipt.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default materialReceiptResolve;
