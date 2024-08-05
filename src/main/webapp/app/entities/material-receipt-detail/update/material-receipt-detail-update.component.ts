import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMaterial } from 'app/entities/material/material.model';
import { MaterialService } from 'app/entities/material/service/material.service';
import { IMaterialReceipt } from 'app/entities/material-receipt/material-receipt.model';
import { MaterialReceiptService } from 'app/entities/material-receipt/service/material-receipt.service';
import { MaterialReceiptDetailService } from '../service/material-receipt-detail.service';
import { IMaterialReceiptDetail } from '../material-receipt-detail.model';
import { MaterialReceiptDetailFormService, MaterialReceiptDetailFormGroup } from './material-receipt-detail-form.service';

@Component({
  standalone: true,
  selector: 'jhi-material-receipt-detail-update',
  templateUrl: './material-receipt-detail-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MaterialReceiptDetailUpdateComponent implements OnInit {
  isSaving = false;
  materialReceiptDetail: IMaterialReceiptDetail | null = null;

  materialsSharedCollection: IMaterial[] = [];
  materialReceiptsSharedCollection: IMaterialReceipt[] = [];

  protected materialReceiptDetailService = inject(MaterialReceiptDetailService);
  protected materialReceiptDetailFormService = inject(MaterialReceiptDetailFormService);
  protected materialService = inject(MaterialService);
  protected materialReceiptService = inject(MaterialReceiptService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MaterialReceiptDetailFormGroup = this.materialReceiptDetailFormService.createMaterialReceiptDetailFormGroup();

  compareMaterial = (o1: IMaterial | null, o2: IMaterial | null): boolean => this.materialService.compareMaterial(o1, o2);

  compareMaterialReceipt = (o1: IMaterialReceipt | null, o2: IMaterialReceipt | null): boolean =>
    this.materialReceiptService.compareMaterialReceipt(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ materialReceiptDetail }) => {
      this.materialReceiptDetail = materialReceiptDetail;
      if (materialReceiptDetail) {
        this.updateForm(materialReceiptDetail);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const materialReceiptDetail = this.materialReceiptDetailFormService.getMaterialReceiptDetail(this.editForm);
    if (materialReceiptDetail.id !== null) {
      this.subscribeToSaveResponse(this.materialReceiptDetailService.update(materialReceiptDetail));
    } else {
      this.subscribeToSaveResponse(this.materialReceiptDetailService.create(materialReceiptDetail));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMaterialReceiptDetail>>): void {
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

  protected updateForm(materialReceiptDetail: IMaterialReceiptDetail): void {
    this.materialReceiptDetail = materialReceiptDetail;
    this.materialReceiptDetailFormService.resetForm(this.editForm, materialReceiptDetail);

    this.materialsSharedCollection = this.materialService.addMaterialToCollectionIfMissing<IMaterial>(
      this.materialsSharedCollection,
      materialReceiptDetail.material,
    );
    this.materialReceiptsSharedCollection = this.materialReceiptService.addMaterialReceiptToCollectionIfMissing<IMaterialReceipt>(
      this.materialReceiptsSharedCollection,
      materialReceiptDetail.receipt,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.materialService
      .query()
      .pipe(map((res: HttpResponse<IMaterial[]>) => res.body ?? []))
      .pipe(
        map((materials: IMaterial[]) =>
          this.materialService.addMaterialToCollectionIfMissing<IMaterial>(materials, this.materialReceiptDetail?.material),
        ),
      )
      .subscribe((materials: IMaterial[]) => (this.materialsSharedCollection = materials));

    this.materialReceiptService
      .query()
      .pipe(map((res: HttpResponse<IMaterialReceipt[]>) => res.body ?? []))
      .pipe(
        map((materialReceipts: IMaterialReceipt[]) =>
          this.materialReceiptService.addMaterialReceiptToCollectionIfMissing<IMaterialReceipt>(
            materialReceipts,
            this.materialReceiptDetail?.receipt,
          ),
        ),
      )
      .subscribe((materialReceipts: IMaterialReceipt[]) => (this.materialReceiptsSharedCollection = materialReceipts));
  }
}
