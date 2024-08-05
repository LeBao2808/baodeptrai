import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMaterialInventory } from '../material-inventory.model';
import { MaterialInventoryService } from '../service/material-inventory.service';

const materialInventoryResolve = (route: ActivatedRouteSnapshot): Observable<null | IMaterialInventory> => {
  const id = route.params['id'];
  if (id) {
    return inject(MaterialInventoryService)
      .find(id)
      .pipe(
        mergeMap((materialInventory: HttpResponse<IMaterialInventory>) => {
          if (materialInventory.body) {
            return of(materialInventory.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default materialInventoryResolve;
