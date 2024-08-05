import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOfferDetail, NewOfferDetail } from '../offer-detail.model';

export type PartialUpdateOfferDetail = Partial<IOfferDetail> & Pick<IOfferDetail, 'id'>;

export type EntityResponseType = HttpResponse<IOfferDetail>;
export type EntityArrayResponseType = HttpResponse<IOfferDetail[]>;

@Injectable({ providedIn: 'root' })
export class OfferDetailService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/offer-details');

  create(offerDetail: NewOfferDetail): Observable<EntityResponseType> {
    return this.http.post<IOfferDetail>(this.resourceUrl, offerDetail, { observe: 'response' });
  }

  update(offerDetail: IOfferDetail): Observable<EntityResponseType> {
    return this.http.put<IOfferDetail>(`${this.resourceUrl}/${this.getOfferDetailIdentifier(offerDetail)}`, offerDetail, {
      observe: 'response',
    });
  }

  partialUpdate(offerDetail: PartialUpdateOfferDetail): Observable<EntityResponseType> {
    return this.http.patch<IOfferDetail>(`${this.resourceUrl}/${this.getOfferDetailIdentifier(offerDetail)}`, offerDetail, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOfferDetail>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOfferDetail[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOfferDetailIdentifier(offerDetail: Pick<IOfferDetail, 'id'>): number {
    return offerDetail.id;
  }

  compareOfferDetail(o1: Pick<IOfferDetail, 'id'> | null, o2: Pick<IOfferDetail, 'id'> | null): boolean {
    return o1 && o2 ? this.getOfferDetailIdentifier(o1) === this.getOfferDetailIdentifier(o2) : o1 === o2;
  }

  addOfferDetailToCollectionIfMissing<Type extends Pick<IOfferDetail, 'id'>>(
    offerDetailCollection: Type[],
    ...offerDetailsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const offerDetails: Type[] = offerDetailsToCheck.filter(isPresent);
    if (offerDetails.length > 0) {
      const offerDetailCollectionIdentifiers = offerDetailCollection.map(offerDetailItem => this.getOfferDetailIdentifier(offerDetailItem));
      const offerDetailsToAdd = offerDetails.filter(offerDetailItem => {
        const offerDetailIdentifier = this.getOfferDetailIdentifier(offerDetailItem);
        if (offerDetailCollectionIdentifiers.includes(offerDetailIdentifier)) {
          return false;
        }
        offerDetailCollectionIdentifiers.push(offerDetailIdentifier);
        return true;
      });
      return [...offerDetailsToAdd, ...offerDetailCollection];
    }
    return offerDetailCollection;
  }
}
