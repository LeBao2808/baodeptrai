import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IProductOrder } from 'app/entities/product-order/product-order.model';
import { ProductOrderService } from 'app/entities/product-order/service/product-order.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IProductOrderDetail } from '../product-order-detail.model';
import { ProductOrderDetailService } from '../service/product-order-detail.service';
import { ProductOrderDetailFormService } from './product-order-detail-form.service';

import { ProductOrderDetailUpdateComponent } from './product-order-detail-update.component';

describe('ProductOrderDetail Management Update Component', () => {
  let comp: ProductOrderDetailUpdateComponent;
  let fixture: ComponentFixture<ProductOrderDetailUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productOrderDetailFormService: ProductOrderDetailFormService;
  let productOrderDetailService: ProductOrderDetailService;
  let productOrderService: ProductOrderService;
  let productService: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ProductOrderDetailUpdateComponent],
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
      .overrideTemplate(ProductOrderDetailUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductOrderDetailUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productOrderDetailFormService = TestBed.inject(ProductOrderDetailFormService);
    productOrderDetailService = TestBed.inject(ProductOrderDetailService);
    productOrderService = TestBed.inject(ProductOrderService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ProductOrder query and add missing value', () => {
      const productOrderDetail: IProductOrderDetail = { id: 456 };
      const order: IProductOrder = { id: 15299 };
      productOrderDetail.order = order;

      const productOrderCollection: IProductOrder[] = [{ id: 16957 }];
      jest.spyOn(productOrderService, 'query').mockReturnValue(of(new HttpResponse({ body: productOrderCollection })));
      const additionalProductOrders = [order];
      const expectedCollection: IProductOrder[] = [...additionalProductOrders, ...productOrderCollection];
      jest.spyOn(productOrderService, 'addProductOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productOrderDetail });
      comp.ngOnInit();

      expect(productOrderService.query).toHaveBeenCalled();
      expect(productOrderService.addProductOrderToCollectionIfMissing).toHaveBeenCalledWith(
        productOrderCollection,
        ...additionalProductOrders.map(expect.objectContaining),
      );
      expect(comp.productOrdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Product query and add missing value', () => {
      const productOrderDetail: IProductOrderDetail = { id: 456 };
      const product: IProduct = { id: 6052 };
      productOrderDetail.product = product;

      const productCollection: IProduct[] = [{ id: 8552 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productOrderDetail });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining),
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const productOrderDetail: IProductOrderDetail = { id: 456 };
      const order: IProductOrder = { id: 21884 };
      productOrderDetail.order = order;
      const product: IProduct = { id: 10913 };
      productOrderDetail.product = product;

      activatedRoute.data = of({ productOrderDetail });
      comp.ngOnInit();

      expect(comp.productOrdersSharedCollection).toContain(order);
      expect(comp.productsSharedCollection).toContain(product);
      expect(comp.productOrderDetail).toEqual(productOrderDetail);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductOrderDetail>>();
      const productOrderDetail = { id: 123 };
      jest.spyOn(productOrderDetailFormService, 'getProductOrderDetail').mockReturnValue(productOrderDetail);
      jest.spyOn(productOrderDetailService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productOrderDetail });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productOrderDetail }));
      saveSubject.complete();

      // THEN
      expect(productOrderDetailFormService.getProductOrderDetail).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productOrderDetailService.update).toHaveBeenCalledWith(expect.objectContaining(productOrderDetail));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductOrderDetail>>();
      const productOrderDetail = { id: 123 };
      jest.spyOn(productOrderDetailFormService, 'getProductOrderDetail').mockReturnValue({ id: null });
      jest.spyOn(productOrderDetailService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productOrderDetail: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productOrderDetail }));
      saveSubject.complete();

      // THEN
      expect(productOrderDetailFormService.getProductOrderDetail).toHaveBeenCalled();
      expect(productOrderDetailService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductOrderDetail>>();
      const productOrderDetail = { id: 123 };
      jest.spyOn(productOrderDetailService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productOrderDetail });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productOrderDetailService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProductOrder', () => {
      it('Should forward to productOrderService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(productOrderService, 'compareProductOrder');
        comp.compareProductOrder(entity, entity2);
        expect(productOrderService.compareProductOrder).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
