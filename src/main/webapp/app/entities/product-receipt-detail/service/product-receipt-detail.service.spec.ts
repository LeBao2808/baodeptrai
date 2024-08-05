import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IProductReceiptDetail } from '../product-receipt-detail.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../product-receipt-detail.test-samples';

import { ProductReceiptDetailService } from './product-receipt-detail.service';

const requireRestSample: IProductReceiptDetail = {
  ...sampleWithRequiredData,
};

describe('ProductReceiptDetail Service', () => {
  let service: ProductReceiptDetailService;
  let httpMock: HttpTestingController;
  let expectedResult: IProductReceiptDetail | IProductReceiptDetail[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ProductReceiptDetailService);
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

    it('should create a ProductReceiptDetail', () => {
      const productReceiptDetail = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(productReceiptDetail).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductReceiptDetail', () => {
      const productReceiptDetail = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(productReceiptDetail).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductReceiptDetail', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductReceiptDetail', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProductReceiptDetail', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addProductReceiptDetailToCollectionIfMissing', () => {
      it('should add a ProductReceiptDetail to an empty array', () => {
        const productReceiptDetail: IProductReceiptDetail = sampleWithRequiredData;
        expectedResult = service.addProductReceiptDetailToCollectionIfMissing([], productReceiptDetail);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productReceiptDetail);
      });

      it('should not add a ProductReceiptDetail to an array that contains it', () => {
        const productReceiptDetail: IProductReceiptDetail = sampleWithRequiredData;
        const productReceiptDetailCollection: IProductReceiptDetail[] = [
          {
            ...productReceiptDetail,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProductReceiptDetailToCollectionIfMissing(productReceiptDetailCollection, productReceiptDetail);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductReceiptDetail to an array that doesn't contain it", () => {
        const productReceiptDetail: IProductReceiptDetail = sampleWithRequiredData;
        const productReceiptDetailCollection: IProductReceiptDetail[] = [sampleWithPartialData];
        expectedResult = service.addProductReceiptDetailToCollectionIfMissing(productReceiptDetailCollection, productReceiptDetail);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productReceiptDetail);
      });

      it('should add only unique ProductReceiptDetail to an array', () => {
        const productReceiptDetailArray: IProductReceiptDetail[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const productReceiptDetailCollection: IProductReceiptDetail[] = [sampleWithRequiredData];
        expectedResult = service.addProductReceiptDetailToCollectionIfMissing(productReceiptDetailCollection, ...productReceiptDetailArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productReceiptDetail: IProductReceiptDetail = sampleWithRequiredData;
        const productReceiptDetail2: IProductReceiptDetail = sampleWithPartialData;
        expectedResult = service.addProductReceiptDetailToCollectionIfMissing([], productReceiptDetail, productReceiptDetail2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productReceiptDetail);
        expect(expectedResult).toContain(productReceiptDetail2);
      });

      it('should accept null and undefined values', () => {
        const productReceiptDetail: IProductReceiptDetail = sampleWithRequiredData;
        expectedResult = service.addProductReceiptDetailToCollectionIfMissing([], null, productReceiptDetail, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productReceiptDetail);
      });

      it('should return initial array if no ProductReceiptDetail is added', () => {
        const productReceiptDetailCollection: IProductReceiptDetail[] = [sampleWithRequiredData];
        expectedResult = service.addProductReceiptDetailToCollectionIfMissing(productReceiptDetailCollection, undefined, null);
        expect(expectedResult).toEqual(productReceiptDetailCollection);
      });
    });

    describe('compareProductReceiptDetail', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProductReceiptDetail(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareProductReceiptDetail(entity1, entity2);
        const compareResult2 = service.compareProductReceiptDetail(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareProductReceiptDetail(entity1, entity2);
        const compareResult2 = service.compareProductReceiptDetail(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareProductReceiptDetail(entity1, entity2);
        const compareResult2 = service.compareProductReceiptDetail(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
