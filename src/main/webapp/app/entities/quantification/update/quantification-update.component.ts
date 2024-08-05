import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IMaterial } from 'app/entities/material/material.model';
import { MaterialService } from 'app/entities/material/service/material.service';
import { QuantificationService } from '../service/quantification.service';
import { IQuantification } from '../quantification.model';
import { QuantificationFormService, QuantificationFormGroup } from './quantification-form.service';

@Component({
  standalone: true,
  selector: 'jhi-quantification-update',
  templateUrl: './quantification-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class QuantificationUpdateComponent implements OnInit {
  isSaving = false;
  quantification: IQuantification | null = null;

  productsSharedCollection: IProduct[] = [];
  materialsSharedCollection: IMaterial[] = [];

  protected quantificationService = inject(QuantificationService);
  protected quantificationFormService = inject(QuantificationFormService);
  protected productService = inject(ProductService);
  protected materialService = inject(MaterialService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: QuantificationFormGroup = this.quantificationFormService.createQuantificationFormGroup();

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareMaterial = (o1: IMaterial | null, o2: IMaterial | null): boolean => this.materialService.compareMaterial(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ quantification }) => {
      this.quantification = quantification;
      if (quantification) {
        this.updateForm(quantification);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const quantification = this.quantificationFormService.getQuantification(this.editForm);
    if (quantification.id !== null) {
      this.subscribeToSaveResponse(this.quantificationService.update(quantification));
    } else {
      this.subscribeToSaveResponse(this.quantificationService.create(quantification));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuantification>>): void {
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

  protected updateForm(quantification: IQuantification): void {
    this.quantification = quantification;
    this.quantificationFormService.resetForm(this.editForm, quantification);

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      quantification.product,
    );
    this.materialsSharedCollection = this.materialService.addMaterialToCollectionIfMissing<IMaterial>(
      this.materialsSharedCollection,
      quantification.material,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing<IProduct>(products, this.quantification?.product),
        ),
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));

    this.materialService
      .query()
      .pipe(map((res: HttpResponse<IMaterial[]>) => res.body ?? []))
      .pipe(
        map((materials: IMaterial[]) =>
          this.materialService.addMaterialToCollectionIfMissing<IMaterial>(materials, this.quantification?.material),
        ),
      )
      .subscribe((materials: IMaterial[]) => (this.materialsSharedCollection = materials));
  }
}
