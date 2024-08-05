import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IOffer } from 'app/entities/offer/offer.model';
import { OfferService } from 'app/entities/offer/service/offer.service';
import { IOfferDetail } from '../offer-detail.model';
import { OfferDetailService } from '../service/offer-detail.service';
import { OfferDetailFormService } from './offer-detail-form.service';

import { OfferDetailUpdateComponent } from './offer-detail-update.component';

describe('OfferDetail Management Update Component', () => {
  let comp: OfferDetailUpdateComponent;
  let fixture: ComponentFixture<OfferDetailUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let offerDetailFormService: OfferDetailFormService;
  let offerDetailService: OfferDetailService;
  let productService: ProductService;
  let offerService: OfferService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [OfferDetailUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(OfferDetailUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OfferDetailUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    offerDetailFormService = TestBed.inject(OfferDetailFormService);
    offerDetailService = TestBed.inject(OfferDetailService);
    productService = TestBed.inject(ProductService);
    offerService = TestBed.inject(OfferService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Product query and add missing value', () => {
      const offerDetail: IOfferDetail = { id: 456 };
      const product: IProduct = { id: 21456 };
      offerDetail.product = product;

      const productCollection: IProduct[] = [{ id: 4096 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ offerDetail });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining),
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Offer query and add missing value', () => {
      const offerDetail: IOfferDetail = { id: 456 };
      const offer: IOffer = { id: 4070 };
      offerDetail.offer = offer;

      const offerCollection: IOffer[] = [{ id: 4634 }];
      jest.spyOn(offerService, 'query').mockReturnValue(of(new HttpResponse({ body: offerCollection })));
      const additionalOffers = [offer];
      const expectedCollection: IOffer[] = [...additionalOffers, ...offerCollection];
      jest.spyOn(offerService, 'addOfferToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ offerDetail });
      comp.ngOnInit();

      expect(offerService.query).toHaveBeenCalled();
      expect(offerService.addOfferToCollectionIfMissing).toHaveBeenCalledWith(
        offerCollection,
        ...additionalOffers.map(expect.objectContaining),
      );
      expect(comp.offersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const offerDetail: IOfferDetail = { id: 456 };
      const product: IProduct = { id: 13185 };
      offerDetail.product = product;
      const offer: IOffer = { id: 29106 };
      offerDetail.offer = offer;

      activatedRoute.data = of({ offerDetail });
      comp.ngOnInit();

      expect(comp.productsSharedCollection).toContain(product);
      expect(comp.offersSharedCollection).toContain(offer);
      expect(comp.offerDetail).toEqual(offerDetail);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOfferDetail>>();
      const offerDetail = { id: 123 };
      jest.spyOn(offerDetailFormService, 'getOfferDetail').mockReturnValue(offerDetail);
      jest.spyOn(offerDetailService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ offerDetail });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: offerDetail }));
      saveSubject.complete();

      // THEN
      expect(offerDetailFormService.getOfferDetail).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(offerDetailService.update).toHaveBeenCalledWith(expect.objectContaining(offerDetail));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOfferDetail>>();
      const offerDetail = { id: 123 };
      jest.spyOn(offerDetailFormService, 'getOfferDetail').mockReturnValue({ id: null });
      jest.spyOn(offerDetailService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ offerDetail: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: offerDetail }));
      saveSubject.complete();

      // THEN
      expect(offerDetailFormService.getOfferDetail).toHaveBeenCalled();
      expect(offerDetailService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOfferDetail>>();
      const offerDetail = { id: 123 };
      jest.spyOn(offerDetailService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ offerDetail });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(offerDetailService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProduct', () => {
      it('Should forward to productService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(productService, 'compareProduct');
        comp.compareProduct(entity, entity2);
        expect(productService.compareProduct).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareOffer', () => {
      it('Should forward to offerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(offerService, 'compareOffer');
        comp.compareOffer(entity, entity2);
        expect(offerService.compareOffer).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
