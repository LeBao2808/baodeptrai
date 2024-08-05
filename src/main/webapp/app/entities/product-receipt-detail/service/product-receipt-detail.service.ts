import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProductReceiptDetail, NewProductReceiptDetail } from '../product-receipt-detail.model';

export type PartialUpdateProductReceiptDetail = Partial<IProductReceiptDetail> & Pick<IProductReceiptDetail, 'id'>;

export type EntityResponseType = HttpResponse<IProductReceiptDetail>;
export type EntityArrayResponseType = HttpResponse<IProductReceiptDetail[]>;

@Injectable({ providedIn: 'root' })
export class ProductReceiptDetailService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/product-receipt-details');

  create(productReceiptDetail: NewProductReceiptDetail): Observable<EntityResponseType> {
    return this.http.post<IProductReceiptDetail>(this.resourceUrl, productReceiptDetail, { observe: 'response' });
  }

  update(productReceiptDetail: IProductReceiptDetail): Observable<EntityResponseType> {
    return this.http.put<IProductReceiptDetail>(
      `${this.resourceUrl}/${this.getProductReceiptDetailIdentifier(productReceiptDetail)}`,
      productReceiptDetail,
      { observe: 'response' },
    );
  }

  partialUpdate(productReceiptDetail: PartialUpdateProductReceiptDetail): Observable<EntityResponseType> {
    return this.http.patch<IProductReceiptDetail>(
      `${this.resourceUrl}/${this.getProductReceiptDetailIdentifier(productReceiptDetail)}`,
      productReceiptDetail,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProductReceiptDetail>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProductReceiptDetail[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProductReceiptDetailIdentifier(productReceiptDetail: Pick<IProductReceiptDetail, 'id'>): number {
    return productReceiptDetail.id;
  }

  compareProductReceiptDetail(o1: Pick<IProductReceiptDetail, 'id'> | null, o2: Pick<IProductReceiptDetail, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductReceiptDetailIdentifier(o1) === this.getProductReceiptDetailIdentifier(o2) : o1 === o2;
  }

  addProductReceiptDetailToCollectionIfMissing<Type extends Pick<IProductReceiptDetail, 'id'>>(
    productReceiptDetailCollection: Type[],
    ...productReceiptDetailsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productReceiptDetails: Type[] = productReceiptDetailsToCheck.filter(isPresent);
    if (productReceiptDetails.length > 0) {
      const productReceiptDetailCollectionIdentifiers = productReceiptDetailCollection.map(productReceiptDetailItem =>
        this.getProductReceiptDetailIdentifier(productReceiptDetailItem),
      );
      const productReceiptDetailsToAdd = productReceiptDetails.filter(productReceiptDetailItem => {
        const productReceiptDetailIdentifier = this.getProductReceiptDetailIdentifier(productReceiptDetailItem);
        if (productReceiptDetailCollectionIdentifiers.includes(productReceiptDetailIdentifier)) {
          return false;
        }
        productReceiptDetailCollectionIdentifiers.push(productReceiptDetailIdentifier);
        return true;
      });
      return [...productReceiptDetailsToAdd, ...productReceiptDetailCollection];
    }
    return productReceiptDetailCollection;
  }
}
