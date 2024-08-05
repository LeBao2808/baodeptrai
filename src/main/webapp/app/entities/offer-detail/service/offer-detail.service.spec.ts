import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IOfferDetail } from '../offer-detail.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../offer-detail.test-samples';

import { OfferDetailService } from './offer-detail.service';

const requireRestSample: IOfferDetail = {
  ...sampleWithRequiredData,
};

describe('OfferDetail Service', () => {
  let service: OfferDetailService;
  let httpMock: HttpTestingController;
  let expectedResult: IOfferDetail | IOfferDetail[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(OfferDetailService);
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

    it('should create a OfferDetail', () => {
      const offerDetail = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(offerDetail).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OfferDetail', () => {
      const offerDetail = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(offerDetail).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OfferDetail', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OfferDetail', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OfferDetail', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOfferDetailToCollectionIfMissing', () => {
      it('should add a OfferDetail to an empty array', () => {
        const offerDetail: IOfferDetail = sampleWithRequiredData;
        expectedResult = service.addOfferDetailToCollectionIfMissing([], offerDetail);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(offerDetail);
      });

      it('should not add a OfferDetail to an array that contains it', () => {
        const offerDetail: IOfferDetail = sampleWithRequiredData;
        const offerDetailCollection: IOfferDetail[] = [
          {
            ...offerDetail,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOfferDetailToCollectionIfMissing(offerDetailCollection, offerDetail);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OfferDetail to an array that doesn't contain it", () => {
        const offerDetail: IOfferDetail = sampleWithRequiredData;
        const offerDetailCollection: IOfferDetail[] = [sampleWithPartialData];
        expectedResult = service.addOfferDetailToCollectionIfMissing(offerDetailCollection, offerDetail);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(offerDetail);
      });

      it('should add only unique OfferDetail to an array', () => {
        const offerDetailArray: IOfferDetail[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const offerDetailCollection: IOfferDetail[] = [sampleWithRequiredData];
        expectedResult = service.addOfferDetailToCollectionIfMissing(offerDetailCollection, ...offerDetailArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const offerDetail: IOfferDetail = sampleWithRequiredData;
        const offerDetail2: IOfferDetail = sampleWithPartialData;
        expectedResult = service.addOfferDetailToCollectionIfMissing([], offerDetail, offerDetail2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(offerDetail);
        expect(expectedResult).toContain(offerDetail2);
      });

      it('should accept null and undefined values', () => {
        const offerDetail: IOfferDetail = sampleWithRequiredData;
        expectedResult = service.addOfferDetailToCollectionIfMissing([], null, offerDetail, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(offerDetail);
      });

      it('should return initial array if no OfferDetail is added', () => {
        const offerDetailCollection: IOfferDetail[] = [sampleWithRequiredData];
        expectedResult = service.addOfferDetailToCollectionIfMissing(offerDetailCollection, undefined, null);
        expect(expectedResult).toEqual(offerDetailCollection);
      });
    });

    describe('compareOfferDetail', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOfferDetail(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareOfferDetail(entity1, entity2);
        const compareResult2 = service.compareOfferDetail(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareOfferDetail(entity1, entity2);
        const compareResult2 = service.compareOfferDetail(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareOfferDetail(entity1, entity2);
        const compareResult2 = service.compareOfferDetail(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
