import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMaterialReceipt, NewMaterialReceipt } from '../material-receipt.model';

export type PartialUpdateMaterialReceipt = Partial<IMaterialReceipt> & Pick<IMaterialReceipt, 'id'>;

type RestOf<T extends IMaterialReceipt | NewMaterialReceipt> = Omit<T, 'creationDate' | 'paymentDate' | 'receiptDate'> & {
  creationDate?: string | null;
  paymentDate?: string | null;
  receiptDate?: string | null;
};

export type RestMaterialReceipt = RestOf<IMaterialReceipt>;

export type NewRestMaterialReceipt = RestOf<NewMaterialReceipt>;

export type PartialUpdateRestMaterialReceipt = RestOf<PartialUpdateMaterialReceipt>;

export type EntityResponseType = HttpResponse<IMaterialReceipt>;
export type EntityArrayResponseType = HttpResponse<IMaterialReceipt[]>;

@Injectable({ providedIn: 'root' })
export class MaterialReceiptService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/material-receipts');

  create(materialReceipt: NewMaterialReceipt): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(materialReceipt);
    return this.http
      .post<RestMaterialReceipt>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(materialReceipt: IMaterialReceipt): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(materialReceipt);
    return this.http
      .put<RestMaterialReceipt>(`${this.resourceUrl}/${this.getMaterialReceiptIdentifier(materialReceipt)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(materialReceipt: PartialUpdateMaterialReceipt): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(materialReceipt);
    return this.http
      .patch<RestMaterialReceipt>(`${this.resourceUrl}/${this.getMaterialReceiptIdentifier(materialReceipt)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMaterialReceipt>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMaterialReceipt[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMaterialReceiptIdentifier(materialReceipt: Pick<IMaterialReceipt, 'id'>): number {
    return materialReceipt.id;
  }

  compareMaterialReceipt(o1: Pick<IMaterialReceipt, 'id'> | null, o2: Pick<IMaterialReceipt, 'id'> | null): boolean {
    return o1 && o2 ? this.getMaterialReceiptIdentifier(o1) === this.getMaterialReceiptIdentifier(o2) : o1 === o2;
  }

  addMaterialReceiptToCollectionIfMissing<Type extends Pick<IMaterialReceipt, 'id'>>(
    materialReceiptCollection: Type[],
    ...materialReceiptsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const materialReceipts: Type[] = materialReceiptsToCheck.filter(isPresent);
    if (materialReceipts.length > 0) {
      const materialReceiptCollectionIdentifiers = materialReceiptCollection.map(materialReceiptItem =>
        this.getMaterialReceiptIdentifier(materialReceiptItem),
      );
      const materialReceiptsToAdd = materialReceipts.filter(materialReceiptItem => {
        const materialReceiptIdentifier = this.getMaterialReceiptIdentifier(materialReceiptItem);
        if (materialReceiptCollectionIdentifiers.includes(materialReceiptIdentifier)) {
          return false;
        }
        materialReceiptCollectionIdentifiers.push(materialReceiptIdentifier);
        return true;
      });
      return [...materialReceiptsToAdd, ...materialReceiptCollection];
    }
    return materialReceiptCollection;
  }

  protected convertDateFromClient<T extends IMaterialReceipt | NewMaterialReceipt | PartialUpdateMaterialReceipt>(
    materialReceipt: T,
  ): RestOf<T> {
    return {
      ...materialReceipt,
      creationDate: materialReceipt.creationDate?.toJSON() ?? null,
      paymentDate: materialReceipt.paymentDate?.toJSON() ?? null,
      receiptDate: materialReceipt.receiptDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restMaterialReceipt: RestMaterialReceipt): IMaterialReceipt {
    return {
      ...restMaterialReceipt,
      creationDate: restMaterialReceipt.creationDate ? dayjs(restMaterialReceipt.creationDate) : undefined,
      paymentDate: restMaterialReceipt.paymentDate ? dayjs(restMaterialReceipt.paymentDate) : undefined,
      receiptDate: restMaterialReceipt.receiptDate ? dayjs(restMaterialReceipt.receiptDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMaterialReceipt>): HttpResponse<IMaterialReceipt> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMaterialReceipt[]>): HttpResponse<IMaterialReceipt[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
