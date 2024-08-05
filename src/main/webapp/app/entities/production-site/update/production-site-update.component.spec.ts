import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { ProductionSiteService } from '../service/production-site.service';
import { IProductionSite } from '../production-site.model';
import { ProductionSiteFormService } from './production-site-form.service';

import { ProductionSiteUpdateComponent } from './production-site-update.component';

describe('ProductionSite Management Update Component', () => {
  let comp: ProductionSiteUpdateComponent;
  let fixture: ComponentFixture<ProductionSiteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productionSiteFormService: ProductionSiteFormService;
  let productionSiteService: ProductionSiteService;
  let productService: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ProductionSiteUpdateComponent],
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
      .overrideTemplate(ProductionSiteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductionSiteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productionSiteFormService = TestBed.inject(ProductionSiteFormService);
    productionSiteService = TestBed.inject(ProductionSiteService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Product query and add missing value', () => {
      const productionSite: IProductionSite = { id: 456 };
      const productId: IProduct = { id: 30814 };
      productionSite.productId = productId;

      const productCollection: IProduct[] = [{ id: 21686 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [productId];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productionSite });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining),
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const productionSite: IProductionSite = { id: 456 };
      const productId: IProduct = { id: 30673 };
      productionSite.productId = productId;

      activatedRoute.data = of({ productionSite });
      comp.ngOnInit();

      expect(comp.productsSharedCollection).toContain(productId);
      expect(comp.productionSite).toEqual(productionSite);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductionSite>>();
      const productionSite = { id: 123 };
      jest.spyOn(productionSiteFormService, 'getProductionSite').mockReturnValue(productionSite);
      jest.spyOn(productionSiteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productionSite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productionSite }));
      saveSubject.complete();

      // THEN
      expect(productionSiteFormService.getProductionSite).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productionSiteService.update).toHaveBeenCalledWith(expect.objectContaining(productionSite));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductionSite>>();
      const productionSite = { id: 123 };
      jest.spyOn(productionSiteFormService, 'getProductionSite').mockReturnValue({ id: null });
      jest.spyOn(productionSiteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productionSite: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productionSite }));
      saveSubject.complete();

      // THEN
      expect(productionSiteFormService.getProductionSite).toHaveBeenCalled();
      expect(productionSiteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductionSite>>();
      const productionSite = { id: 123 };
      jest.spyOn(productionSiteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productionSite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productionSiteService.update).toHaveBeenCalled();
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
  });
});
