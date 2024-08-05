import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProduct, NewProduct } from '../product.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProduct for edit and NewProductFormGroupInput for create.
 */
type ProductFormGroupInput = IProduct | PartialWithRequiredKeyOf<NewProduct>;

type ProductFormDefaults = Pick<NewProduct, 'id'>;

type ProductFormGroupContent = {
  id: FormControl<IProduct['id'] | NewProduct['id']>;
  name: FormControl<IProduct['name']>;
  code: FormControl<IProduct['code']>;
  unit: FormControl<IProduct['unit']>;
  description: FormControl<IProduct['description']>;
  weight: FormControl<IProduct['weight']>;
  height: FormControl<IProduct['height']>;
  width: FormControl<IProduct['width']>;
  length: FormControl<IProduct['length']>;
  imageUrl: FormControl<IProduct['imageUrl']>;
  type: FormControl<IProduct['type']>;
  color: FormControl<IProduct['color']>;
  cbm: FormControl<IProduct['cbm']>;
  price: FormControl<IProduct['price']>;
  construction: FormControl<IProduct['construction']>;
  masterPackingQty: FormControl<IProduct['masterPackingQty']>;
  masterNestCode: FormControl<IProduct['masterNestCode']>;
  innerQty: FormControl<IProduct['innerQty']>;
  packSize: FormControl<IProduct['packSize']>;
  nestCode: FormControl<IProduct['nestCode']>;
  countryOfOrigin: FormControl<IProduct['countryOfOrigin']>;
  vendorName: FormControl<IProduct['vendorName']>;
  numberOfSet: FormControl<IProduct['numberOfSet']>;
  priceFOB: FormControl<IProduct['priceFOB']>;
  qty40HC: FormControl<IProduct['qty40HC']>;
  d57Qty: FormControl<IProduct['d57Qty']>;
  category: FormControl<IProduct['category']>;
  labels: FormControl<IProduct['labels']>;
  planningNotes: FormControl<IProduct['planningNotes']>;
  factTag: FormControl<IProduct['factTag']>;
  packagingLength: FormControl<IProduct['packagingLength']>;
  packagingHeight: FormControl<IProduct['packagingHeight']>;
  packagingWidth: FormControl<IProduct['packagingWidth']>;
  setIdProduct: FormControl<IProduct['setIdProduct']>;
  parentProduct: FormControl<IProduct['parentProduct']>;
  material: FormControl<IProduct['material']>;
};

export type ProductFormGroup = FormGroup<ProductFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductFormService {
  createProductFormGroup(product: ProductFormGroupInput = { id: null }): ProductFormGroup {
    const productRawValue = {
      ...this.getFormDefaults(),
      ...product,
    };
    return new FormGroup<ProductFormGroupContent>({
      id: new FormControl(
        { value: productRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(productRawValue.name),
      code: new FormControl(productRawValue.code),
      unit: new FormControl(productRawValue.unit),
      description: new FormControl(productRawValue.description),
      weight: new FormControl(productRawValue.weight),
      height: new FormControl(productRawValue.height),
      width: new FormControl(productRawValue.width),
      length: new FormControl(productRawValue.length),
      imageUrl: new FormControl(productRawValue.imageUrl),
      type: new FormControl(productRawValue.type),
      color: new FormControl(productRawValue.color),
      cbm: new FormControl(productRawValue.cbm),
      price: new FormControl(productRawValue.price),
      construction: new FormControl(productRawValue.construction),
      masterPackingQty: new FormControl(productRawValue.masterPackingQty),
      masterNestCode: new FormControl(productRawValue.masterNestCode),
      innerQty: new FormControl(productRawValue.innerQty),
      packSize: new FormControl(productRawValue.packSize),
      nestCode: new FormControl(productRawValue.nestCode),
      countryOfOrigin: new FormControl(productRawValue.countryOfOrigin),
      vendorName: new FormControl(productRawValue.vendorName),
      numberOfSet: new FormControl(productRawValue.numberOfSet),
      priceFOB: new FormControl(productRawValue.priceFOB),
      qty40HC: new FormControl(productRawValue.qty40HC),
      d57Qty: new FormControl(productRawValue.d57Qty),
      category: new FormControl(productRawValue.category),
      labels: new FormControl(productRawValue.labels),
      planningNotes: new FormControl(productRawValue.planningNotes),
      factTag: new FormControl(productRawValue.factTag),
      packagingLength: new FormControl(productRawValue.packagingLength),
      packagingHeight: new FormControl(productRawValue.packagingHeight),
      packagingWidth: new FormControl(productRawValue.packagingWidth),
      setIdProduct: new FormControl(productRawValue.setIdProduct),
      parentProduct: new FormControl(productRawValue.parentProduct),
      material: new FormControl(productRawValue.material),
    });
  }

  getProduct(form: ProductFormGroup): IProduct | NewProduct {
    return form.getRawValue() as IProduct | NewProduct;
  }

  resetForm(form: ProductFormGroup, product: ProductFormGroupInput): void {
    const productRawValue = { ...this.getFormDefaults(), ...product };
    form.reset(
      {
        ...productRawValue,
        id: { value: productRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProductFormDefaults {
    return {
      id: null,
    };
  }
}
