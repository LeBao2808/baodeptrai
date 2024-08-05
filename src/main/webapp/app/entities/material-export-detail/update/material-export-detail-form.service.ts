import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMaterialExportDetail, NewMaterialExportDetail } from '../material-export-detail.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMaterialExportDetail for edit and NewMaterialExportDetailFormGroupInput for create.
 */
type MaterialExportDetailFormGroupInput = IMaterialExportDetail | PartialWithRequiredKeyOf<NewMaterialExportDetail>;

type MaterialExportDetailFormDefaults = Pick<NewMaterialExportDetail, 'id'>;

type MaterialExportDetailFormGroupContent = {
  id: FormControl<IMaterialExportDetail['id'] | NewMaterialExportDetail['id']>;
  note: FormControl<IMaterialExportDetail['note']>;
  exportPrice: FormControl<IMaterialExportDetail['exportPrice']>;
  quantity: FormControl<IMaterialExportDetail['quantity']>;
  materialExport: FormControl<IMaterialExportDetail['materialExport']>;
  material: FormControl<IMaterialExportDetail['material']>;
};

export type MaterialExportDetailFormGroup = FormGroup<MaterialExportDetailFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MaterialExportDetailFormService {
  createMaterialExportDetailFormGroup(
    materialExportDetail: MaterialExportDetailFormGroupInput = { id: null },
  ): MaterialExportDetailFormGroup {
    const materialExportDetailRawValue = {
      ...this.getFormDefaults(),
      ...materialExportDetail,
    };
    return new FormGroup<MaterialExportDetailFormGroupContent>({
      id: new FormControl(
        { value: materialExportDetailRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      note: new FormControl(materialExportDetailRawValue.note),
      exportPrice: new FormControl(materialExportDetailRawValue.exportPrice),
      quantity: new FormControl(materialExportDetailRawValue.quantity),
      materialExport: new FormControl(materialExportDetailRawValue.materialExport),
      material: new FormControl(materialExportDetailRawValue.material),
    });
  }

  getMaterialExportDetail(form: MaterialExportDetailFormGroup): IMaterialExportDetail | NewMaterialExportDetail {
    return form.getRawValue() as IMaterialExportDetail | NewMaterialExportDetail;
  }

  resetForm(form: MaterialExportDetailFormGroup, materialExportDetail: MaterialExportDetailFormGroupInput): void {
    const materialExportDetailRawValue = { ...this.getFormDefaults(), ...materialExportDetail };
    form.reset(
      {
        ...materialExportDetailRawValue,
        id: { value: materialExportDetailRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MaterialExportDetailFormDefaults {
    return {
      id: null,
    };
  }
}
