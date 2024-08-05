import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMaterialReceiptDetail, NewMaterialReceiptDetail } from '../material-receipt-detail.model';

export type PartialUpdateMaterialReceiptDetail = Partial<IMaterialReceiptDetail> & Pick<IMaterialReceiptDetail, 'id'>;

export type EntityResponseType = HttpResponse<IMaterialReceiptDetail>;
export type EntityArrayResponseType = HttpResponse<IMaterialReceiptDetail[]>;

@Injectable({ providedIn: 'root' })
export class MaterialReceiptDetailService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/material-receipt-details');

  create(materialReceiptDetail: NewMaterialReceiptDetail): Observable<EntityResponseType> {
    return this.http.post<IMaterialReceiptDetail>(this.resourceUrl, materialReceiptDetail, { observe: 'response' });
  }

  update(materialReceiptDetail: IMaterialReceiptDetail): Observable<EntityResponseType> {
    return this.http.put<IMaterialReceiptDetail>(
      `${this.resourceUrl}/${this.getMaterialReceiptDetailIdentifier(materialReceiptDetail)}`,
      materialReceiptDetail,
      { observe: 'response' },
    );
  }

  partialUpdate(materialReceiptDetail: PartialUpdateMaterialReceiptDetail): Observable<EntityResponseType> {
    return this.http.patch<IMaterialReceiptDetail>(
      `${this.resourceUrl}/${this.getMaterialReceiptDetailIdentifier(materialReceiptDetail)}`,
      materialReceiptDetail,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMaterialReceiptDetail>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMaterialReceiptDetail[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMaterialReceiptDetailIdentifier(materialReceiptDetail: Pick<IMaterialReceiptDetail, 'id'>): number {
    return materialReceiptDetail.id;
  }

  compareMaterialReceiptDetail(o1: Pick<IMaterialReceiptDetail, 'id'> | null, o2: Pick<IMaterialReceiptDetail, 'id'> | null): boolean {
    return o1 && o2 ? this.getMaterialReceiptDetailIdentifier(o1) === this.getMaterialReceiptDetailIdentifier(o2) : o1 === o2;
  }

  addMaterialReceiptDetailToCollectionIfMissing<Type extends Pick<IMaterialReceiptDetail, 'id'>>(
    materialReceiptDetailCollection: Type[],
    ...materialReceiptDetailsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const materialReceiptDetails: Type[] = materialReceiptDetailsToCheck.filter(isPresent);
    if (materialReceiptDetails.length > 0) {
      const materialReceiptDetailCollectionIdentifiers = materialReceiptDetailCollection.map(materialReceiptDetailItem =>
        this.getMaterialReceiptDetailIdentifier(materialReceiptDetailItem),
      );
      const materialReceiptDetailsToAdd = materialReceiptDetails.filter(materialReceiptDetailItem => {
        const materialReceiptDetailIdentifier = this.getMaterialReceiptDetailIdentifier(materialReceiptDetailItem);
        if (materialReceiptDetailCollectionIdentifiers.includes(materialReceiptDetailIdentifier)) {
          return false;
        }
        materialReceiptDetailCollectionIdentifiers.push(materialReceiptDetailIdentifier);
        return true;
      });
      return [...materialReceiptDetailsToAdd, ...materialReceiptDetailCollection];
    }
    return materialReceiptDetailCollection;
  }
}
