import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProductReceipt, NewProductReceipt } from '../product-receipt.model';

export type PartialUpdateProductReceipt = Partial<IProductReceipt> & Pick<IProductReceipt, 'id'>;

type RestOf<T extends IProductReceipt | NewProductReceipt> = Omit<T, 'creationDate' | 'paymentDate' | 'receiptDate'> & {
  creationDate?: string | null;
  paymentDate?: string | null;
  receiptDate?: string | null;
};

export type RestProductReceipt = RestOf<IProductReceipt>;

export type NewRestProductReceipt = RestOf<NewProductReceipt>;

export type PartialUpdateRestProductReceipt = RestOf<PartialUpdateProductReceipt>;

export type EntityResponseType = HttpResponse<IProductReceipt>;
export type EntityArrayResponseType = HttpResponse<IProductReceipt[]>;

@Injectable({ providedIn: 'root' })
export class ProductReceiptService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/product-receipts');

  create(productReceipt: NewProductReceipt): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productReceipt);
    return this.http
      .post<RestProductReceipt>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(productReceipt: IProductReceipt): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productReceipt);
    return this.http
      .put<RestProductReceipt>(`${this.resourceUrl}/${this.getProductReceiptIdentifier(productReceipt)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(productReceipt: PartialUpdateProductReceipt): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productReceipt);
    return this.http
      .patch<RestProductReceipt>(`${this.resourceUrl}/${this.getProductReceiptIdentifier(productReceipt)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestProductReceipt>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestProductReceipt[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProductReceiptIdentifier(productReceipt: Pick<IProductReceipt, 'id'>): number {
    return productReceipt.id;
  }

  compareProductReceipt(o1: Pick<IProductReceipt, 'id'> | null, o2: Pick<IProductReceipt, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductReceiptIdentifier(o1) === this.getProductReceiptIdentifier(o2) : o1 === o2;
  }

  addProductReceiptToCollectionIfMissing<Type extends Pick<IProductReceipt, 'id'>>(
    productReceiptCollection: Type[],
    ...productReceiptsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productReceipts: Type[] = productReceiptsToCheck.filter(isPresent);
    if (productReceipts.length > 0) {
      const productReceiptCollectionIdentifiers = productReceiptCollection.map(productReceiptItem =>
        this.getProductReceiptIdentifier(productReceiptItem),
      );
      const productReceiptsToAdd = productReceipts.filter(productReceiptItem => {
        const productReceiptIdentifier = this.getProductReceiptIdentifier(productReceiptItem);
        if (productReceiptCollectionIdentifiers.includes(productReceiptIdentifier)) {
          return false;
        }
        productReceiptCollectionIdentifiers.push(productReceiptIdentifier);
        return true;
      });
      return [...productReceiptsToAdd, ...productReceiptCollection];
    }
    return productReceiptCollection;
  }

  protected convertDateFromClient<T extends IProductReceipt | NewProductReceipt | PartialUpdateProductReceipt>(
    productReceipt: T,
  ): RestOf<T> {
    return {
      ...productReceipt,
      creationDate: productReceipt.creationDate?.toJSON() ?? null,
      paymentDate: productReceipt.paymentDate?.toJSON() ?? null,
      receiptDate: productReceipt.receiptDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restProductReceipt: RestProductReceipt): IProductReceipt {
    return {
      ...restProductReceipt,
      creationDate: restProductReceipt.creationDate ? dayjs(restProductReceipt.creationDate) : undefined,
      paymentDate: restProductReceipt.paymentDate ? dayjs(restProductReceipt.paymentDate) : undefined,
      receiptDate: restProductReceipt.receiptDate ? dayjs(restProductReceipt.receiptDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestProductReceipt>): HttpResponse<IProductReceipt> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestProductReceipt[]>): HttpResponse<IProductReceipt[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
