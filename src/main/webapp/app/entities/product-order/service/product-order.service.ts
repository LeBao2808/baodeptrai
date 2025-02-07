import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProductOrder, NewProductOrder } from '../product-order.model';

export type PartialUpdateProductOrder = Partial<IProductOrder> & Pick<IProductOrder, 'id'>;

type RestOf<T extends IProductOrder | NewProductOrder> = Omit<T, 'orderDate' | 'paymentDate' | 'warehouseReleaseDate'> & {
  orderDate?: string | null;
  paymentDate?: string | null;
  warehouseReleaseDate?: string | null;
};

export type RestProductOrder = RestOf<IProductOrder>;

export type NewRestProductOrder = RestOf<NewProductOrder>;

export type PartialUpdateRestProductOrder = RestOf<PartialUpdateProductOrder>;

export type EntityResponseType = HttpResponse<IProductOrder>;
export type EntityArrayResponseType = HttpResponse<IProductOrder[]>;

@Injectable({ providedIn: 'root' })
export class ProductOrderService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/product-orders');

  create(productOrder: NewProductOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productOrder);
    return this.http
      .post<RestProductOrder>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(productOrder: IProductOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productOrder);
    return this.http
      .put<RestProductOrder>(`${this.resourceUrl}/${this.getProductOrderIdentifier(productOrder)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(productOrder: PartialUpdateProductOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productOrder);
    return this.http
      .patch<RestProductOrder>(`${this.resourceUrl}/${this.getProductOrderIdentifier(productOrder)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestProductOrder>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestProductOrder[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProductOrderIdentifier(productOrder: Pick<IProductOrder, 'id'>): number {
    return productOrder.id;
  }

  compareProductOrder(o1: Pick<IProductOrder, 'id'> | null, o2: Pick<IProductOrder, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductOrderIdentifier(o1) === this.getProductOrderIdentifier(o2) : o1 === o2;
  }

  addProductOrderToCollectionIfMissing<Type extends Pick<IProductOrder, 'id'>>(
    productOrderCollection: Type[],
    ...productOrdersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productOrders: Type[] = productOrdersToCheck.filter(isPresent);
    if (productOrders.length > 0) {
      const productOrderCollectionIdentifiers = productOrderCollection.map(productOrderItem =>
        this.getProductOrderIdentifier(productOrderItem),
      );
      const productOrdersToAdd = productOrders.filter(productOrderItem => {
        const productOrderIdentifier = this.getProductOrderIdentifier(productOrderItem);
        if (productOrderCollectionIdentifiers.includes(productOrderIdentifier)) {
          return false;
        }
        productOrderCollectionIdentifiers.push(productOrderIdentifier);
        return true;
      });
      return [...productOrdersToAdd, ...productOrderCollection];
    }
    return productOrderCollection;
  }

  protected convertDateFromClient<T extends IProductOrder | NewProductOrder | PartialUpdateProductOrder>(productOrder: T): RestOf<T> {
    return {
      ...productOrder,
      orderDate: productOrder.orderDate?.toJSON() ?? null,
      paymentDate: productOrder.paymentDate?.toJSON() ?? null,
      warehouseReleaseDate: productOrder.warehouseReleaseDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restProductOrder: RestProductOrder): IProductOrder {
    return {
      ...restProductOrder,
      orderDate: restProductOrder.orderDate ? dayjs(restProductOrder.orderDate) : undefined,
      paymentDate: restProductOrder.paymentDate ? dayjs(restProductOrder.paymentDate) : undefined,
      warehouseReleaseDate: restProductOrder.warehouseReleaseDate ? dayjs(restProductOrder.warehouseReleaseDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestProductOrder>): HttpResponse<IProductOrder> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestProductOrder[]>): HttpResponse<IProductOrder[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
