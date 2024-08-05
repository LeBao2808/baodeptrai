import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProductReceipt } from '../product-receipt.model';
import { ProductReceiptService } from '../service/product-receipt.service';

const productReceiptResolve = (route: ActivatedRouteSnapshot): Observable<null | IProductReceipt> => {
  const id = route.params['id'];
  if (id) {
    return inject(ProductReceiptService)
      .find(id)
      .pipe(
        mergeMap((productReceipt: HttpResponse<IProductReceipt>) => {
          if (productReceipt.body) {
            return of(productReceipt.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default productReceiptResolve;
