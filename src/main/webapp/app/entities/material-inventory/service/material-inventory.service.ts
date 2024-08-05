import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMaterialInventory, NewMaterialInventory } from '../material-inventory.model';

export type PartialUpdateMaterialInventory = Partial<IMaterialInventory> & Pick<IMaterialInventory, 'id'>;

export type EntityResponseType = HttpResponse<IMaterialInventory>;
export type EntityArrayResponseType = HttpResponse<IMaterialInventory[]>;

@Injectable({ providedIn: 'root' })
export class MaterialInventoryService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/material-inventories');

  create(materialInventory: NewMaterialInventory): Observable<EntityResponseType> {
    return this.http.post<IMaterialInventory>(this.resourceUrl, materialInventory, { observe: 'response' });
  }

  update(materialInventory: IMaterialInventory): Observable<EntityResponseType> {
    return this.http.put<IMaterialInventory>(
      `${this.resourceUrl}/${this.getMaterialInventoryIdentifier(materialInventory)}`,
      materialInventory,
      { observe: 'response' },
    );
  }

  partialUpdate(materialInventory: PartialUpdateMaterialInventory): Observable<EntityResponseType> {
    return this.http.patch<IMaterialInventory>(
      `${this.resourceUrl}/${this.getMaterialInventoryIdentifier(materialInventory)}`,
      materialInventory,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMaterialInventory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMaterialInventory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMaterialInventoryIdentifier(materialInventory: Pick<IMaterialInventory, 'id'>): number {
    return materialInventory.id;
  }

  compareMaterialInventory(o1: Pick<IMaterialInventory, 'id'> | null, o2: Pick<IMaterialInventory, 'id'> | null): boolean {
    return o1 && o2 ? this.getMaterialInventoryIdentifier(o1) === this.getMaterialInventoryIdentifier(o2) : o1 === o2;
  }

  addMaterialInventoryToCollectionIfMissing<Type extends Pick<IMaterialInventory, 'id'>>(
    materialInventoryCollection: Type[],
    ...materialInventoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const materialInventories: Type[] = materialInventoriesToCheck.filter(isPresent);
    if (materialInventories.length > 0) {
      const materialInventoryCollectionIdentifiers = materialInventoryCollection.map(materialInventoryItem =>
        this.getMaterialInventoryIdentifier(materialInventoryItem),
      );
      const materialInventoriesToAdd = materialInventories.filter(materialInventoryItem => {
        const materialInventoryIdentifier = this.getMaterialInventoryIdentifier(materialInventoryItem);
        if (materialInventoryCollectionIdentifiers.includes(materialInventoryIdentifier)) {
          return false;
        }
        materialInventoryCollectionIdentifiers.push(materialInventoryIdentifier);
        return true;
      });
      return [...materialInventoriesToAdd, ...materialInventoryCollection];
    }
    return materialInventoryCollection;
  }
}
