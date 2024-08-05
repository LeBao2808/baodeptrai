import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMaterial } from 'app/entities/material/material.model';
import { MaterialService } from 'app/entities/material/service/material.service';
import { IMaterialInventory } from '../material-inventory.model';
import { MaterialInventoryService } from '../service/material-inventory.service';
import { MaterialInventoryFormService, MaterialInventoryFormGroup } from './material-inventory-form.service';

@Component({
  standalone: true,
  selector: 'jhi-material-inventory-update',
  templateUrl: './material-inventory-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MaterialInventoryUpdateComponent implements OnInit {
  isSaving = false;
  materialInventory: IMaterialInventory | null = null;

  materialsSharedCollection: IMaterial[] = [];

  protected materialInventoryService = inject(MaterialInventoryService);
  protected materialInventoryFormService = inject(MaterialInventoryFormService);
  protected materialService = inject(MaterialService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MaterialInventoryFormGroup = this.materialInventoryFormService.createMaterialInventoryFormGroup();

  compareMaterial = (o1: IMaterial | null, o2: IMaterial | null): boolean => this.materialService.compareMaterial(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ materialInventory }) => {
      this.materialInventory = materialInventory;
      if (materialInventory) {
        this.updateForm(materialInventory);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const materialInventory = this.materialInventoryFormService.getMaterialInventory(this.editForm);
    if (materialInventory.id !== null) {
      this.subscribeToSaveResponse(this.materialInventoryService.update(materialInventory));
    } else {
      this.subscribeToSaveResponse(this.materialInventoryService.create(materialInventory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMaterialInventory>>): void {
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

  protected updateForm(materialInventory: IMaterialInventory): void {
    this.materialInventory = materialInventory;
    this.materialInventoryFormService.resetForm(this.editForm, materialInventory);

    this.materialsSharedCollection = this.materialService.addMaterialToCollectionIfMissing<IMaterial>(
      this.materialsSharedCollection,
      materialInventory.material,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.materialService
      .query()
      .pipe(map((res: HttpResponse<IMaterial[]>) => res.body ?? []))
      .pipe(
        map((materials: IMaterial[]) =>
          this.materialService.addMaterialToCollectionIfMissing<IMaterial>(materials, this.materialInventory?.material),
        ),
      )
      .subscribe((materials: IMaterial[]) => (this.materialsSharedCollection = materials));
  }
}
