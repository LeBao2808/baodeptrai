import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IPlanning } from 'app/entities/planning/planning.model';
import { PlanningService } from 'app/entities/planning/service/planning.service';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IProductOrder } from '../product-order.model';
import { ProductOrderService } from '../service/product-order.service';
import { ProductOrderFormService } from './product-order-form.service';

import { ProductOrderUpdateComponent } from './product-order-update.component';

describe('ProductOrder Management Update Component', () => {
  let comp: ProductOrderUpdateComponent;
  let fixture: ComponentFixture<ProductOrderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productOrderFormService: ProductOrderFormService;
  let productOrderService: ProductOrderService;
  let planningService: PlanningService;
  let customerService: CustomerService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ProductOrderUpdateComponent],
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
      .overrideTemplate(ProductOrderUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductOrderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productOrderFormService = TestBed.inject(ProductOrderFormService);
    productOrderService = TestBed.inject(ProductOrderService);
    planningService = TestBed.inject(PlanningService);
    customerService = TestBed.inject(CustomerService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Planning query and add missing value', () => {
      const productOrder: IProductOrder = { id: 456 };
      const quantificationOrder: IPlanning = { id: 2585 };
      productOrder.quantificationOrder = quantificationOrder;

      const planningCollection: IPlanning[] = [{ id: 26557 }];
      jest.spyOn(planningService, 'query').mockReturnValue(of(new HttpResponse({ body: planningCollection })));
      const additionalPlannings = [quantificationOrder];
      const expectedCollection: IPlanning[] = [...additionalPlannings, ...planningCollection];
      jest.spyOn(planningService, 'addPlanningToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productOrder });
      comp.ngOnInit();

      expect(planningService.query).toHaveBeenCalled();
      expect(planningService.addPlanningToCollectionIfMissing).toHaveBeenCalledWith(
        planningCollection,
        ...additionalPlannings.map(expect.objectContaining),
      );
      expect(comp.planningsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Customer query and add missing value', () => {
      const productOrder: IProductOrder = { id: 456 };
      const customer: ICustomer = { id: 19673 };
      productOrder.customer = customer;

      const customerCollection: ICustomer[] = [{ id: 73 }];
      jest.spyOn(customerService, 'query').mockReturnValue(of(new HttpResponse({ body: customerCollection })));
      const additionalCustomers = [customer];
      const expectedCollection: ICustomer[] = [...additionalCustomers, ...customerCollection];
      jest.spyOn(customerService, 'addCustomerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productOrder });
      comp.ngOnInit();

      expect(customerService.query).toHaveBeenCalled();
      expect(customerService.addCustomerToCollectionIfMissing).toHaveBeenCalledWith(
        customerCollection,
        ...additionalCustomers.map(expect.objectContaining),
      );
      expect(comp.customersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const productOrder: IProductOrder = { id: 456 };
      const createdByUser: IUser = { id: 7983 };
      productOrder.createdByUser = createdByUser;

      const userCollection: IUser[] = [{ id: 26939 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [createdByUser];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productOrder });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const productOrder: IProductOrder = { id: 456 };
      const quantificationOrder: IPlanning = { id: 2588 };
      productOrder.quantificationOrder = quantificationOrder;
      const customer: ICustomer = { id: 4072 };
      productOrder.customer = customer;
      const createdByUser: IUser = { id: 15652 };
      productOrder.createdByUser = createdByUser;

      activatedRoute.data = of({ productOrder });
      comp.ngOnInit();

      expect(comp.planningsSharedCollection).toContain(quantificationOrder);
      expect(comp.customersSharedCollection).toContain(customer);
      expect(comp.usersSharedCollection).toContain(createdByUser);
      expect(comp.productOrder).toEqual(productOrder);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductOrder>>();
      const productOrder = { id: 123 };
      jest.spyOn(productOrderFormService, 'getProductOrder').mockReturnValue(productOrder);
      jest.spyOn(productOrderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productOrder }));
      saveSubject.complete();

      // THEN
      expect(productOrderFormService.getProductOrder).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productOrderService.update).toHaveBeenCalledWith(expect.objectContaining(productOrder));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductOrder>>();
      const productOrder = { id: 123 };
      jest.spyOn(productOrderFormService, 'getProductOrder').mockReturnValue({ id: null });
      jest.spyOn(productOrderService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productOrder: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productOrder }));
      saveSubject.complete();

      // THEN
      expect(productOrderFormService.getProductOrder).toHaveBeenCalled();
      expect(productOrderService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductOrder>>();
      const productOrder = { id: 123 };
      jest.spyOn(productOrderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productOrderService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePlanning', () => {
      it('Should forward to planningService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(planningService, 'comparePlanning');
        comp.comparePlanning(entity, entity2);
        expect(planningService.comparePlanning).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCustomer', () => {
      it('Should forward to customerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(customerService, 'compareCustomer');
        comp.compareCustomer(entity, entity2);
        expect(customerService.compareCustomer).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
