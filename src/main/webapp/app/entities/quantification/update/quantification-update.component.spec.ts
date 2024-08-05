import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IMaterial } from 'app/entities/material/material.model';
import { MaterialService } from 'app/entities/material/service/material.service';
import { IQuantification } from '../quantification.model';
import { QuantificationService } from '../service/quantification.service';
import { QuantificationFormService } from './quantification-form.service';

import { QuantificationUpdateComponent } from './quantification-update.component';

describe('Quantification Management Update Component', () => {
  let comp: QuantificationUpdateComponent;
  let fixture: ComponentFixture<QuantificationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let quantificationFormService: QuantificationFormService;
  let quantificationService: QuantificationService;
  let productService: ProductService;
  let materialService: MaterialService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [QuantificationUpdateComponent],
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
      .overrideTemplate(QuantificationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QuantificationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    quantificationFormService = TestBed.inject(QuantificationFormService);
    quantificationService = TestBed.inject(QuantificationService);
    productService = TestBed.inject(ProductService);
    materialService = TestBed.inject(MaterialService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Product query and add missing value', () => {
      const quantification: IQuantification = { id: 456 };
      const product: IProduct = { id: 24891 };
      quantification.product = product;

      const productCollection: IProduct[] = [{ id: 12515 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quantification });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining),
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Material query and add missing value', () => {
      const quantification: IQuantification = { id: 456 };
      const material: IMaterial = { id: 16145 };
      quantification.material = material;

      const materialCollection: IMaterial[] = [{ id: 20892 }];
      jest.spyOn(materialService, 'query').mockReturnValue(of(new HttpResponse({ body: materialCollection })));
      const additionalMaterials = [material];
      const expectedCollection: IMaterial[] = [...additionalMaterials, ...materialCollection];
      jest.spyOn(materialService, 'addMaterialToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quantification });
      comp.ngOnInit();

      expect(materialService.query).toHaveBeenCalled();
      expect(materialService.addMaterialToCollectionIfMissing).toHaveBeenCalledWith(
        materialCollection,
        ...additionalMaterials.map(expect.objectContaining),
      );
      expect(comp.materialsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const quantification: IQuantification = { id: 456 };
      const product: IProduct = { id: 19916 };
      quantification.product = product;
      const material: IMaterial = { id: 28234 };
      quantification.material = material;

      activatedRoute.data = of({ quantification });
      comp.ngOnInit();

      expect(comp.productsSharedCollection).toContain(product);
      expect(comp.materialsSharedCollection).toContain(material);
      expect(comp.quantification).toEqual(quantification);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuantification>>();
      const quantification = { id: 123 };
      jest.spyOn(quantificationFormService, 'getQuantification').mockReturnValue(quantification);
      jest.spyOn(quantificationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quantification });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: quantification }));
      saveSubject.complete();

      // THEN
      expect(quantificationFormService.getQuantification).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(quantificationService.update).toHaveBeenCalledWith(expect.objectContaining(quantification));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuantification>>();
      const quantification = { id: 123 };
      jest.spyOn(quantificationFormService, 'getQuantification').mockReturnValue({ id: null });
      jest.spyOn(quantificationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quantification: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: quantification }));
      saveSubject.complete();

      // THEN
      expect(quantificationFormService.getQuantification).toHaveBeenCalled();
      expect(quantificationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuantification>>();
      const quantification = { id: 123 };
      jest.spyOn(quantificationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quantification });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(quantificationService.update).toHaveBeenCalled();
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

    describe('compareMaterial', () => {
      it('Should forward to materialService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(materialService, 'compareMaterial');
        comp.compareMaterial(entity, entity2);
        expect(materialService.compareMaterial).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
