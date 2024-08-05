import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProductInventory } from '../product-inventory.model';
import { ProductInventoryService } from '../service/product-inventory.service';

const productInventoryResolve = (route: ActivatedRouteSnapshot): Observable<null | IProductInventory> => {
  const id = route.params['id'];
  if (id) {
    return inject(ProductInventoryService)
      .find(id)
      .pipe(
        mergeMap((productInventory: HttpResponse<IProductInventory>) => {
          if (productInventory.body) {
            return of(productInventory.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default productInventoryResolve;
