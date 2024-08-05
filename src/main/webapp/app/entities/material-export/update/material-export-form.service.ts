import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMaterialExport, NewMaterialExport } from '../material-export.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMaterialExport for edit and NewMaterialExportFormGroupInput for create.
 */
type MaterialExportFormGroupInput = IMaterialExport | PartialWithRequiredKeyOf<NewMaterialExport>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMaterialExport | NewMaterialExport> = Omit<T, 'creationDate' | 'exportDate'> & {
  creationDate?: string | null;
  exportDate?: string | null;
};

type MaterialExportFormRawValue = FormValueOf<IMaterialExport>;

type NewMaterialExportFormRawValue = FormValueOf<NewMaterialExport>;

type MaterialExportFormDefaults = Pick<NewMaterialExport, 'id' | 'creationDate' | 'exportDate'>;

type MaterialExportFormGroupContent = {
  id: FormControl<MaterialExportFormRawValue['id'] | NewMaterialExport['id']>;
  creationDate: FormControl<MaterialExportFormRawValue['creationDate']>;
  exportDate: FormControl<MaterialExportFormRawValue['exportDate']>;
  status: FormControl<MaterialExportFormRawValue['status']>;
  code: FormControl<MaterialExportFormRawValue['code']>;
  createdByUser: FormControl<MaterialExportFormRawValue['createdByUser']>;
  productionSite: FormControl<MaterialExportFormRawValue['productionSite']>;
};

export type MaterialExportFormGroup = FormGroup<MaterialExportFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MaterialExportFormService {
  createMaterialExportFormGroup(materialExport: MaterialExportFormGroupInput = { id: null }): MaterialExportFormGroup {
    const materialExportRawValue = this.convertMaterialExportToMaterialExportRawValue({
      ...this.getFormDefaults(),
      ...materialExport,
    });
    return new FormGroup<MaterialExportFormGroupContent>({
      id: new FormControl(
        { value: materialExportRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      creationDate: new FormControl(materialExportRawValue.creationDate),
      exportDate: new FormControl(materialExportRawValue.exportDate),
      status: new FormControl(materialExportRawValue.status),
      code: new FormControl(materialExportRawValue.code),
      createdByUser: new FormControl(materialExportRawValue.createdByUser),
      productionSite: new FormControl(materialExportRawValue.productionSite),
    });
  }

  getMaterialExport(form: MaterialExportFormGroup): IMaterialExport | NewMaterialExport {
    return this.convertMaterialExportRawValueToMaterialExport(
      form.getRawValue() as MaterialExportFormRawValue | NewMaterialExportFormRawValue,
    );
  }

  resetForm(form: MaterialExportFormGroup, materialExport: MaterialExportFormGroupInput): void {
    const materialExportRawValue = this.convertMaterialExportToMaterialExportRawValue({ ...this.getFormDefaults(), ...materialExport });
    form.reset(
      {
        ...materialExportRawValue,
        id: { value: materialExportRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MaterialExportFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      creationDate: currentTime,
      exportDate: currentTime,
    };
  }

  private convertMaterialExportRawValueToMaterialExport(
    rawMaterialExport: MaterialExportFormRawValue | NewMaterialExportFormRawValue,
  ): IMaterialExport | NewMaterialExport {
    return {
      ...rawMaterialExport,
      creationDate: dayjs(rawMaterialExport.creationDate, DATE_TIME_FORMAT),
      exportDate: dayjs(rawMaterialExport.exportDate, DATE_TIME_FORMAT),
    };
  }

  private convertMaterialExportToMaterialExportRawValue(
    materialExport: IMaterialExport | (Partial<NewMaterialExport> & MaterialExportFormDefaults),
  ): MaterialExportFormRawValue | PartialWithRequiredKeyOf<NewMaterialExportFormRawValue> {
    return {
      ...materialExport,
      creationDate: materialExport.creationDate ? materialExport.creationDate.format(DATE_TIME_FORMAT) : undefined,
      exportDate: materialExport.exportDate ? materialExport.exportDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
