import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IProductReceipt } from '../product-receipt.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../product-receipt.test-samples';

import { ProductReceiptService, RestProductReceipt } from './product-receipt.service';

const requireRestSample: RestProductReceipt = {
  ...sampleWithRequiredData,
  creationDate: sampleWithRequiredData.creationDate?.toJSON(),
  paymentDate: sampleWithRequiredData.paymentDate?.toJSON(),
  receiptDate: sampleWithRequiredData.receiptDate?.toJSON(),
};

describe('ProductReceipt Service', () => {
  let service: ProductReceiptService;
  let httpMock: HttpTestingController;
  let expectedResult: IProductReceipt | IProductReceipt[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ProductReceiptService);
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

    it('should create a ProductReceipt', () => {
      const productReceipt = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(productReceipt).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductReceipt', () => {
      const productReceipt = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(productReceipt).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductReceipt', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductReceipt', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProductReceipt', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addProductReceiptToCollectionIfMissing', () => {
      it('should add a ProductReceipt to an empty array', () => {
        const productReceipt: IProductReceipt = sampleWithRequiredData;
        expectedResult = service.addProductReceiptToCollectionIfMissing([], productReceipt);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productReceipt);
      });

      it('should not add a ProductReceipt to an array that contains it', () => {
        const productReceipt: IProductReceipt = sampleWithRequiredData;
        const productReceiptCollection: IProductReceipt[] = [
          {
            ...productReceipt,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProductReceiptToCollectionIfMissing(productReceiptCollection, productReceipt);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductReceipt to an array that doesn't contain it", () => {
        const productReceipt: IProductReceipt = sampleWithRequiredData;
        const productReceiptCollection: IProductReceipt[] = [sampleWithPartialData];
        expectedResult = service.addProductReceiptToCollectionIfMissing(productReceiptCollection, productReceipt);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productReceipt);
      });

      it('should add only unique ProductReceipt to an array', () => {
        const productReceiptArray: IProductReceipt[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const productReceiptCollection: IProductReceipt[] = [sampleWithRequiredData];
        expectedResult = service.addProductReceiptToCollectionIfMissing(productReceiptCollection, ...productReceiptArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productReceipt: IProductReceipt = sampleWithRequiredData;
        const productReceipt2: IProductReceipt = sampleWithPartialData;
        expectedResult = service.addProductReceiptToCollectionIfMissing([], productReceipt, productReceipt2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productReceipt);
        expect(expectedResult).toContain(productReceipt2);
      });

      it('should accept null and undefined values', () => {
        const productReceipt: IProductReceipt = sampleWithRequiredData;
        expectedResult = service.addProductReceiptToCollectionIfMissing([], null, productReceipt, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productReceipt);
      });

      it('should return initial array if no ProductReceipt is added', () => {
        const productReceiptCollection: IProductReceipt[] = [sampleWithRequiredData];
        expectedResult = service.addProductReceiptToCollectionIfMissing(productReceiptCollection, undefined, null);
        expect(expectedResult).toEqual(productReceiptCollection);
      });
    });

    describe('compareProductReceipt', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProductReceipt(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareProductReceipt(entity1, entity2);
        const compareResult2 = service.compareProductReceipt(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareProductReceipt(entity1, entity2);
        const compareResult2 = service.compareProductReceipt(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareProductReceipt(entity1, entity2);
        const compareResult2 = service.compareProductReceipt(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
