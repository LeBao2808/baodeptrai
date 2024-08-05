import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IQuantification, NewQuantification } from '../quantification.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IQuantification for edit and NewQuantificationFormGroupInput for create.
 */
type QuantificationFormGroupInput = IQuantification | PartialWithRequiredKeyOf<NewQuantification>;

type QuantificationFormDefaults = Pick<NewQuantification, 'id'>;

type QuantificationFormGroupContent = {
  id: FormControl<IQuantification['id'] | NewQuantification['id']>;
  quantity: FormControl<IQuantification['quantity']>;
  product: FormControl<IQuantification['product']>;
  material: FormControl<IQuantification['material']>;
};

export type QuantificationFormGroup = FormGroup<QuantificationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class QuantificationFormService {
  createQuantificationFormGroup(quantification: QuantificationFormGroupInput = { id: null }): QuantificationFormGroup {
    const quantificationRawValue = {
      ...this.getFormDefaults(),
      ...quantification,
    };
    return new FormGroup<QuantificationFormGroupContent>({
      id: new FormControl(
        { value: quantificationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      quantity: new FormControl(quantificationRawValue.quantity),
      product: new FormControl(quantificationRawValue.product),
      material: new FormControl(quantificationRawValue.material),
    });
  }

  getQuantification(form: QuantificationFormGroup): IQuantification | NewQuantification {
    return form.getRawValue() as IQuantification | NewQuantification;
  }

  resetForm(form: QuantificationFormGroup, quantification: QuantificationFormGroupInput): void {
    const quantificationRawValue = { ...this.getFormDefaults(), ...quantification };
    form.reset(
      {
        ...quantificationRawValue,
        id: { value: quantificationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): QuantificationFormDefaults {
    return {
      id: null,
    };
  }
}
