import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IQuantification } from '../quantification.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../quantification.test-samples';

import { QuantificationService } from './quantification.service';

const requireRestSample: IQuantification = {
  ...sampleWithRequiredData,
};

describe('Quantification Service', () => {
  let service: QuantificationService;
  let httpMock: HttpTestingController;
  let expectedResult: IQuantification | IQuantification[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(QuantificationService);
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

    it('should create a Quantification', () => {
      const quantification = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(quantification).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Quantification', () => {
      const quantification = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(quantification).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Quantification', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Quantification', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Quantification', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addQuantificationToCollectionIfMissing', () => {
      it('should add a Quantification to an empty array', () => {
        const quantification: IQuantification = sampleWithRequiredData;
        expectedResult = service.addQuantificationToCollectionIfMissing([], quantification);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(quantification);
      });

      it('should not add a Quantification to an array that contains it', () => {
        const quantification: IQuantification = sampleWithRequiredData;
        const quantificationCollection: IQuantification[] = [
          {
            ...quantification,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addQuantificationToCollectionIfMissing(quantificationCollection, quantification);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Quantification to an array that doesn't contain it", () => {
        const quantification: IQuantification = sampleWithRequiredData;
        const quantificationCollection: IQuantification[] = [sampleWithPartialData];
        expectedResult = service.addQuantificationToCollectionIfMissing(quantificationCollection, quantification);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(quantification);
      });

      it('should add only unique Quantification to an array', () => {
        const quantificationArray: IQuantification[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const quantificationCollection: IQuantification[] = [sampleWithRequiredData];
        expectedResult = service.addQuantificationToCollectionIfMissing(quantificationCollection, ...quantificationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const quantification: IQuantification = sampleWithRequiredData;
        const quantification2: IQuantification = sampleWithPartialData;
        expectedResult = service.addQuantificationToCollectionIfMissing([], quantification, quantification2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(quantification);
        expect(expectedResult).toContain(quantification2);
      });

      it('should accept null and undefined values', () => {
        const quantification: IQuantification = sampleWithRequiredData;
        expectedResult = service.addQuantificationToCollectionIfMissing([], null, quantification, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(quantification);
      });

      it('should return initial array if no Quantification is added', () => {
        const quantificationCollection: IQuantification[] = [sampleWithRequiredData];
        expectedResult = service.addQuantificationToCollectionIfMissing(quantificationCollection, undefined, null);
        expect(expectedResult).toEqual(quantificationCollection);
      });
    });

    describe('compareQuantification', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareQuantification(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareQuantification(entity1, entity2);
        const compareResult2 = service.compareQuantification(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareQuantification(entity1, entity2);
        const compareResult2 = service.compareQuantification(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareQuantification(entity1, entity2);
        const compareResult2 = service.compareQuantification(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
