import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IQuantification, NewQuantification } from '../quantification.model';

export type PartialUpdateQuantification = Partial<IQuantification> & Pick<IQuantification, 'id'>;

export type EntityResponseType = HttpResponse<IQuantification>;
export type EntityArrayResponseType = HttpResponse<IQuantification[]>;

@Injectable({ providedIn: 'root' })
export class QuantificationService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/quantifications');

  create(quantification: NewQuantification): Observable<EntityResponseType> {
    return this.http.post<IQuantification>(this.resourceUrl, quantification, { observe: 'response' });
  }

  update(quantification: IQuantification): Observable<EntityResponseType> {
    return this.http.put<IQuantification>(`${this.resourceUrl}/${this.getQuantificationIdentifier(quantification)}`, quantification, {
      observe: 'response',
    });
  }

  partialUpdate(quantification: PartialUpdateQuantification): Observable<EntityResponseType> {
    return this.http.patch<IQuantification>(`${this.resourceUrl}/${this.getQuantificationIdentifier(quantification)}`, quantification, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IQuantification>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IQuantification[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getQuantificationIdentifier(quantification: Pick<IQuantification, 'id'>): number {
    return quantification.id;
  }

  compareQuantification(o1: Pick<IQuantification, 'id'> | null, o2: Pick<IQuantification, 'id'> | null): boolean {
    return o1 && o2 ? this.getQuantificationIdentifier(o1) === this.getQuantificationIdentifier(o2) : o1 === o2;
  }

  addQuantificationToCollectionIfMissing<Type extends Pick<IQuantification, 'id'>>(
    quantificationCollection: Type[],
    ...quantificationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const quantifications: Type[] = quantificationsToCheck.filter(isPresent);
    if (quantifications.length > 0) {
      const quantificationCollectionIdentifiers = quantificationCollection.map(quantificationItem =>
        this.getQuantificationIdentifier(quantificationItem),
      );
      const quantificationsToAdd = quantifications.filter(quantificationItem => {
        const quantificationIdentifier = this.getQuantificationIdentifier(quantificationItem);
        if (quantificationCollectionIdentifiers.includes(quantificationIdentifier)) {
          return false;
        }
        quantificationCollectionIdentifiers.push(quantificationIdentifier);
        return true;
      });
      return [...quantificationsToAdd, ...quantificationCollection];
    }
    return quantificationCollection;
  }
}
