import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IProductionSite } from 'app/entities/production-site/production-site.model';
import { ProductionSiteService } from 'app/entities/production-site/service/production-site.service';
import { IMaterialExport } from '../material-export.model';
import { MaterialExportService } from '../service/material-export.service';
import { MaterialExportFormService } from './material-export-form.service';

import { MaterialExportUpdateComponent } from './material-export-update.component';

describe('MaterialExport Management Update Component', () => {
  let comp: MaterialExportUpdateComponent;
  let fixture: ComponentFixture<MaterialExportUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let materialExportFormService: MaterialExportFormService;
  let materialExportService: MaterialExportService;
  let userService: UserService;
  let productionSiteService: ProductionSiteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MaterialExportUpdateComponent],
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
      .overrideTemplate(MaterialExportUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MaterialExportUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    materialExportFormService = TestBed.inject(MaterialExportFormService);
    materialExportService = TestBed.inject(MaterialExportService);
    userService = TestBed.inject(UserService);
    productionSiteService = TestBed.inject(ProductionSiteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const materialExport: IMaterialExport = { id: 456 };
      const createdByUser: IUser = { id: 2944 };
      materialExport.createdByUser = createdByUser;

      const userCollection: IUser[] = [{ id: 32751 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [createdByUser];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ materialExport });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ProductionSite query and add missing value', () => {
      const materialExport: IMaterialExport = { id: 456 };
      const productionSite: IProductionSite = { id: 3971 };
      materialExport.productionSite = productionSite;

      const productionSiteCollection: IProductionSite[] = [{ id: 25591 }];
      jest.spyOn(productionSiteService, 'query').mockReturnValue(of(new HttpResponse({ body: productionSiteCollection })));
      const additionalProductionSites = [productionSite];
      const expectedCollection: IProductionSite[] = [...additionalProductionSites, ...productionSiteCollection];
      jest.spyOn(productionSiteService, 'addProductionSiteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ materialExport });
      comp.ngOnInit();

      expect(productionSiteService.query).toHaveBeenCalled();
      expect(productionSiteService.addProductionSiteToCollectionIfMissing).toHaveBeenCalledWith(
        productionSiteCollection,
        ...additionalProductionSites.map(expect.objectContaining),
      );
      expect(comp.productionSitesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const materialExport: IMaterialExport = { id: 456 };
      const createdByUser: IUser = { id: 31512 };
      materialExport.createdByUser = createdByUser;
      const productionSite: IProductionSite = { id: 23140 };
      materialExport.productionSite = productionSite;

      activatedRoute.data = of({ materialExport });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(createdByUser);
      expect(comp.productionSitesSharedCollection).toContain(productionSite);
      expect(comp.materialExport).toEqual(materialExport);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaterialExport>>();
      const materialExport = { id: 123 };
      jest.spyOn(materialExportFormService, 'getMaterialExport').mockReturnValue(materialExport);
      jest.spyOn(materialExportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ materialExport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: materialExport }));
      saveSubject.complete();

      // THEN
      expect(materialExportFormService.getMaterialExport).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(materialExportService.update).toHaveBeenCalledWith(expect.objectContaining(materialExport));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaterialExport>>();
      const materialExport = { id: 123 };
      jest.spyOn(materialExportFormService, 'getMaterialExport').mockReturnValue({ id: null });
      jest.spyOn(materialExportService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ materialExport: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: materialExport }));
      saveSubject.complete();

      // THEN
      expect(materialExportFormService.getMaterialExport).toHaveBeenCalled();
      expect(materialExportService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaterialExport>>();
      const materialExport = { id: 123 };
      jest.spyOn(materialExportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ materialExport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(materialExportService.update).toHaveBeenCalled();
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

    describe('compareProductionSite', () => {
      it('Should forward to productionSiteService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(productionSiteService, 'compareProductionSite');
        comp.compareProductionSite(entity, entity2);
        expect(productionSiteService.compareProductionSite).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
