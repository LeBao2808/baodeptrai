import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMaterialReceiptDetail, NewMaterialReceiptDetail } from '../material-receipt-detail.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMaterialReceiptDetail for edit and NewMaterialReceiptDetailFormGroupInput for create.
 */
type MaterialReceiptDetailFormGroupInput = IMaterialReceiptDetail | PartialWithRequiredKeyOf<NewMaterialReceiptDetail>;

type MaterialReceiptDetailFormDefaults = Pick<NewMaterialReceiptDetail, 'id'>;

type MaterialReceiptDetailFormGroupContent = {
  id: FormControl<IMaterialReceiptDetail['id'] | NewMaterialReceiptDetail['id']>;
  note: FormControl<IMaterialReceiptDetail['note']>;
  importPrice: FormControl<IMaterialReceiptDetail['importPrice']>;
  quantity: FormControl<IMaterialReceiptDetail['quantity']>;
  material: FormControl<IMaterialReceiptDetail['material']>;
  receipt: FormControl<IMaterialReceiptDetail['receipt']>;
};

export type MaterialReceiptDetailFormGroup = FormGroup<MaterialReceiptDetailFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MaterialReceiptDetailFormService {
  createMaterialReceiptDetailFormGroup(
    materialReceiptDetail: MaterialReceiptDetailFormGroupInput = { id: null },
  ): MaterialReceiptDetailFormGroup {
    const materialReceiptDetailRawValue = {
      ...this.getFormDefaults(),
      ...materialReceiptDetail,
    };
    return new FormGroup<MaterialReceiptDetailFormGroupContent>({
      id: new FormControl(
        { value: materialReceiptDetailRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      note: new FormControl(materialReceiptDetailRawValue.note),
      importPrice: new FormControl(materialReceiptDetailRawValue.importPrice),
      quantity: new FormControl(materialReceiptDetailRawValue.quantity),
      material: new FormControl(materialReceiptDetailRawValue.material),
      receipt: new FormControl(materialReceiptDetailRawValue.receipt),
    });
  }

  getMaterialReceiptDetail(form: MaterialReceiptDetailFormGroup): IMaterialReceiptDetail | NewMaterialReceiptDetail {
    return form.getRawValue() as IMaterialReceiptDetail | NewMaterialReceiptDetail;
  }

  resetForm(form: MaterialReceiptDetailFormGroup, materialReceiptDetail: MaterialReceiptDetailFormGroupInput): void {
    const materialReceiptDetailRawValue = { ...this.getFormDefaults(), ...materialReceiptDetail };
    form.reset(
      {
        ...materialReceiptDetailRawValue,
        id: { value: materialReceiptDetailRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MaterialReceiptDetailFormDefaults {
    return {
      id: null,
    };
  }
}
