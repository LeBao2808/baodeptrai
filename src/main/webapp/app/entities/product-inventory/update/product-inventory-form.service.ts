import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProductInventory, NewProductInventory } from '../product-inventory.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductInventory for edit and NewProductInventoryFormGroupInput for create.
 */
type ProductInventoryFormGroupInput = IProductInventory | PartialWithRequiredKeyOf<NewProductInventory>;

type ProductInventoryFormDefaults = Pick<NewProductInventory, 'id'>;

type ProductInventoryFormGroupContent = {
  id: FormControl<IProductInventory['id'] | NewProductInventory['id']>;
  quantityOnHand: FormControl<IProductInventory['quantityOnHand']>;
  inventoryMonth: FormControl<IProductInventory['inventoryMonth']>;
  inventoryYear: FormControl<IProductInventory['inventoryYear']>;
  type: FormControl<IProductInventory['type']>;
  price: FormControl<IProductInventory['price']>;
  product: FormControl<IProductInventory['product']>;
};

export type ProductInventoryFormGroup = FormGroup<ProductInventoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductInventoryFormService {
  createProductInventoryFormGroup(productInventory: ProductInventoryFormGroupInput = { id: null }): ProductInventoryFormGroup {
    const productInventoryRawValue = {
      ...this.getFormDefaults(),
      ...productInventory,
    };
    return new FormGroup<ProductInventoryFormGroupContent>({
      id: new FormControl(
        { value: productInventoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      quantityOnHand: new FormControl(productInventoryRawValue.quantityOnHand),
      inventoryMonth: new FormControl(productInventoryRawValue.inventoryMonth),
      inventoryYear: new FormControl(productInventoryRawValue.inventoryYear),
      type: new FormControl(productInventoryRawValue.type),
      price: new FormControl(productInventoryRawValue.price),
      product: new FormControl(productInventoryRawValue.product),
    });
  }

  getProductInventory(form: ProductInventoryFormGroup): IProductInventory | NewProductInventory {
    return form.getRawValue() as IProductInventory | NewProductInventory;
  }

  resetForm(form: ProductInventoryFormGroup, productInventory: ProductInventoryFormGroupInput): void {
    const productInventoryRawValue = { ...this.getFormDefaults(), ...productInventory };
    form.reset(
      {
        ...productInventoryRawValue,
        id: { value: productInventoryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProductInventoryFormDefaults {
    return {
      id: null,
    };
  }
}
