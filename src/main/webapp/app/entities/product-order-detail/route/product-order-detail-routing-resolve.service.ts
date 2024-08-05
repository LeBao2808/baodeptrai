import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProductOrderDetail } from '../product-order-detail.model';
import { ProductOrderDetailService } from '../service/product-order-detail.service';

const productOrderDetailResolve = (route: ActivatedRouteSnapshot): Observable<null | IProductOrderDetail> => {
  const id = route.params['id'];
  if (id) {
    return inject(ProductOrderDetailService)
      .find(id)
      .pipe(
        mergeMap((productOrderDetail: HttpResponse<IProductOrderDetail>) => {
          if (productOrderDetail.body) {
            return of(productOrderDetail.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default productOrderDetailResolve;
