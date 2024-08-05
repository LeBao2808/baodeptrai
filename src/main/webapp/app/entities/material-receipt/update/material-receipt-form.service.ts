import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMaterialReceipt, NewMaterialReceipt } from '../material-receipt.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMaterialReceipt for edit and NewMaterialReceiptFormGroupInput for create.
 */
type MaterialReceiptFormGroupInput = IMaterialReceipt | PartialWithRequiredKeyOf<NewMaterialReceipt>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMaterialReceipt | NewMaterialReceipt> = Omit<T, 'creationDate' | 'paymentDate' | 'receiptDate'> & {
  creationDate?: string | null;
  paymentDate?: string | null;
  receiptDate?: string | null;
};

type MaterialReceiptFormRawValue = FormValueOf<IMaterialReceipt>;

type NewMaterialReceiptFormRawValue = FormValueOf<NewMaterialReceipt>;

type MaterialReceiptFormDefaults = Pick<NewMaterialReceipt, 'id' | 'creationDate' | 'paymentDate' | 'receiptDate'>;

type MaterialReceiptFormGroupContent = {
  id: FormControl<MaterialReceiptFormRawValue['id'] | NewMaterialReceipt['id']>;
  creationDate: FormControl<MaterialReceiptFormRawValue['creationDate']>;
  paymentDate: FormControl<MaterialReceiptFormRawValue['paymentDate']>;
  receiptDate: FormControl<MaterialReceiptFormRawValue['receiptDate']>;
  status: FormControl<MaterialReceiptFormRawValue['status']>;
  code: FormControl<MaterialReceiptFormRawValue['code']>;
  createdByUser: FormControl<MaterialReceiptFormRawValue['createdByUser']>;
  quantificationOrder: FormControl<MaterialReceiptFormRawValue['quantificationOrder']>;
};

export type MaterialReceiptFormGroup = FormGroup<MaterialReceiptFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MaterialReceiptFormService {
  createMaterialReceiptFormGroup(materialReceipt: MaterialReceiptFormGroupInput = { id: null }): MaterialReceiptFormGroup {
    const materialReceiptRawValue = this.convertMaterialReceiptToMaterialReceiptRawValue({
      ...this.getFormDefaults(),
      ...materialReceipt,
    });
    return new FormGroup<MaterialReceiptFormGroupContent>({
      id: new FormControl(
        { value: materialReceiptRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      creationDate: new FormControl(materialReceiptRawValue.creationDate),
      paymentDate: new FormControl(materialReceiptRawValue.paymentDate),
      receiptDate: new FormControl(materialReceiptRawValue.receiptDate),
      status: new FormControl(materialReceiptRawValue.status),
      code: new FormControl(materialReceiptRawValue.code),
      createdByUser: new FormControl(materialReceiptRawValue.createdByUser),
      quantificationOrder: new FormControl(materialReceiptRawValue.quantificationOrder),
    });
  }

  getMaterialReceipt(form: MaterialReceiptFormGroup): IMaterialReceipt | NewMaterialReceipt {
    return this.convertMaterialReceiptRawValueToMaterialReceipt(
      form.getRawValue() as MaterialReceiptFormRawValue | NewMaterialReceiptFormRawValue,
    );
  }

  resetForm(form: MaterialReceiptFormGroup, materialReceipt: MaterialReceiptFormGroupInput): void {
    const materialReceiptRawValue = this.convertMaterialReceiptToMaterialReceiptRawValue({ ...this.getFormDefaults(), ...materialReceipt });
    form.reset(
      {
        ...materialReceiptRawValue,
        id: { value: materialReceiptRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MaterialReceiptFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      creationDate: currentTime,
      paymentDate: currentTime,
      receiptDate: currentTime,
    };
  }

  private convertMaterialReceiptRawValueToMaterialReceipt(
    rawMaterialReceipt: MaterialReceiptFormRawValue | NewMaterialReceiptFormRawValue,
  ): IMaterialReceipt | NewMaterialReceipt {
    return {
      ...rawMaterialReceipt,
      creationDate: dayjs(rawMaterialReceipt.creationDate, DATE_TIME_FORMAT),
      paymentDate: dayjs(rawMaterialReceipt.paymentDate, DATE_TIME_FORMAT),
      receiptDate: dayjs(rawMaterialReceipt.receiptDate, DATE_TIME_FORMAT),
    };
  }

  private convertMaterialReceiptToMaterialReceiptRawValue(
    materialReceipt: IMaterialReceipt | (Partial<NewMaterialReceipt> & MaterialReceiptFormDefaults),
  ): MaterialReceiptFormRawValue | PartialWithRequiredKeyOf<NewMaterialReceiptFormRawValue> {
    return {
      ...materialReceipt,
      creationDate: materialReceipt.creationDate ? materialReceipt.creationDate.format(DATE_TIME_FORMAT) : undefined,
      paymentDate: materialReceipt.paymentDate ? materialReceipt.paymentDate.format(DATE_TIME_FORMAT) : undefined,
      receiptDate: materialReceipt.receiptDate ? materialReceipt.receiptDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
