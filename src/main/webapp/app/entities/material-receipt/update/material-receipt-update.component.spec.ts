import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IPlanning } from 'app/entities/planning/planning.model';
import { PlanningService } from 'app/entities/planning/service/planning.service';
import { IMaterialReceipt } from '../material-receipt.model';
import { MaterialReceiptService } from '../service/material-receipt.service';
import { MaterialReceiptFormService } from './material-receipt-form.service';

import { MaterialReceiptUpdateComponent } from './material-receipt-update.component';

describe('MaterialReceipt Management Update Component', () => {
  let comp: MaterialReceiptUpdateComponent;
  let fixture: ComponentFixture<MaterialReceiptUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let materialReceiptFormService: MaterialReceiptFormService;
  let materialReceiptService: MaterialReceiptService;
  let userService: UserService;
  let planningService: PlanningService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MaterialReceiptUpdateComponent],
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
      .overrideTemplate(MaterialReceiptUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MaterialReceiptUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    materialReceiptFormService = TestBed.inject(MaterialReceiptFormService);
    materialReceiptService = TestBed.inject(MaterialReceiptService);
    userService = TestBed.inject(UserService);
    planningService = TestBed.inject(PlanningService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const materialReceipt: IMaterialReceipt = { id: 456 };
      const createdByUser: IUser = { id: 31255 };
      materialReceipt.createdByUser = createdByUser;

      const userCollection: IUser[] = [{ id: 22464 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [createdByUser];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ materialReceipt });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Planning query and add missing value', () => {
      const materialReceipt: IMaterialReceipt = { id: 456 };
      const quantificationOrder: IPlanning = { id: 27691 };
      materialReceipt.quantificationOrder = quantificationOrder;

      const planningCollection: IPlanning[] = [{ id: 17065 }];
      jest.spyOn(planningService, 'query').mockReturnValue(of(new HttpResponse({ body: planningCollection })));
      const additionalPlannings = [quantificationOrder];
      const expectedCollection: IPlanning[] = [...additionalPlannings, ...planningCollection];
      jest.spyOn(planningService, 'addPlanningToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ materialReceipt });
      comp.ngOnInit();

      expect(planningService.query).toHaveBeenCalled();
      expect(planningService.addPlanningToCollectionIfMissing).toHaveBeenCalledWith(
        planningCollection,
        ...additionalPlannings.map(expect.objectContaining),
      );
      expect(comp.planningsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const materialReceipt: IMaterialReceipt = { id: 456 };
      const createdByUser: IUser = { id: 5451 };
      materialReceipt.createdByUser = createdByUser;
      const quantificationOrder: IPlanning = { id: 19296 };
      materialReceipt.quantificationOrder = quantificationOrder;

      activatedRoute.data = of({ materialReceipt });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(createdByUser);
      expect(comp.planningsSharedCollection).toContain(quantificationOrder);
      expect(comp.materialReceipt).toEqual(materialReceipt);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaterialReceipt>>();
      const materialReceipt = { id: 123 };
      jest.spyOn(materialReceiptFormService, 'getMaterialReceipt').mockReturnValue(materialReceipt);
      jest.spyOn(materialReceiptService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ materialReceipt });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: materialReceipt }));
      saveSubject.complete();

      // THEN
      expect(materialReceiptFormService.getMaterialReceipt).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(materialReceiptService.update).toHaveBeenCalledWith(expect.objectContaining(materialReceipt));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaterialReceipt>>();
      const materialReceipt = { id: 123 };
      jest.spyOn(materialReceiptFormService, 'getMaterialReceipt').mockReturnValue({ id: null });
      jest.spyOn(materialReceiptService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ materialReceipt: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: materialReceipt }));
      saveSubject.complete();

      // THEN
      expect(materialReceiptFormService.getMaterialReceipt).toHaveBeenCalled();
      expect(materialReceiptService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaterialReceipt>>();
      const materialReceipt = { id: 123 };
      jest.spyOn(materialReceiptService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ materialReceipt });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(materialReceiptService.update).toHaveBeenCalled();
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

    describe('comparePlanning', () => {
      it('Should forward to planningService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(planningService, 'comparePlanning');
        comp.comparePlanning(entity, entity2);
        expect(planningService.comparePlanning).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
