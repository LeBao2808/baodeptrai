import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMaterialInventory } from '../material-inventory.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../material-inventory.test-samples';

import { MaterialInventoryService } from './material-inventory.service';

const requireRestSample: IMaterialInventory = {
  ...sampleWithRequiredData,
};

describe('MaterialInventory Service', () => {
  let service: MaterialInventoryService;
  let httpMock: HttpTestingController;
  let expectedResult: IMaterialInventory | IMaterialInventory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MaterialInventoryService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a MaterialInventory', () => {
      const materialInventory = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(materialInventory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MaterialInventory', () => {
      const materialInventory = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(materialInventory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MaterialInventory', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MaterialInventory', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MaterialInventory', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMaterialInventoryToCollectionIfMissing', () => {
      it('should add a MaterialInventory to an empty array', () => {
        const materialInventory: IMaterialInventory = sampleWithRequiredData;
        expectedResult = service.addMaterialInventoryToCollectionIfMissing([], materialInventory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(materialInventory);
      });

      it('should not add a MaterialInventory to an array that contains it', () => {
        const materialInventory: IMaterialInventory = sampleWithRequiredData;
        const materialInventoryCollection: IMaterialInventory[] = [
          {
            ...materialInventory,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMaterialInventoryToCollectionIfMissing(materialInventoryCollection, materialInventory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MaterialInventory to an array that doesn't contain it", () => {
        const materialInventory: IMaterialInventory = sampleWithRequiredData;
        const materialInventoryCollection: IMaterialInventory[] = [sampleWithPartialData];
        expectedResult = service.addMaterialInventoryToCollectionIfMissing(materialInventoryCollection, materialInventory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(materialInventory);
      });

      it('should add only unique MaterialInventory to an array', () => {
        const materialInventoryArray: IMaterialInventory[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const materialInventoryCollection: IMaterialInventory[] = [sampleWithRequiredData];
        expectedResult = service.addMaterialInventoryToCollectionIfMissing(materialInventoryCollection, ...materialInventoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const materialInventory: IMaterialInventory = sampleWithRequiredData;
        const materialInventory2: IMaterialInventory = sampleWithPartialData;
        expectedResult = service.addMaterialInventoryToCollectionIfMissing([], materialInventory, materialInventory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(materialInventory);
        expect(expectedResult).toContain(materialInventory2);
      });

      it('should accept null and undefined values', () => {
        const materialInventory: IMaterialInventory = sampleWithRequiredData;
        expectedResult = service.addMaterialInventoryToCollectionIfMissing([], null, materialInventory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(materialInventory);
      });

      it('should return initial array if no MaterialInventory is added', () => {
        const materialInventoryCollection: IMaterialInventory[] = [sampleWithRequiredData];
        expectedResult = service.addMaterialInventoryToCollectionIfMissing(materialInventoryCollection, undefined, null);
        expect(expectedResult).toEqual(materialInventoryCollection);
      });
    });

    describe('compareMaterialInventory', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMaterialInventory(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMaterialInventory(entity1, entity2);
        const compareResult2 = service.compareMaterialInventory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMaterialInventory(entity1, entity2);
        const compareResult2 = service.compareMaterialInventory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMaterialInventory(entity1, entity2);
        const compareResult2 = service.compareMaterialInventory(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
