import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMaterialReceiptDetail } from '../material-receipt-detail.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../material-receipt-detail.test-samples';

import { MaterialReceiptDetailService } from './material-receipt-detail.service';

const requireRestSample: IMaterialReceiptDetail = {
  ...sampleWithRequiredData,
};

describe('MaterialReceiptDetail Service', () => {
  let service: MaterialReceiptDetailService;
  let httpMock: HttpTestingController;
  let expectedResult: IMaterialReceiptDetail | IMaterialReceiptDetail[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MaterialReceiptDetailService);
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

    it('should create a MaterialReceiptDetail', () => {
      const materialReceiptDetail = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(materialReceiptDetail).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MaterialReceiptDetail', () => {
      const materialReceiptDetail = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(materialReceiptDetail).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MaterialReceiptDetail', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MaterialReceiptDetail', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MaterialReceiptDetail', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMaterialReceiptDetailToCollectionIfMissing', () => {
      it('should add a MaterialReceiptDetail to an empty array', () => {
        const materialReceiptDetail: IMaterialReceiptDetail = sampleWithRequiredData;
        expectedResult = service.addMaterialReceiptDetailToCollectionIfMissing([], materialReceiptDetail);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(materialReceiptDetail);
      });

      it('should not add a MaterialReceiptDetail to an array that contains it', () => {
        const materialReceiptDetail: IMaterialReceiptDetail = sampleWithRequiredData;
        const materialReceiptDetailCollection: IMaterialReceiptDetail[] = [
          {
            ...materialReceiptDetail,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMaterialReceiptDetailToCollectionIfMissing(materialReceiptDetailCollection, materialReceiptDetail);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MaterialReceiptDetail to an array that doesn't contain it", () => {
        const materialReceiptDetail: IMaterialReceiptDetail = sampleWithRequiredData;
        const materialReceiptDetailCollection: IMaterialReceiptDetail[] = [sampleWithPartialData];
        expectedResult = service.addMaterialReceiptDetailToCollectionIfMissing(materialReceiptDetailCollection, materialReceiptDetail);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(materialReceiptDetail);
      });

      it('should add only unique MaterialReceiptDetail to an array', () => {
        const materialReceiptDetailArray: IMaterialReceiptDetail[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const materialReceiptDetailCollection: IMaterialReceiptDetail[] = [sampleWithRequiredData];
        expectedResult = service.addMaterialReceiptDetailToCollectionIfMissing(
          materialReceiptDetailCollection,
          ...materialReceiptDetailArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const materialReceiptDetail: IMaterialReceiptDetail = sampleWithRequiredData;
        const materialReceiptDetail2: IMaterialReceiptDetail = sampleWithPartialData;
        expectedResult = service.addMaterialReceiptDetailToCollectionIfMissing([], materialReceiptDetail, materialReceiptDetail2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(materialReceiptDetail);
        expect(expectedResult).toContain(materialReceiptDetail2);
      });

      it('should accept null and undefined values', () => {
        const materialReceiptDetail: IMaterialReceiptDetail = sampleWithRequiredData;
        expectedResult = service.addMaterialReceiptDetailToCollectionIfMissing([], null, materialReceiptDetail, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(materialReceiptDetail);
      });

      it('should return initial array if no MaterialReceiptDetail is added', () => {
        const materialReceiptDetailCollection: IMaterialReceiptDetail[] = [sampleWithRequiredData];
        expectedResult = service.addMaterialReceiptDetailToCollectionIfMissing(materialReceiptDetailCollection, undefined, null);
        expect(expectedResult).toEqual(materialReceiptDetailCollection);
      });
    });

    describe('compareMaterialReceiptDetail', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMaterialReceiptDetail(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMaterialReceiptDetail(entity1, entity2);
        const compareResult2 = service.compareMaterialReceiptDetail(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMaterialReceiptDetail(entity1, entity2);
        const compareResult2 = service.compareMaterialReceiptDetail(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMaterialReceiptDetail(entity1, entity2);
        const compareResult2 = service.compareMaterialReceiptDetail(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
