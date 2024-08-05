import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMaterialExport } from 'app/entities/material-export/material-export.model';
import { MaterialExportService } from 'app/entities/material-export/service/material-export.service';
import { IMaterial } from 'app/entities/material/material.model';
import { MaterialService } from 'app/entities/material/service/material.service';
import { MaterialExportDetailService } from '../service/material-export-detail.service';
import { IMaterialExportDetail } from '../material-export-detail.model';
import { MaterialExportDetailFormService, MaterialExportDetailFormGroup } from './material-export-detail-form.service';

@Component({
  standalone: true,
  selector: 'jhi-material-export-detail-update',
  templateUrl: './material-export-detail-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MaterialExportDetailUpdateComponent implements OnInit {
  isSaving = false;
  materialExportDetail: IMaterialExportDetail | null = null;

  materialExportsSharedCollection: IMaterialExport[] = [];
  materialsSharedCollection: IMaterial[] = [];

  protected materialExportDetailService = inject(MaterialExportDetailService);
  protected materialExportDetailFormService = inject(MaterialExportDetailFormService);
  protected materialExportService = inject(MaterialExportService);
  protected materialService = inject(MaterialService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MaterialExportDetailFormGroup = this.materialExportDetailFormService.createMaterialExportDetailFormGroup();

  compareMaterialExport = (o1: IMaterialExport | null, o2: IMaterialExport | null): boolean =>
    this.materialExportService.compareMaterialExport(o1, o2);

  compareMaterial = (o1: IMaterial | null, o2: IMaterial | null): boolean => this.materialService.compareMaterial(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ materialExportDetail }) => {
      this.materialExportDetail = materialExportDetail;
      if (materialExportDetail) {
        this.updateForm(materialExportDetail);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const materialExportDetail = this.materialExportDetailFormService.getMaterialExportDetail(this.editForm);
    if (materialExportDetail.id !== null) {
      this.subscribeToSaveResponse(this.materialExportDetailService.update(materialExportDetail));
    } else {
      this.subscribeToSaveResponse(this.materialExportDetailService.create(materialExportDetail));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMaterialExportDetail>>): void {
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

  protected updateForm(materialExportDetail: IMaterialExportDetail): void {
    this.materialExportDetail = materialExportDetail;
    this.materialExportDetailFormService.resetForm(this.editForm, materialExportDetail);

    this.materialExportsSharedCollection = this.materialExportService.addMaterialExportToCollectionIfMissing<IMaterialExport>(
      this.materialExportsSharedCollection,
      materialExportDetail.materialExport,
    );
    this.materialsSharedCollection = this.materialService.addMaterialToCollectionIfMissing<IMaterial>(
      this.materialsSharedCollection,
      materialExportDetail.material,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.materialExportService
      .query()
      .pipe(map((res: HttpResponse<IMaterialExport[]>) => res.body ?? []))
      .pipe(
        map((materialExports: IMaterialExport[]) =>
          this.materialExportService.addMaterialExportToCollectionIfMissing<IMaterialExport>(
            materialExports,
            this.materialExportDetail?.materialExport,
          ),
        ),
      )
      .subscribe((materialExports: IMaterialExport[]) => (this.materialExportsSharedCollection = materialExports));

    this.materialService
      .query()
      .pipe(map((res: HttpResponse<IMaterial[]>) => res.body ?? []))
      .pipe(
        map((materials: IMaterial[]) =>
          this.materialService.addMaterialToCollectionIfMissing<IMaterial>(materials, this.materialExportDetail?.material),
        ),
      )
      .subscribe((materials: IMaterial[]) => (this.materialsSharedCollection = materials));
  }
}
