import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IQuantification } from 'app/entities/quantification/quantification.model';
import { QuantificationService } from 'app/entities/quantification/service/quantification.service';
import { IPlanning } from '../planning.model';
import { PlanningService } from '../service/planning.service';
import { PlanningFormService, PlanningFormGroup } from './planning-form.service';

@Component({
  standalone: true,
  selector: 'jhi-planning-update',
  templateUrl: './planning-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PlanningUpdateComponent implements OnInit {
  isSaving = false;
  planning: IPlanning | null = null;

  quantificationsSharedCollection: IQuantification[] = [];

  protected planningService = inject(PlanningService);
  protected planningFormService = inject(PlanningFormService);
  protected quantificationService = inject(QuantificationService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PlanningFormGroup = this.planningFormService.createPlanningFormGroup();

  compareQuantification = (o1: IQuantification | null, o2: IQuantification | null): boolean =>
    this.quantificationService.compareQuantification(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ planning }) => {
      this.planning = planning;
      if (planning) {
        this.updateForm(planning);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const planning = this.planningFormService.getPlanning(this.editForm);
    if (planning.id !== null) {
      this.subscribeToSaveResponse(this.planningService.update(planning));
    } else {
      this.subscribeToSaveResponse(this.planningService.create(planning));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlanning>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(planning: IPlanning): void {
    this.planning = planning;
    this.planningFormService.resetForm(this.editForm, planning);

    this.quantificationsSharedCollection = this.quantificationService.addQuantificationToCollectionIfMissing<IQuantification>(
      this.quantificationsSharedCollection,
      planning.quantification,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.quantificationService
      .query()
      .pipe(map((res: HttpResponse<IQuantification[]>) => res.body ?? []))
      .pipe(
        map((quantifications: IQuantification[]) =>
          this.quantificationService.addQuantificationToCollectionIfMissing<IQuantification>(
            quantifications,
            this.planning?.quantification,
          ),
        ),
      )
      .subscribe((quantifications: IQuantification[]) => (this.quantificationsSharedCollection = quantifications));
  }
}
