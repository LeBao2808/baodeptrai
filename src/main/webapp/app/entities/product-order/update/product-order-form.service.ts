import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IProductOrder, NewProductOrder } from '../product-order.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductOrder for edit and NewProductOrderFormGroupInput for create.
 */
type ProductOrderFormGroupInput = IProductOrder | PartialWithRequiredKeyOf<NewProductOrder>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IProductOrder | NewProductOrder> = Omit<T, 'orderDate' | 'paymentDate' | 'warehouseReleaseDate'> & {
  orderDate?: string | null;
  paymentDate?: string | null;
  warehouseReleaseDate?: string | null;
};

type ProductOrderFormRawValue = FormValueOf<IProductOrder>;

type NewProductOrderFormRawValue = FormValueOf<NewProductOrder>;

type ProductOrderFormDefaults = Pick<NewProductOrder, 'id' | 'orderDate' | 'paymentDate' | 'warehouseReleaseDate'>;

type ProductOrderFormGroupContent = {
  id: FormControl<ProductOrderFormRawValue['id'] | NewProductOrder['id']>;
  paymentMethod: FormControl<ProductOrderFormRawValue['paymentMethod']>;
  note: FormControl<ProductOrderFormRawValue['note']>;
  status: FormControl<ProductOrderFormRawValue['status']>;
  orderDate: FormControl<ProductOrderFormRawValue['orderDate']>;
  paymentDate: FormControl<ProductOrderFormRawValue['paymentDate']>;
  warehouseReleaseDate: FormControl<ProductOrderFormRawValue['warehouseReleaseDate']>;
  code: FormControl<ProductOrderFormRawValue['code']>;
  quantificationOrder: FormControl<ProductOrderFormRawValue['quantificationOrder']>;
  customer: FormControl<ProductOrderFormRawValue['customer']>;
  createdByUser: FormControl<ProductOrderFormRawValue['createdByUser']>;
};

export type ProductOrderFormGroup = FormGroup<ProductOrderFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductOrderFormService {
  createProductOrderFormGroup(productOrder: ProductOrderFormGroupInput = { id: null }): ProductOrderFormGroup {
    const productOrderRawValue = this.convertProductOrderToProductOrderRawValue({
      ...this.getFormDefaults(),
      ...productOrder,
    });
    return new FormGroup<ProductOrderFormGroupContent>({
      id: new FormControl(
        { value: productOrderRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      paymentMethod: new FormControl(productOrderRawValue.paymentMethod),
      note: new FormControl(productOrderRawValue.note),
      status: new FormControl(productOrderRawValue.status),
      orderDate: new FormControl(productOrderRawValue.orderDate),
      paymentDate: new FormControl(productOrderRawValue.paymentDate),
      warehouseReleaseDate: new FormControl(productOrderRawValue.warehouseReleaseDate),
      code: new FormControl(productOrderRawValue.code),
      quantificationOrder: new FormControl(productOrderRawValue.quantificationOrder),
      customer: new FormControl(productOrderRawValue.customer),
      createdByUser: new FormControl(productOrderRawValue.createdByUser),
    });
  }

  getProductOrder(form: ProductOrderFormGroup): IProductOrder | NewProductOrder {
    return this.convertProductOrderRawValueToProductOrder(form.getRawValue() as ProductOrderFormRawValue | NewProductOrderFormRawValue);
  }

  resetForm(form: ProductOrderFormGroup, productOrder: ProductOrderFormGroupInput): void {
    const productOrderRawValue = this.convertProductOrderToProductOrderRawValue({ ...this.getFormDefaults(), ...productOrder });
    form.reset(
      {
        ...productOrderRawValue,
        id: { value: productOrderRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProductOrderFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      orderDate: currentTime,
      paymentDate: currentTime,
      warehouseReleaseDate: currentTime,
    };
  }

  private convertProductOrderRawValueToProductOrder(
    rawProductOrder: ProductOrderFormRawValue | NewProductOrderFormRawValue,
  ): IProductOrder | NewProductOrder {
    return {
      ...rawProductOrder,
      orderDate: dayjs(rawProductOrder.orderDate, DATE_TIME_FORMAT),
      paymentDate: dayjs(rawProductOrder.paymentDate, DATE_TIME_FORMAT),
      warehouseReleaseDate: dayjs(rawProductOrder.warehouseReleaseDate, DATE_TIME_FORMAT),
    };
  }

  private convertProductOrderToProductOrderRawValue(
    productOrder: IProductOrder | (Partial<NewProductOrder> & ProductOrderFormDefaults),
  ): ProductOrderFormRawValue | PartialWithRequiredKeyOf<NewProductOrderFormRawValue> {
    return {
      ...productOrder,
      orderDate: productOrder.orderDate ? productOrder.orderDate.format(DATE_TIME_FORMAT) : undefined,
      paymentDate: productOrder.paymentDate ? productOrder.paymentDate.format(DATE_TIME_FORMAT) : undefined,
      warehouseReleaseDate: productOrder.warehouseReleaseDate ? productOrder.warehouseReleaseDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
