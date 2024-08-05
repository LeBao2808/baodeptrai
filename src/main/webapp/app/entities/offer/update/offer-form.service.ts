import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IOffer, NewOffer } from '../offer.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOffer for edit and NewOfferFormGroupInput for create.
 */
type OfferFormGroupInput = IOffer | PartialWithRequiredKeyOf<NewOffer>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IOffer | NewOffer> = Omit<T, 'date'> & {
  date?: string | null;
};

type OfferFormRawValue = FormValueOf<IOffer>;

type NewOfferFormRawValue = FormValueOf<NewOffer>;

type OfferFormDefaults = Pick<NewOffer, 'id' | 'date'>;

type OfferFormGroupContent = {
  id: FormControl<OfferFormRawValue['id'] | NewOffer['id']>;
  date: FormControl<OfferFormRawValue['date']>;
  status: FormControl<OfferFormRawValue['status']>;
  code: FormControl<OfferFormRawValue['code']>;
  customer: FormControl<OfferFormRawValue['customer']>;
  user: FormControl<OfferFormRawValue['user']>;
};

export type OfferFormGroup = FormGroup<OfferFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OfferFormService {
  createOfferFormGroup(offer: OfferFormGroupInput = { id: null }): OfferFormGroup {
    const offerRawValue = this.convertOfferToOfferRawValue({
      ...this.getFormDefaults(),
      ...offer,
    });
    return new FormGroup<OfferFormGroupContent>({
      id: new FormControl(
        { value: offerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      date: new FormControl(offerRawValue.date),
      status: new FormControl(offerRawValue.status),
      code: new FormControl(offerRawValue.code),
      customer: new FormControl(offerRawValue.customer),
      user: new FormControl(offerRawValue.user),
    });
  }

  getOffer(form: OfferFormGroup): IOffer | NewOffer {
    return this.convertOfferRawValueToOffer(form.getRawValue() as OfferFormRawValue | NewOfferFormRawValue);
  }

  resetForm(form: OfferFormGroup, offer: OfferFormGroupInput): void {
    const offerRawValue = this.convertOfferToOfferRawValue({ ...this.getFormDefaults(), ...offer });
    form.reset(
      {
        ...offerRawValue,
        id: { value: offerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): OfferFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      date: currentTime,
    };
  }

  private convertOfferRawValueToOffer(rawOffer: OfferFormRawValue | NewOfferFormRawValue): IOffer | NewOffer {
    return {
      ...rawOffer,
      date: dayjs(rawOffer.date, DATE_TIME_FORMAT),
    };
  }

  private convertOfferToOfferRawValue(
    offer: IOffer | (Partial<NewOffer> & OfferFormDefaults),
  ): OfferFormRawValue | PartialWithRequiredKeyOf<NewOfferFormRawValue> {
    return {
      ...offer,
      date: offer.date ? offer.date.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
