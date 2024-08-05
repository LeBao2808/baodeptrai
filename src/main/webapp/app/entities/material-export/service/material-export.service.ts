import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMaterialExport, NewMaterialExport } from '../material-export.model';

export type PartialUpdateMaterialExport = Partial<IMaterialExport> & Pick<IMaterialExport, 'id'>;

type RestOf<T extends IMaterialExport | NewMaterialExport> = Omit<T, 'creationDate' | 'exportDate'> & {
  creationDate?: string | null;
  exportDate?: string | null;
};

export type RestMaterialExport = RestOf<IMaterialExport>;

export type NewRestMaterialExport = RestOf<NewMaterialExport>;

export type PartialUpdateRestMaterialExport = RestOf<PartialUpdateMaterialExport>;

export type EntityResponseType = HttpResponse<IMaterialExport>;
export type EntityArrayResponseType = HttpResponse<IMaterialExport[]>;

@Injectable({ providedIn: 'root' })
export class MaterialExportService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/material-exports');

  create(materialExport: NewMaterialExport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(materialExport);
    return this.http
      .post<RestMaterialExport>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(materialExport: IMaterialExport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(materialExport);
    return this.http
      .put<RestMaterialExport>(`${this.resourceUrl}/${this.getMaterialExportIdentifier(materialExport)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(materialExport: PartialUpdateMaterialExport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(materialExport);
    return this.http
      .patch<RestMaterialExport>(`${this.resourceUrl}/${this.getMaterialExportIdentifier(materialExport)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMaterialExport>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMaterialExport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMaterialExportIdentifier(materialExport: Pick<IMaterialExport, 'id'>): number {
    return materialExport.id;
  }

  compareMaterialExport(o1: Pick<IMaterialExport, 'id'> | null, o2: Pick<IMaterialExport, 'id'> | null): boolean {
    return o1 && o2 ? this.getMaterialExportIdentifier(o1) === this.getMaterialExportIdentifier(o2) : o1 === o2;
  }

  addMaterialExportToCollectionIfMissing<Type extends Pick<IMaterialExport, 'id'>>(
    materialExportCollection: Type[],
    ...materialExportsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const materialExports: Type[] = materialExportsToCheck.filter(isPresent);
    if (materialExports.length > 0) {
      const materialExportCollectionIdentifiers = materialExportCollection.map(materialExportItem =>
        this.getMaterialExportIdentifier(materialExportItem),
      );
      const materialExportsToAdd = materialExports.filter(materialExportItem => {
        const materialExportIdentifier = this.getMaterialExportIdentifier(materialExportItem);
        if (materialExportCollectionIdentifiers.includes(materialExportIdentifier)) {
          return false;
        }
        materialExportCollectionIdentifiers.push(materialExportIdentifier);
        return true;
      });
      return [...materialExportsToAdd, ...materialExportCollection];
    }
    return materialExportCollection;
  }

  protected convertDateFromClient<T extends IMaterialExport | NewMaterialExport | PartialUpdateMaterialExport>(
    materialExport: T,
  ): RestOf<T> {
    return {
      ...materialExport,
      creationDate: materialExport.creationDate?.toJSON() ?? null,
      exportDate: materialExport.exportDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restMaterialExport: RestMaterialExport): IMaterialExport {
    return {
      ...restMaterialExport,
      creationDate: restMaterialExport.creationDate ? dayjs(restMaterialExport.creationDate) : undefined,
      exportDate: restMaterialExport.exportDate ? dayjs(restMaterialExport.exportDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMaterialExport>): HttpResponse<IMaterialExport> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMaterialExport[]>): HttpResponse<IMaterialExport[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
