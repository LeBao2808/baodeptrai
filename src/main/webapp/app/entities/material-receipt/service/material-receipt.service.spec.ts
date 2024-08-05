import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMaterialReceipt } from '../material-receipt.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../material-receipt.test-samples';

import { MaterialReceiptService, RestMaterialReceipt } from './material-receipt.service';

const requireRestSample: RestMaterialReceipt = {
  ...sampleWithRequiredData,
  creationDate: sampleWithRequiredData.creationDate?.toJSON(),
  paymentDate: sampleWithRequiredData.paymentDate?.toJSON(),
  receiptDate: sampleWithRequiredData.receiptDate?.toJSON(),
};

describe('MaterialReceipt Service', () => {
  let service: MaterialReceiptService;
  let httpMock: HttpTestingController;
  let expectedResult: IMaterialReceipt | IMaterialReceipt[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MaterialReceiptService);
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

    it('should create a MaterialReceipt', () => {
      const materialReceipt = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(materialReceipt).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MaterialReceipt', () => {
      const materialReceipt = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(materialReceipt).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MaterialReceipt', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MaterialReceipt', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MaterialReceipt', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMaterialReceiptToCollectionIfMissing', () => {
      it('should add a MaterialReceipt to an empty array', () => {
        const materialReceipt: IMaterialReceipt = sampleWithRequiredData;
        expectedResult = service.addMaterialReceiptToCollectionIfMissing([], materialReceipt);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(materialReceipt);
      });

      it('should not add a MaterialReceipt to an array that contains it', () => {
        const materialReceipt: IMaterialReceipt = sampleWithRequiredData;
        const materialReceiptCollection: IMaterialReceipt[] = [
          {
            ...materialReceipt,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMaterialReceiptToCollectionIfMissing(materialReceiptCollection, materialReceipt);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MaterialReceipt to an array that doesn't contain it", () => {
        const materialReceipt: IMaterialReceipt = sampleWithRequiredData;
        const materialReceiptCollection: IMaterialReceipt[] = [sampleWithPartialData];
        expectedResult = service.addMaterialReceiptToCollectionIfMissing(materialReceiptCollection, materialReceipt);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(materialReceipt);
      });

      it('should add only unique MaterialReceipt to an array', () => {
        const materialReceiptArray: IMaterialReceipt[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const materialReceiptCollection: IMaterialReceipt[] = [sampleWithRequiredData];
        expectedResult = service.addMaterialReceiptToCollectionIfMissing(materialReceiptCollection, ...materialReceiptArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const materialReceipt: IMaterialReceipt = sampleWithRequiredData;
        const materialReceipt2: IMaterialReceipt = sampleWithPartialData;
        expectedResult = service.addMaterialReceiptToCollectionIfMissing([], materialReceipt, materialReceipt2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(materialReceipt);
        expect(expectedResult).toContain(materialReceipt2);
      });

      it('should accept null and undefined values', () => {
        const materialReceipt: IMaterialReceipt = sampleWithRequiredData;
        expectedResult = service.addMaterialReceiptToCollectionIfMissing([], null, materialReceipt, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(materialReceipt);
      });

      it('should return initial array if no MaterialReceipt is added', () => {
        const materialReceiptCollection: IMaterialReceipt[] = [sampleWithRequiredData];
        expectedResult = service.addMaterialReceiptToCollectionIfMissing(materialReceiptCollection, undefined, null);
        expect(expectedResult).toEqual(materialReceiptCollection);
      });
    });

    describe('compareMaterialReceipt', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMaterialReceipt(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMaterialReceipt(entity1, entity2);
        const compareResult2 = service.compareMaterialReceipt(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMaterialReceipt(entity1, entity2);
        const compareResult2 = service.compareMaterialReceipt(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMaterialReceipt(entity1, entity2);
        const compareResult2 = service.compareMaterialReceipt(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
