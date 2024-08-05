import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IProductOrderDetail, NewProductOrderDetail } from '../product-order-detail.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductOrderDetail for edit and NewProductOrderDetailFormGroupInput for create.
 */
type ProductOrderDetailFormGroupInput = IProductOrderDetail | PartialWithRequiredKeyOf<NewProductOrderDetail>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IProductOrderDetail | NewProductOrderDetail> = Omit<T, 'orderCreationDate'> & {
  orderCreationDate?: string | null;
};

type ProductOrderDetailFormRawValue = FormValueOf<IProductOrderDetail>;

type NewProductOrderDetailFormRawValue = FormValueOf<NewProductOrderDetail>;

type ProductOrderDetailFormDefaults = Pick<NewProductOrderDetail, 'id' | 'orderCreationDate'>;

type ProductOrderDetailFormGroupContent = {
  id: FormControl<ProductOrderDetailFormRawValue['id'] | NewProductOrderDetail['id']>;
  orderCreationDate: FormControl<ProductOrderDetailFormRawValue['orderCreationDate']>;
  quantity: FormControl<ProductOrderDetailFormRawValue['quantity']>;
  unitPrice: FormControl<ProductOrderDetailFormRawValue['unitPrice']>;
  order: FormControl<ProductOrderDetailFormRawValue['order']>;
  product: FormControl<ProductOrderDetailFormRawValue['product']>;
};

export type ProductOrderDetailFormGroup = FormGroup<ProductOrderDetailFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductOrderDetailFormService {
  createProductOrderDetailFormGroup(productOrderDetail: ProductOrderDetailFormGroupInput = { id: null }): ProductOrderDetailFormGroup {
    const productOrderDetailRawValue = this.convertProductOrderDetailToProductOrderDetailRawValue({
      ...this.getFormDefaults(),
      ...productOrderDetail,
    });
    return new FormGroup<ProductOrderDetailFormGroupContent>({
      id: new FormControl(
        { value: productOrderDetailRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      orderCreationDate: new FormControl(productOrderDetailRawValue.orderCreationDate),
      quantity: new FormControl(productOrderDetailRawValue.quantity),
      unitPrice: new FormControl(productOrderDetailRawValue.unitPrice),
      order: new FormControl(productOrderDetailRawValue.order),
      product: new FormControl(productOrderDetailRawValue.product),
    });
  }

  getProductOrderDetail(form: ProductOrderDetailFormGroup): IProductOrderDetail | NewProductOrderDetail {
    return this.convertProductOrderDetailRawValueToProductOrderDetail(
      form.getRawValue() as ProductOrderDetailFormRawValue | NewProductOrderDetailFormRawValue,
    );
  }

  resetForm(form: ProductOrderDetailFormGroup, productOrderDetail: ProductOrderDetailFormGroupInput): void {
    const productOrderDetailRawValue = this.convertProductOrderDetailToProductOrderDetailRawValue({
      ...this.getFormDefaults(),
      ...productOrderDetail,
    });
    form.reset(
      {
        ...productOrderDetailRawValue,
        id: { value: productOrderDetailRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProductOrderDetailFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      orderCreationDate: currentTime,
    };
  }

  private convertProductOrderDetailRawValueToProductOrderDetail(
    rawProductOrderDetail: ProductOrderDetailFormRawValue | NewProductOrderDetailFormRawValue,
  ): IProductOrderDetail | NewProductOrderDetail {
    return {
      ...rawProductOrderDetail,
      orderCreationDate: dayjs(rawProductOrderDetail.orderCreationDate, DATE_TIME_FORMAT),
    };
  }

  private convertProductOrderDetailToProductOrderDetailRawValue(
    productOrderDetail: IProductOrderDetail | (Partial<NewProductOrderDetail> & ProductOrderDetailFormDefaults),
  ): ProductOrderDetailFormRawValue | PartialWithRequiredKeyOf<NewProductOrderDetailFormRawValue> {
    return {
      ...productOrderDetail,
      orderCreationDate: productOrderDetail.orderCreationDate ? productOrderDetail.orderCreationDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
