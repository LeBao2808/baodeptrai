import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IPlanning } from 'app/entities/planning/planning.model';
import { PlanningService } from 'app/entities/planning/service/planning.service';
import { MaterialReceiptService } from '../service/material-receipt.service';
import { IMaterialReceipt } from '../material-receipt.model';
import { MaterialReceiptFormService, MaterialReceiptFormGroup } from './material-receipt-form.service';

@Component({
  standalone: true,
  selector: 'jhi-material-receipt-update',
  templateUrl: './material-receipt-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MaterialReceiptUpdateComponent implements OnInit {
  isSaving = false;
  materialReceipt: IMaterialReceipt | null = null;

  usersSharedCollection: IUser[] = [];
  planningsSharedCollection: IPlanning[] = [];

  protected materialReceiptService = inject(MaterialReceiptService);
  protected materialReceiptFormService = inject(MaterialReceiptFormService);
  protected userService = inject(UserService);
  protected planningService = inject(PlanningService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MaterialReceiptFormGroup = this.materialReceiptFormService.createMaterialReceiptFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  comparePlanning = (o1: IPlanning | null, o2: IPlanning | null): boolean => this.planningService.comparePlanning(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ materialReceipt }) => {
      this.materialReceipt = materialReceipt;
      if (materialReceipt) {
        this.updateForm(materialReceipt);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const materialReceipt = this.materialReceiptFormService.getMaterialReceipt(this.editForm);
    if (materialReceipt.id !== null) {
      this.subscribeToSaveResponse(this.materialReceiptService.update(materialReceipt));
    } else {
      this.subscribeToSaveResponse(this.materialReceiptService.create(materialReceipt));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMaterialReceipt>>): void {
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

  protected updateForm(materialReceipt: IMaterialReceipt): void {
    this.materialReceipt = materialReceipt;
    this.materialReceiptFormService.resetForm(this.editForm, materialReceipt);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
      this.usersSharedCollection,
      materialReceipt.createdByUser,
    );
    this.planningsSharedCollection = this.planningService.addPlanningToCollectionIfMissing<IPlanning>(
      this.planningsSharedCollection,
      materialReceipt.quantificationOrder,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.materialReceipt?.createdByUser)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.planningService
      .query()
      .pipe(map((res: HttpResponse<IPlanning[]>) => res.body ?? []))
      .pipe(
        map((plannings: IPlanning[]) =>
          this.planningService.addPlanningToCollectionIfMissing<IPlanning>(plannings, this.materialReceipt?.quantificationOrder),
        ),
      )
      .subscribe((plannings: IPlanning[]) => (this.planningsSharedCollection = plannings));
  }
}
