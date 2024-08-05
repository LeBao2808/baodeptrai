import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProductReceiptDetail, NewProductReceiptDetail } from '../product-receipt-detail.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductReceiptDetail for edit and NewProductReceiptDetailFormGroupInput for create.
 */
type ProductReceiptDetailFormGroupInput = IProductReceiptDetail | PartialWithRequiredKeyOf<NewProductReceiptDetail>;

type ProductReceiptDetailFormDefaults = Pick<NewProductReceiptDetail, 'id'>;

type ProductReceiptDetailFormGroupContent = {
  id: FormControl<IProductReceiptDetail['id'] | NewProductReceiptDetail['id']>;
  note: FormControl<IProductReceiptDetail['note']>;
  quantity: FormControl<IProductReceiptDetail['quantity']>;
  product: FormControl<IProductReceiptDetail['product']>;
  receipt: FormControl<IProductReceiptDetail['receipt']>;
};

export type ProductReceiptDetailFormGroup = FormGroup<ProductReceiptDetailFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductReceiptDetailFormService {
  createProductReceiptDetailFormGroup(
    productReceiptDetail: ProductReceiptDetailFormGroupInput = { id: null },
  ): ProductReceiptDetailFormGroup {
    const productReceiptDetailRawValue = {
      ...this.getFormDefaults(),
      ...productReceiptDetail,
    };
    return new FormGroup<ProductReceiptDetailFormGroupContent>({
      id: new FormControl(
        { value: productReceiptDetailRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      note: new FormControl(productReceiptDetailRawValue.note),
      quantity: new FormControl(productReceiptDetailRawValue.quantity),
      product: new FormControl(productReceiptDetailRawValue.product),
      receipt: new FormControl(productReceiptDetailRawValue.receipt),
    });
  }

  getProductReceiptDetail(form: ProductReceiptDetailFormGroup): IProductReceiptDetail | NewProductReceiptDetail {
    return form.getRawValue() as IProductReceiptDetail | NewProductReceiptDetail;
  }

  resetForm(form: ProductReceiptDetailFormGroup, productReceiptDetail: ProductReceiptDetailFormGroupInput): void {
    const productReceiptDetailRawValue = { ...this.getFormDefaults(), ...productReceiptDetail };
    form.reset(
      {
        ...productReceiptDetailRawValue,
        id: { value: productReceiptDetailRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProductReceiptDetailFormDefaults {
    return {
      id: null,
    };
  }
}
