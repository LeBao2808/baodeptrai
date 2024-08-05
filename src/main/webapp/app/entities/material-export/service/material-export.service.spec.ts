import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMaterialExport } from '../material-export.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../material-export.test-samples';

import { MaterialExportService, RestMaterialExport } from './material-export.service';

const requireRestSample: RestMaterialExport = {
  ...sampleWithRequiredData,
  creationDate: sampleWithRequiredData.creationDate?.toJSON(),
  exportDate: sampleWithRequiredData.exportDate?.toJSON(),
};

describe('MaterialExport Service', () => {
  let service: MaterialExportService;
  let httpMock: HttpTestingController;
  let expectedResult: IMaterialExport | IMaterialExport[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MaterialExportService);
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

    it('should create a MaterialExport', () => {
      const materialExport = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(materialExport).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MaterialExport', () => {
      const materialExport = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(materialExport).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MaterialExport', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MaterialExport', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MaterialExport', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMaterialExportToCollectionIfMissing', () => {
      it('should add a MaterialExport to an empty array', () => {
        const materialExport: IMaterialExport = sampleWithRequiredData;
        expectedResult = service.addMaterialExportToCollectionIfMissing([], materialExport);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(materialExport);
      });

      it('should not add a MaterialExport to an array that contains it', () => {
        const materialExport: IMaterialExport = sampleWithRequiredData;
        const materialExportCollection: IMaterialExport[] = [
          {
            ...materialExport,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMaterialExportToCollectionIfMissing(materialExportCollection, materialExport);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MaterialExport to an array that doesn't contain it", () => {
        const materialExport: IMaterialExport = sampleWithRequiredData;
        const materialExportCollection: IMaterialExport[] = [sampleWithPartialData];
        expectedResult = service.addMaterialExportToCollectionIfMissing(materialExportCollection, materialExport);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(materialExport);
      });

      it('should add only unique MaterialExport to an array', () => {
        const materialExportArray: IMaterialExport[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const materialExportCollection: IMaterialExport[] = [sampleWithRequiredData];
        expectedResult = service.addMaterialExportToCollectionIfMissing(materialExportCollection, ...materialExportArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const materialExport: IMaterialExport = sampleWithRequiredData;
        const materialExport2: IMaterialExport = sampleWithPartialData;
        expectedResult = service.addMaterialExportToCollectionIfMissing([], materialExport, materialExport2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(materialExport);
        expect(expectedResult).toContain(materialExport2);
      });

      it('should accept null and undefined values', () => {
        const materialExport: IMaterialExport = sampleWithRequiredData;
        expectedResult = service.addMaterialExportToCollectionIfMissing([], null, materialExport, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(materialExport);
      });

      it('should return initial array if no MaterialExport is added', () => {
        const materialExportCollection: IMaterialExport[] = [sampleWithRequiredData];
        expectedResult = service.addMaterialExportToCollectionIfMissing(materialExportCollection, undefined, null);
        expect(expectedResult).toEqual(materialExportCollection);
      });
    });

    describe('compareMaterialExport', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMaterialExport(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMaterialExport(entity1, entity2);
        const compareResult2 = service.compareMaterialExport(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMaterialExport(entity1, entity2);
        const compareResult2 = service.compareMaterialExport(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMaterialExport(entity1, entity2);
        const compareResult2 = service.compareMaterialExport(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
