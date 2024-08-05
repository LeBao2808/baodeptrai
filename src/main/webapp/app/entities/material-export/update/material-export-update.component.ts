import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IProductionSite } from 'app/entities/production-site/production-site.model';
import { ProductionSiteService } from 'app/entities/production-site/service/production-site.service';
import { MaterialExportService } from '../service/material-export.service';
import { IMaterialExport } from '../material-export.model';
import { MaterialExportFormService, MaterialExportFormGroup } from './material-export-form.service';

@Component({
  standalone: true,
  selector: 'jhi-material-export-update',
  templateUrl: './material-export-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MaterialExportUpdateComponent implements OnInit {
  isSaving = false;
  materialExport: IMaterialExport | null = null;

  usersSharedCollection: IUser[] = [];
  productionSitesSharedCollection: IProductionSite[] = [];

  protected materialExportService = inject(MaterialExportService);
  protected materialExportFormService = inject(MaterialExportFormService);
  protected userService = inject(UserService);
  protected productionSiteService = inject(ProductionSiteService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MaterialExportFormGroup = this.materialExportFormService.createMaterialExportFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareProductionSite = (o1: IProductionSite | null, o2: IProductionSite | null): boolean =>
    this.productionSiteService.compareProductionSite(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ materialExport }) => {
      this.materialExport = materialExport;
      if (materialExport) {
        this.updateForm(materialExport);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const materialExport = this.materialExportFormService.getMaterialExport(this.editForm);
    if (materialExport.id !== null) {
      this.subscribeToSaveResponse(this.materialExportService.update(materialExport));
    } else {
      this.subscribeToSaveResponse(this.materialExportService.create(materialExport));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMaterialExport>>): void {
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

  protected updateForm(materialExport: IMaterialExport): void {
    this.materialExport = materialExport;
    this.materialExportFormService.resetForm(this.editForm, materialExport);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
      this.usersSharedCollection,
      materialExport.createdByUser,
    );
    this.productionSitesSharedCollection = this.productionSiteService.addProductionSiteToCollectionIfMissing<IProductionSite>(
      this.productionSitesSharedCollection,
      materialExport.productionSite,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.materialExport?.createdByUser)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.productionSiteService
      .query()
      .pipe(map((res: HttpResponse<IProductionSite[]>) => res.body ?? []))
      .pipe(
        map((productionSites: IProductionSite[]) =>
          this.productionSiteService.addProductionSiteToCollectionIfMissing<IProductionSite>(
            productionSites,
            this.materialExport?.productionSite,
          ),
        ),
      )
      .subscribe((productionSites: IProductionSite[]) => (this.productionSitesSharedCollection = productionSites));
  }
}
