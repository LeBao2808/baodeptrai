import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProductOrderDetail, NewProductOrderDetail } from '../product-order-detail.model';

export type PartialUpdateProductOrderDetail = Partial<IProductOrderDetail> & Pick<IProductOrderDetail, 'id'>;

type RestOf<T extends IProductOrderDetail | NewProductOrderDetail> = Omit<T, 'orderCreationDate'> & {
  orderCreationDate?: string | null;
};

export type RestProductOrderDetail = RestOf<IProductOrderDetail>;

export type NewRestProductOrderDetail = RestOf<NewProductOrderDetail>;

export type PartialUpdateRestProductOrderDetail = RestOf<PartialUpdateProductOrderDetail>;

export type EntityResponseType = HttpResponse<IProductOrderDetail>;
export type EntityArrayResponseType = HttpResponse<IProductOrderDetail[]>;

@Injectable({ providedIn: 'root' })
export class ProductOrderDetailService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/product-order-details');

  create(productOrderDetail: NewProductOrderDetail): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productOrderDetail);
    return this.http
      .post<RestProductOrderDetail>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(productOrderDetail: IProductOrderDetail): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productOrderDetail);
    return this.http
      .put<RestProductOrderDetail>(`${this.resourceUrl}/${this.getProductOrderDetailIdentifier(productOrderDetail)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(productOrderDetail: PartialUpdateProductOrderDetail): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productOrderDetail);
    return this.http
      .patch<RestProductOrderDetail>(`${this.resourceUrl}/${this.getProductOrderDetailIdentifier(productOrderDetail)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestProductOrderDetail>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestProductOrderDetail[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProductOrderDetailIdentifier(productOrderDetail: Pick<IProductOrderDetail, 'id'>): number {
    return productOrderDetail.id;
  }

  compareProductOrderDetail(o1: Pick<IProductOrderDetail, 'id'> | null, o2: Pick<IProductOrderDetail, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductOrderDetailIdentifier(o1) === this.getProductOrderDetailIdentifier(o2) : o1 === o2;
  }

  addProductOrderDetailToCollectionIfMissing<Type extends Pick<IProductOrderDetail, 'id'>>(
    productOrderDetailCollection: Type[],
    ...productOrderDetailsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productOrderDetails: Type[] = productOrderDetailsToCheck.filter(isPresent);
    if (productOrderDetails.length > 0) {
      const productOrderDetailCollectionIdentifiers = productOrderDetailCollection.map(productOrderDetailItem =>
        this.getProductOrderDetailIdentifier(productOrderDetailItem),
      );
      const productOrderDetailsToAdd = productOrderDetails.filter(productOrderDetailItem => {
        const productOrderDetailIdentifier = this.getProductOrderDetailIdentifier(productOrderDetailItem);
        if (productOrderDetailCollectionIdentifiers.includes(productOrderDetailIdentifier)) {
          return false;
        }
        productOrderDetailCollectionIdentifiers.push(productOrderDetailIdentifier);
        return true;
      });
      return [...productOrderDetailsToAdd, ...productOrderDetailCollection];
    }
    return productOrderDetailCollection;
  }

  protected convertDateFromClient<T extends IProductOrderDetail | NewProductOrderDetail | PartialUpdateProductOrderDetail>(
    productOrderDetail: T,
  ): RestOf<T> {
    return {
      ...productOrderDetail,
      orderCreationDate: productOrderDetail.orderCreationDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restProductOrderDetail: RestProductOrderDetail): IProductOrderDetail {
    return {
      ...restProductOrderDetail,
      orderCreationDate: restProductOrderDetail.orderCreationDate ? dayjs(restProductOrderDetail.orderCreationDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestProductOrderDetail>): HttpResponse<IProductOrderDetail> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestProductOrderDetail[]>): HttpResponse<IProductOrderDetail[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
