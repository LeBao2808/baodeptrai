import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IQuantification } from 'app/entities/quantification/quantification.model';
import { QuantificationService } from 'app/entities/quantification/service/quantification.service';
import { PlanningService } from '../service/planning.service';
import { IPlanning } from '../planning.model';
import { PlanningFormService } from './planning-form.service';

import { PlanningUpdateComponent } from './planning-update.component';

describe('Planning Management Update Component', () => {
  let comp: PlanningUpdateComponent;
  let fixture: ComponentFixture<PlanningUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let planningFormService: PlanningFormService;
  let planningService: PlanningService;
  let quantificationService: QuantificationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PlanningUpdateComponent],
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
      .overrideTemplate(PlanningUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PlanningUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    planningFormService = TestBed.inject(PlanningFormService);
    planningService = TestBed.inject(PlanningService);
    quantificationService = TestBed.inject(QuantificationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Quantification query and add missing value', () => {
      const planning: IPlanning = { id: 456 };
      const quantification: IQuantification = { id: 1897 };
      planning.quantification = quantification;

      const quantificationCollection: IQuantification[] = [{ id: 30961 }];
      jest.spyOn(quantificationService, 'query').mockReturnValue(of(new HttpResponse({ body: quantificationCollection })));
      const additionalQuantifications = [quantification];
      const expectedCollection: IQuantification[] = [...additionalQuantifications, ...quantificationCollection];
      jest.spyOn(quantificationService, 'addQuantificationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ planning });
      comp.ngOnInit();

      expect(quantificationService.query).toHaveBeenCalled();
      expect(quantificationService.addQuantificationToCollectionIfMissing).toHaveBeenCalledWith(
        quantificationCollection,
        ...additionalQuantifications.map(expect.objectContaining),
      );
      expect(comp.quantificationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const planning: IPlanning = { id: 456 };
      const quantification: IQuantification = { id: 24686 };
      planning.quantification = quantification;

      activatedRoute.data = of({ planning });
      comp.ngOnInit();

      expect(comp.quantificationsSharedCollection).toContain(quantification);
      expect(comp.planning).toEqual(planning);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlanning>>();
      const planning = { id: 123 };
      jest.spyOn(planningFormService, 'getPlanning').mockReturnValue(planning);
      jest.spyOn(planningService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ planning });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: planning }));
      saveSubject.complete();

      // THEN
      expect(planningFormService.getPlanning).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(planningService.update).toHaveBeenCalledWith(expect.objectContaining(planning));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlanning>>();
      const planning = { id: 123 };
      jest.spyOn(planningFormService, 'getPlanning').mockReturnValue({ id: null });
      jest.spyOn(planningService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ planning: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: planning }));
      saveSubject.complete();

      // THEN
      expect(planningFormService.getPlanning).toHaveBeenCalled();
      expect(planningService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlanning>>();
      const planning = { id: 123 };
      jest.spyOn(planningService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ planning });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(planningService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareQuantification', () => {
      it('Should forward to quantificationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(quantificationService, 'compareQuantification');
        comp.compareQuantification(entity, entity2);
        expect(quantificationService.compareQuantification).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
