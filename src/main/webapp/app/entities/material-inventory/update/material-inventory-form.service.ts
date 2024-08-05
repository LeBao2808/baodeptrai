import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMaterialInventory, NewMaterialInventory } from '../material-inventory.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMaterialInventory for edit and NewMaterialInventoryFormGroupInput for create.
 */
type MaterialInventoryFormGroupInput = IMaterialInventory | PartialWithRequiredKeyOf<NewMaterialInventory>;

type MaterialInventoryFormDefaults = Pick<NewMaterialInventory, 'id'>;

type MaterialInventoryFormGroupContent = {
  id: FormControl<IMaterialInventory['id'] | NewMaterialInventory['id']>;
  quantityOnHand: FormControl<IMaterialInventory['quantityOnHand']>;
  inventoryMonth: FormControl<IMaterialInventory['inventoryMonth']>;
  inventoryYear: FormControl<IMaterialInventory['inventoryYear']>;
  type: FormControl<IMaterialInventory['type']>;
  price: FormControl<IMaterialInventory['price']>;
  material: FormControl<IMaterialInventory['material']>;
};

export type MaterialInventoryFormGroup = FormGroup<MaterialInventoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MaterialInventoryFormService {
  createMaterialInventoryFormGroup(materialInventory: MaterialInventoryFormGroupInput = { id: null }): MaterialInventoryFormGroup {
    const materialInventoryRawValue = {
      ...this.getFormDefaults(),
      ...materialInventory,
    };
    return new FormGroup<MaterialInventoryFormGroupContent>({
      id: new FormControl(
        { value: materialInventoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      quantityOnHand: new FormControl(materialInventoryRawValue.quantityOnHand),
      inventoryMonth: new FormControl(materialInventoryRawValue.inventoryMonth),
      inventoryYear: new FormControl(materialInventoryRawValue.inventoryYear),
      type: new FormControl(materialInventoryRawValue.type),
      price: new FormControl(materialInventoryRawValue.price),
      material: new FormControl(materialInventoryRawValue.material),
    });
  }

  getMaterialInventory(form: MaterialInventoryFormGroup): IMaterialInventory | NewMaterialInventory {
    return form.getRawValue() as IMaterialInventory | NewMaterialInventory;
  }

  resetForm(form: MaterialInventoryFormGroup, materialInventory: MaterialInventoryFormGroupInput): void {
    const materialInventoryRawValue = { ...this.getFormDefaults(), ...materialInventory };
    form.reset(
      {
        ...materialInventoryRawValue,
        id: { value: materialInventoryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MaterialInventoryFormDefaults {
    return {
      id: null,
    };
  }
}
