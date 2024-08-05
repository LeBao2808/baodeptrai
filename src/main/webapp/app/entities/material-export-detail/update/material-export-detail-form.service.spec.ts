import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../material-export-detail.test-samples';

import { MaterialExportDetailFormService } from './material-export-detail-form.service';

describe('MaterialExportDetail Form Service', () => {
  let service: MaterialExportDetailFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MaterialExportDetailFormService);
  });

  describe('Service methods', () => {
    describe('createMaterialExportDetailFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMaterialExportDetailFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            note: expect.any(Object),
            exportPrice: expect.any(Object),
            quantity: expect.any(Object),
            materialExport: expect.any(Object),
            material: expect.any(Object),
          }),
        );
      });

      it('passing IMaterialExportDetail should create a new form with FormGroup', () => {
        const formGroup = service.createMaterialExportDetailFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            note: expect.any(Object),
            exportPrice: expect.any(Object),
            quantity: expect.any(Object),
            materialExport: expect.any(Object),
            material: expect.any(Object),
          }),
        );
      });
    });

    describe('getMaterialExportDetail', () => {
      it('should return NewMaterialExportDetail for default MaterialExportDetail initial value', () => {
        const formGroup = service.createMaterialExportDetailFormGroup(sampleWithNewData);

        const materialExportDetail = service.getMaterialExportDetail(formGroup) as any;

        expect(materialExportDetail).toMatchObject(sampleWithNewData);
      });

      it('should return NewMaterialExportDetail for empty MaterialExportDetail initial value', () => {
        const formGroup = service.createMaterialExportDetailFormGroup();

        const materialExportDetail = service.getMaterialExportDetail(formGroup) as any;

        expect(materialExportDetail).toMatchObject({});
      });

      it('should return IMaterialExportDetail', () => {
        const formGroup = service.createMaterialExportDetailFormGroup(sampleWithRequiredData);

        const materialExportDetail = service.getMaterialExportDetail(formGroup) as any;

        expect(materialExportDetail).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMaterialExportDetail should not enable id FormControl', () => {
        const formGroup = service.createMaterialExportDetailFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMaterialExportDetail should disable id FormControl', () => {
        const formGroup = service.createMaterialExportDetailFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
