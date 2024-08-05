import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProductReceiptDetail } from '../product-receipt-detail.model';
import { ProductReceiptDetailService } from '../service/product-receipt-detail.service';

const productReceiptDetailResolve = (route: ActivatedRouteSnapshot): Observable<null | IProductReceiptDetail> => {
  const id = route.params['id'];
  if (id) {
    return inject(ProductReceiptDetailService)
      .find(id)
      .pipe(
        mergeMap((productReceiptDetail: HttpResponse<IProductReceiptDetail>) => {
          if (productReceiptDetail.body) {
            return of(productReceiptDetail.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default productReceiptDetailResolve;
