import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../material-export.test-samples';

import { MaterialExportFormService } from './material-export-form.service';

describe('MaterialExport Form Service', () => {
  let service: MaterialExportFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MaterialExportFormService);
  });

  describe('Service methods', () => {
    describe('createMaterialExportFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMaterialExportFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            creationDate: expect.any(Object),
            exportDate: expect.any(Object),
            status: expect.any(Object),
            code: expect.any(Object),
            createdByUser: expect.any(Object),
            productionSite: expect.any(Object),
          }),
        );
      });

      it('passing IMaterialExport should create a new form with FormGroup', () => {
        const formGroup = service.createMaterialExportFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            creationDate: expect.any(Object),
            exportDate: expect.any(Object),
            status: expect.any(Object),
            code: expect.any(Object),
            createdByUser: expect.any(Object),
            productionSite: expect.any(Object),
          }),
        );
      });
    });

    describe('getMaterialExport', () => {
      it('should return NewMaterialExport for default MaterialExport initial value', () => {
        const formGroup = service.createMaterialExportFormGroup(sampleWithNewData);

        const materialExport = service.getMaterialExport(formGroup) as any;

        expect(materialExport).toMatchObject(sampleWithNewData);
      });

      it('should return NewMaterialExport for empty MaterialExport initial value', () => {
        const formGroup = service.createMaterialExportFormGroup();

        const materialExport = service.getMaterialExport(formGroup) as any;

        expect(materialExport).toMatchObject({});
      });

      it('should return IMaterialExport', () => {
        const formGroup = service.createMaterialExportFormGroup(sampleWithRequiredData);

        const materialExport = service.getMaterialExport(formGroup) as any;

        expect(materialExport).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMaterialExport should not enable id FormControl', () => {
        const formGroup = service.createMaterialExportFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMaterialExport should disable id FormControl', () => {
        const formGroup = service.createMaterialExportFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
