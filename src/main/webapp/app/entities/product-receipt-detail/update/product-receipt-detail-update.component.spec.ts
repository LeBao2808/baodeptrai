import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IProductReceipt } from 'app/entities/product-receipt/product-receipt.model';
import { ProductReceiptService } from 'app/entities/product-receipt/service/product-receipt.service';
import { IProductReceiptDetail } from '../product-receipt-detail.model';
import { ProductReceiptDetailService } from '../service/product-receipt-detail.service';
import { ProductReceiptDetailFormService } from './product-receipt-detail-form.service';

import { ProductReceiptDetailUpdateComponent } from './product-receipt-detail-update.component';

describe('ProductReceiptDetail Management Update Component', () => {
  let comp: ProductReceiptDetailUpdateComponent;
  let fixture: ComponentFixture<ProductReceiptDetailUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productReceiptDetailFormService: ProductReceiptDetailFormService;
  let productReceiptDetailService: ProductReceiptDetailService;
  let productService: ProductService;
  let productReceiptService: ProductReceiptService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ProductReceiptDetailUpdateComponent],
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
      .overrideTemplate(ProductReceiptDetailUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductReceiptDetailUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productReceiptDetailFormService = TestBed.inject(ProductReceiptDetailFormService);
    productReceiptDetailService = TestBed.inject(ProductReceiptDetailService);
    productService = TestBed.inject(ProductService);
    productReceiptService = TestBed.inject(ProductReceiptService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Product query and add missing value', () => {
      const productReceiptDetail: IProductReceiptDetail = { id: 456 };
      const product: IProduct = { id: 23954 };
      productReceiptDetail.product = product;

      const productCollection: IProduct[] = [{ id: 25330 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productReceiptDetail });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining),
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ProductReceipt query and add missing value', () => {
      const productReceiptDetail: IProductReceiptDetail = { id: 456 };
      const receipt: IProductReceipt = { id: 9 };
      productReceiptDetail.receipt = receipt;

      const productReceiptCollection: IProductReceipt[] = [{ id: 6925 }];
      jest.spyOn(productReceiptService, 'query').mockReturnValue(of(new HttpResponse({ body: productReceiptCollection })));
      const additionalProductReceipts = [receipt];
      const expectedCollection: IProductReceipt[] = [...additionalProductReceipts, ...productReceiptCollection];
      jest.spyOn(productReceiptService, 'addProductReceiptToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productReceiptDetail });
      comp.ngOnInit();

      expect(productReceiptService.query).toHaveBeenCalled();
      expect(productReceiptService.addProductReceiptToCollectionIfMissing).toHaveBeenCalledWith(
        productReceiptCollection,
        ...additionalProductReceipts.map(expect.objectContaining),
      );
      expect(comp.productReceiptsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const productReceiptDetail: IProductReceiptDetail = { id: 456 };
      const product: IProduct = { id: 16745 };
      productReceiptDetail.product = product;
      const receipt: IProductReceipt = { id: 30802 };
      productReceiptDetail.receipt = receipt;

      activatedRoute.data = of({ productReceiptDetail });
      comp.ngOnInit();

      expect(comp.productsSharedCollection).toContain(product);
      expect(comp.productReceiptsSharedCollection).toContain(receipt);
      expect(comp.productReceiptDetail).toEqual(productReceiptDetail);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductReceiptDetail>>();
      const productReceiptDetail = { id: 123 };
      jest.spyOn(productReceiptDetailFormService, 'getProductReceiptDetail').mockReturnValue(productReceiptDetail);
      jest.spyOn(productReceiptDetailService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productReceiptDetail });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productReceiptDetail }));
      saveSubject.complete();

      // THEN
      expect(productReceiptDetailFormService.getProductReceiptDetail).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productReceiptDetailService.update).toHaveBeenCalledWith(expect.objectContaining(productReceiptDetail));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductReceiptDetail>>();
      const productReceiptDetail = { id: 123 };
      jest.spyOn(productReceiptDetailFormService, 'getProductReceiptDetail').mockReturnValue({ id: null });
      jest.spyOn(productReceiptDetailService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productReceiptDetail: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productReceiptDetail }));
      saveSubject.complete();

      // THEN
      expect(productReceiptDetailFormService.getProductReceiptDetail).toHaveBeenCalled();
      expect(productReceiptDetailService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductReceiptDetail>>();
      const productReceiptDetail = { id: 123 };
      jest.spyOn(productReceiptDetailService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productReceiptDetail });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productReceiptDetailService.update).toHaveBeenCalled();
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

    describe('compareProductReceipt', () => {
      it('Should forward to productReceiptService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(productReceiptService, 'compareProductReceipt');
        comp.compareProductReceipt(entity, entity2);
        expect(productReceiptService.compareProductReceipt).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
