import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMaterialReceiptDetail } from '../material-receipt-detail.model';
import { MaterialReceiptDetailService } from '../service/material-receipt-detail.service';

const materialReceiptDetailResolve = (route: ActivatedRouteSnapshot): Observable<null | IMaterialReceiptDetail> => {
  const id = route.params['id'];
  if (id) {
    return inject(MaterialReceiptDetailService)
      .find(id)
      .pipe(
        mergeMap((materialReceiptDetail: HttpResponse<IMaterialReceiptDetail>) => {
          if (materialReceiptDetail.body) {
            return of(materialReceiptDetail.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default materialReceiptDetailResolve;
