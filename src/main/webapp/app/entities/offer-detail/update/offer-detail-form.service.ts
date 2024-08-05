import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOfferDetail, NewOfferDetail } from '../offer-detail.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOfferDetail for edit and NewOfferDetailFormGroupInput for create.
 */
type OfferDetailFormGroupInput = IOfferDetail | PartialWithRequiredKeyOf<NewOfferDetail>;

type OfferDetailFormDefaults = Pick<NewOfferDetail, 'id'>;

type OfferDetailFormGroupContent = {
  id: FormControl<IOfferDetail['id'] | NewOfferDetail['id']>;
  feedback: FormControl<IOfferDetail['feedback']>;
  product: FormControl<IOfferDetail['product']>;
  offer: FormControl<IOfferDetail['offer']>;
};

export type OfferDetailFormGroup = FormGroup<OfferDetailFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OfferDetailFormService {
  createOfferDetailFormGroup(offerDetail: OfferDetailFormGroupInput = { id: null }): OfferDetailFormGroup {
    const offerDetailRawValue = {
      ...this.getFormDefaults(),
      ...offerDetail,
    };
    return new FormGroup<OfferDetailFormGroupContent>({
      id: new FormControl(
        { value: offerDetailRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      feedback: new FormControl(offerDetailRawValue.feedback),
      product: new FormControl(offerDetailRawValue.product),
      offer: new FormControl(offerDetailRawValue.offer),
    });
  }

  getOfferDetail(form: OfferDetailFormGroup): IOfferDetail | NewOfferDetail {
    return form.getRawValue() as IOfferDetail | NewOfferDetail;
  }

  resetForm(form: OfferDetailFormGroup, offerDetail: OfferDetailFormGroupInput): void {
    const offerDetailRawValue = { ...this.getFormDefaults(), ...offerDetail };
    form.reset(
      {
        ...offerDetailRawValue,
        id: { value: offerDetailRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): OfferDetailFormDefaults {
    return {
      id: null,
    };
  }
}
