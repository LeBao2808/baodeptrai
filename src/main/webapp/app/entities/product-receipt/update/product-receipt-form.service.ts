import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IProductReceipt, NewProductReceipt } from '../product-receipt.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductReceipt for edit and NewProductReceiptFormGroupInput for create.
 */
type ProductReceiptFormGroupInput = IProductReceipt | PartialWithRequiredKeyOf<NewProductReceipt>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IProductReceipt | NewProductReceipt> = Omit<T, 'creationDate' | 'paymentDate' | 'receiptDate'> & {
  creationDate?: string | null;
  paymentDate?: string | null;
  receiptDate?: string | null;
};

type ProductReceiptFormRawValue = FormValueOf<IProductReceipt>;

type NewProductReceiptFormRawValue = FormValueOf<NewProductReceipt>;

type ProductReceiptFormDefaults = Pick<NewProductReceipt, 'id' | 'creationDate' | 'paymentDate' | 'receiptDate'>;

type ProductReceiptFormGroupContent = {
  id: FormControl<ProductReceiptFormRawValue['id'] | NewProductReceipt['id']>;
  creationDate: FormControl<ProductReceiptFormRawValue['creationDate']>;
  paymentDate: FormControl<ProductReceiptFormRawValue['paymentDate']>;
  receiptDate: FormControl<ProductReceiptFormRawValue['receiptDate']>;
  status: FormControl<ProductReceiptFormRawValue['status']>;
  code: FormControl<ProductReceiptFormRawValue['code']>;
  created: FormControl<ProductReceiptFormRawValue['created']>;
};

export type ProductReceiptFormGroup = FormGroup<ProductReceiptFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductReceiptFormService {
  createProductReceiptFormGroup(productReceipt: ProductReceiptFormGroupInput = { id: null }): ProductReceiptFormGroup {
    const productReceiptRawValue = this.convertProductReceiptToProductReceiptRawValue({
      ...this.getFormDefaults(),
      ...productReceipt,
    });
    return new FormGroup<ProductReceiptFormGroupContent>({
      id: new FormControl(
        { value: productReceiptRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      creationDate: new FormControl(productReceiptRawValue.creationDate),
      paymentDate: new FormControl(productReceiptRawValue.paymentDate),
      receiptDate: new FormControl(productReceiptRawValue.receiptDate),
      status: new FormControl(productReceiptRawValue.status),
      code: new FormControl(productReceiptRawValue.code),
      created: new FormControl(productReceiptRawValue.created),
    });
  }

  getProductReceipt(form: ProductReceiptFormGroup): IProductReceipt | NewProductReceipt {
    return this.convertProductReceiptRawValueToProductReceipt(
      form.getRawValue() as ProductReceiptFormRawValue | NewProductReceiptFormRawValue,
    );
  }

  resetForm(form: ProductReceiptFormGroup, productReceipt: ProductReceiptFormGroupInput): void {
    const productReceiptRawValue = this.convertProductReceiptToProductReceiptRawValue({ ...this.getFormDefaults(), ...productReceipt });
    form.reset(
      {
        ...productReceiptRawValue,
        id: { value: productReceiptRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProductReceiptFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      creationDate: currentTime,
      paymentDate: currentTime,
      receiptDate: currentTime,
    };
  }

  private convertProductReceiptRawValueToProductReceipt(
    rawProductReceipt: ProductReceiptFormRawValue | NewProductReceiptFormRawValue,
  ): IProductReceipt | NewProductReceipt {
    return {
      ...rawProductReceipt,
      creationDate: dayjs(rawProductReceipt.creationDate, DATE_TIME_FORMAT),
      paymentDate: dayjs(rawProductReceipt.paymentDate, DATE_TIME_FORMAT),
      receiptDate: dayjs(rawProductReceipt.receiptDate, DATE_TIME_FORMAT),
    };
  }

  private convertProductReceiptToProductReceiptRawValue(
    productReceipt: IProductReceipt | (Partial<NewProductReceipt> & ProductReceiptFormDefaults),
  ): ProductReceiptFormRawValue | PartialWithRequiredKeyOf<NewProductReceiptFormRawValue> {
    return {
      ...productReceipt,
      creationDate: productReceipt.creationDate ? productReceipt.creationDate.format(DATE_TIME_FORMAT) : undefined,
      paymentDate: productReceipt.paymentDate ? productReceipt.paymentDate.format(DATE_TIME_FORMAT) : undefined,
      receiptDate: productReceipt.receiptDate ? productReceipt.receiptDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
