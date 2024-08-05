import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProductionSite, NewProductionSite } from '../production-site.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductionSite for edit and NewProductionSiteFormGroupInput for create.
 */
type ProductionSiteFormGroupInput = IProductionSite | PartialWithRequiredKeyOf<NewProductionSite>;

type ProductionSiteFormDefaults = Pick<NewProductionSite, 'id'>;

type ProductionSiteFormGroupContent = {
  id: FormControl<IProductionSite['id'] | NewProductionSite['id']>;
  name: FormControl<IProductionSite['name']>;
  address: FormControl<IProductionSite['address']>;
  phone: FormControl<IProductionSite['phone']>;
  productId: FormControl<IProductionSite['productId']>;
};

export type ProductionSiteFormGroup = FormGroup<ProductionSiteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductionSiteFormService {
  createProductionSiteFormGroup(productionSite: ProductionSiteFormGroupInput = { id: null }): ProductionSiteFormGroup {
    const productionSiteRawValue = {
      ...this.getFormDefaults(),
      ...productionSite,
    };
    return new FormGroup<ProductionSiteFormGroupContent>({
      id: new FormControl(
        { value: productionSiteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(productionSiteRawValue.name),
      address: new FormControl(productionSiteRawValue.address),
      phone: new FormControl(productionSiteRawValue.phone),
      productId: new FormControl(productionSiteRawValue.productId),
    });
  }

  getProductionSite(form: ProductionSiteFormGroup): IProductionSite | NewProductionSite {
    return form.getRawValue() as IProductionSite | NewProductionSite;
  }

  resetForm(form: ProductionSiteFormGroup, productionSite: ProductionSiteFormGroupInput): void {
    const productionSiteRawValue = { ...this.getFormDefaults(), ...productionSite };
    form.reset(
      {
        ...productionSiteRawValue,
        id: { value: productionSiteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProductionSiteFormDefaults {
    return {
      id: null,
    };
  }
}
