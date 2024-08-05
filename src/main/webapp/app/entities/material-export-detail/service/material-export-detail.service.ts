import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMaterialExportDetail, NewMaterialExportDetail } from '../material-export-detail.model';

export type PartialUpdateMaterialExportDetail = Partial<IMaterialExportDetail> & Pick<IMaterialExportDetail, 'id'>;

export type EntityResponseType = HttpResponse<IMaterialExportDetail>;
export type EntityArrayResponseType = HttpResponse<IMaterialExportDetail[]>;

@Injectable({ providedIn: 'root' })
export class MaterialExportDetailService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/material-export-details');

  create(materialExportDetail: NewMaterialExportDetail): Observable<EntityResponseType> {
    return this.http.post<IMaterialExportDetail>(this.resourceUrl, materialExportDetail, { observe: 'response' });
  }

  update(materialExportDetail: IMaterialExportDetail): Observable<EntityResponseType> {
    return this.http.put<IMaterialExportDetail>(
      `${this.resourceUrl}/${this.getMaterialExportDetailIdentifier(materialExportDetail)}`,
      materialExportDetail,
      { observe: 'response' },
    );
  }

  partialUpdate(materialExportDetail: PartialUpdateMaterialExportDetail): Observable<EntityResponseType> {
    return this.http.patch<IMaterialExportDetail>(
      `${this.resourceUrl}/${this.getMaterialExportDetailIdentifier(materialExportDetail)}`,
      materialExportDetail,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMaterialExportDetail>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMaterialExportDetail[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMaterialExportDetailIdentifier(materialExportDetail: Pick<IMaterialExportDetail, 'id'>): number {
    return materialExportDetail.id;
  }

  compareMaterialExportDetail(o1: Pick<IMaterialExportDetail, 'id'> | null, o2: Pick<IMaterialExportDetail, 'id'> | null): boolean {
    return o1 && o2 ? this.getMaterialExportDetailIdentifier(o1) === this.getMaterialExportDetailIdentifier(o2) : o1 === o2;
  }

  addMaterialExportDetailToCollectionIfMissing<Type extends Pick<IMaterialExportDetail, 'id'>>(
    materialExportDetailCollection: Type[],
    ...materialExportDetailsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const materialExportDetails: Type[] = materialExportDetailsToCheck.filter(isPresent);
    if (materialExportDetails.length > 0) {
      const materialExportDetailCollectionIdentifiers = materialExportDetailCollection.map(materialExportDetailItem =>
        this.getMaterialExportDetailIdentifier(materialExportDetailItem),
      );
      const materialExportDetailsToAdd = materialExportDetails.filter(materialExportDetailItem => {
        const materialExportDetailIdentifier = this.getMaterialExportDetailIdentifier(materialExportDetailItem);
        if (materialExportDetailCollectionIdentifiers.includes(materialExportDetailIdentifier)) {
          return false;
        }
        materialExportDetailCollectionIdentifiers.push(materialExportDetailIdentifier);
        return true;
      });
      return [...materialExportDetailsToAdd, ...materialExportDetailCollection];
    }
    return materialExportDetailCollection;
  }
}
