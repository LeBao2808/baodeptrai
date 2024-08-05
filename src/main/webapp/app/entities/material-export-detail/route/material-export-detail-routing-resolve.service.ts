import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMaterialExportDetail } from '../material-export-detail.model';
import { MaterialExportDetailService } from '../service/material-export-detail.service';

const materialExportDetailResolve = (route: ActivatedRouteSnapshot): Observable<null | IMaterialExportDetail> => {
  const id = route.params['id'];
  if (id) {
    return inject(MaterialExportDetailService)
      .find(id)
      .pipe(
        mergeMap((materialExportDetail: HttpResponse<IMaterialExportDetail>) => {
          if (materialExportDetail.body) {
            return of(materialExportDetail.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default materialExportDetailResolve;
