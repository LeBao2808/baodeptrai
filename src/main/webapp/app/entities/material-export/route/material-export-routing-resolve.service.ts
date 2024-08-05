import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMaterialExport } from '../material-export.model';
import { MaterialExportService } from '../service/material-export.service';

const materialExportResolve = (route: ActivatedRouteSnapshot): Observable<null | IMaterialExport> => {
  const id = route.params['id'];
  if (id) {
    return inject(MaterialExportService)
      .find(id)
      .pipe(
        mergeMap((materialExport: HttpResponse<IMaterialExport>) => {
          if (materialExport.body) {
            return of(materialExport.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default materialExportResolve;
