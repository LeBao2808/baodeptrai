import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { ProductReceiptService } from '../service/product-receipt.service';
import { IProductReceipt } from '../product-receipt.model';
import { ProductReceiptFormService } from './product-receipt-form.service';

import { ProductReceiptUpdateComponent } from './product-receipt-update.component';

describe('ProductReceipt Management Update Component', () => {
  let comp: ProductReceiptUpdateComponent;
  let fixture: ComponentFixture<ProductReceiptUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productReceiptFormService: ProductReceiptFormService;
  let productReceiptService: ProductReceiptService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ProductReceiptUpdateComponent],
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
      .overrideTemplate(ProductReceiptUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductReceiptUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productReceiptFormService = TestBed.inject(ProductReceiptFormService);
    productReceiptService = TestBed.inject(ProductReceiptService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const productReceipt: IProductReceipt = { id: 456 };
      const created: IUser = { id: 25922 };
      productReceipt.created = created;

      const userCollection: IUser[] = [{ id: 30267 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [created];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productReceipt });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const productReceipt: IProductReceipt = { id: 456 };
      const created: IUser = { id: 30453 };
      productReceipt.created = created;

      activatedRoute.data = of({ productReceipt });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(created);
      expect(comp.productReceipt).toEqual(productReceipt);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductReceipt>>();
      const productReceipt = { id: 123 };
      jest.spyOn(productReceiptFormService, 'getProductReceipt').mockReturnValue(productReceipt);
      jest.spyOn(productReceiptService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productReceipt });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productReceipt }));
      saveSubject.complete();

      // THEN
      expect(productReceiptFormService.getProductReceipt).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productReceiptService.update).toHaveBeenCalledWith(expect.objectContaining(productReceipt));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductReceipt>>();
      const productReceipt = { id: 123 };
      jest.spyOn(productReceiptFormService, 'getProductReceipt').mockReturnValue({ id: null });
      jest.spyOn(productReceiptService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productReceipt: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productReceipt }));
      saveSubject.complete();

      // THEN
      expect(productReceiptFormService.getProductReceipt).toHaveBeenCalled();
      expect(productReceiptService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductReceipt>>();
      const productReceipt = { id: 123 };
      jest.spyOn(productReceiptService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productReceipt });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productReceiptService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
