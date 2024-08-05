import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IProductOrderDetail } from '../product-order-detail.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../product-order-detail.test-samples';

import { ProductOrderDetailService, RestProductOrderDetail } from './product-order-detail.service';

const requireRestSample: RestProductOrderDetail = {
  ...sampleWithRequiredData,
  orderCreationDate: sampleWithRequiredData.orderCreationDate?.toJSON(),
};

describe('ProductOrderDetail Service', () => {
  let service: ProductOrderDetailService;
  let httpMock: HttpTestingController;
  let expectedResult: IProductOrderDetail | IProductOrderDetail[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ProductOrderDetailService);
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

    it('should create a ProductOrderDetail', () => {
      const productOrderDetail = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(productOrderDetail).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductOrderDetail', () => {
      const productOrderDetail = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(productOrderDetail).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductOrderDetail', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductOrderDetail', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProductOrderDetail', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addProductOrderDetailToCollectionIfMissing', () => {
      it('should add a ProductOrderDetail to an empty array', () => {
        const productOrderDetail: IProductOrderDetail = sampleWithRequiredData;
        expectedResult = service.addProductOrderDetailToCollectionIfMissing([], productOrderDetail);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productOrderDetail);
      });

      it('should not add a ProductOrderDetail to an array that contains it', () => {
        const productOrderDetail: IProductOrderDetail = sampleWithRequiredData;
        const productOrderDetailCollection: IProductOrderDetail[] = [
          {
            ...productOrderDetail,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProductOrderDetailToCollectionIfMissing(productOrderDetailCollection, productOrderDetail);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductOrderDetail to an array that doesn't contain it", () => {
        const productOrderDetail: IProductOrderDetail = sampleWithRequiredData;
        const productOrderDetailCollection: IProductOrderDetail[] = [sampleWithPartialData];
        expectedResult = service.addProductOrderDetailToCollectionIfMissing(productOrderDetailCollection, productOrderDetail);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productOrderDetail);
      });

      it('should add only unique ProductOrderDetail to an array', () => {
        const productOrderDetailArray: IProductOrderDetail[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const productOrderDetailCollection: IProductOrderDetail[] = [sampleWithRequiredData];
        expectedResult = service.addProductOrderDetailToCollectionIfMissing(productOrderDetailCollection, ...productOrderDetailArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productOrderDetail: IProductOrderDetail = sampleWithRequiredData;
        const productOrderDetail2: IProductOrderDetail = sampleWithPartialData;
        expectedResult = service.addProductOrderDetailToCollectionIfMissing([], productOrderDetail, productOrderDetail2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productOrderDetail);
        expect(expectedResult).toContain(productOrderDetail2);
      });

      it('should accept null and undefined values', () => {
        const productOrderDetail: IProductOrderDetail = sampleWithRequiredData;
        expectedResult = service.addProductOrderDetailToCollectionIfMissing([], null, productOrderDetail, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productOrderDetail);
      });

      it('should return initial array if no ProductOrderDetail is added', () => {
        const productOrderDetailCollection: IProductOrderDetail[] = [sampleWithRequiredData];
        expectedResult = service.addProductOrderDetailToCollectionIfMissing(productOrderDetailCollection, undefined, null);
        expect(expectedResult).toEqual(productOrderDetailCollection);
      });
    });

    describe('compareProductOrderDetail', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProductOrderDetail(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareProductOrderDetail(entity1, entity2);
        const compareResult2 = service.compareProductOrderDetail(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareProductOrderDetail(entity1, entity2);
        const compareResult2 = service.compareProductOrderDetail(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareProductOrderDetail(entity1, entity2);
        const compareResult2 = service.compareProductOrderDetail(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
