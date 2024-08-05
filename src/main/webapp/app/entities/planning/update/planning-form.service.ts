import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPlanning, NewPlanning } from '../planning.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPlanning for edit and NewPlanningFormGroupInput for create.
 */
type PlanningFormGroupInput = IPlanning | PartialWithRequiredKeyOf<NewPlanning>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPlanning | NewPlanning> = Omit<T, 'orderCreationDate'> & {
  orderCreationDate?: string | null;
};

type PlanningFormRawValue = FormValueOf<IPlanning>;

type NewPlanningFormRawValue = FormValueOf<NewPlanning>;

type PlanningFormDefaults = Pick<NewPlanning, 'id' | 'orderCreationDate'>;

type PlanningFormGroupContent = {
  id: FormControl<PlanningFormRawValue['id'] | NewPlanning['id']>;
  orderCreationDate: FormControl<PlanningFormRawValue['orderCreationDate']>;
  status: FormControl<PlanningFormRawValue['status']>;
  code: FormControl<PlanningFormRawValue['code']>;
  quantification: FormControl<PlanningFormRawValue['quantification']>;
};

export type PlanningFormGroup = FormGroup<PlanningFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PlanningFormService {
  createPlanningFormGroup(planning: PlanningFormGroupInput = { id: null }): PlanningFormGroup {
    const planningRawValue = this.convertPlanningToPlanningRawValue({
      ...this.getFormDefaults(),
      ...planning,
    });
    return new FormGroup<PlanningFormGroupContent>({
      id: new FormControl(
        { value: planningRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      orderCreationDate: new FormControl(planningRawValue.orderCreationDate),
      status: new FormControl(planningRawValue.status),
      code: new FormControl(planningRawValue.code),
      quantification: new FormControl(planningRawValue.quantification),
    });
  }

  getPlanning(form: PlanningFormGroup): IPlanning | NewPlanning {
    return this.convertPlanningRawValueToPlanning(form.getRawValue() as PlanningFormRawValue | NewPlanningFormRawValue);
  }

  resetForm(form: PlanningFormGroup, planning: PlanningFormGroupInput): void {
    const planningRawValue = this.convertPlanningToPlanningRawValue({ ...this.getFormDefaults(), ...planning });
    form.reset(
      {
        ...planningRawValue,
        id: { value: planningRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PlanningFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      orderCreationDate: currentTime,
    };
  }

  private convertPlanningRawValueToPlanning(rawPlanning: PlanningFormRawValue | NewPlanningFormRawValue): IPlanning | NewPlanning {
    return {
      ...rawPlanning,
      orderCreationDate: dayjs(rawPlanning.orderCreationDate, DATE_TIME_FORMAT),
    };
  }

  private convertPlanningToPlanningRawValue(
    planning: IPlanning | (Partial<NewPlanning> & PlanningFormDefaults),
  ): PlanningFormRawValue | PartialWithRequiredKeyOf<NewPlanningFormRawValue> {
    return {
      ...planning,
      orderCreationDate: planning.orderCreationDate ? planning.orderCreationDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
