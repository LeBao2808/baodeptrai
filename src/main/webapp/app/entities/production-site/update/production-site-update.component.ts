import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IProductionSite } from '../production-site.model';
import { ProductionSiteService } from '../service/production-site.service';
import { ProductionSiteFormService, ProductionSiteFormGroup } from './production-site-form.service';

@Component({
  standalone: true,
  selector: 'jhi-production-site-update',
  templateUrl: './production-site-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProductionSiteUpdateComponent implements OnInit {
  isSaving = false;
  productionSite: IProductionSite | null = null;

  productsSharedCollection: IProduct[] = [];

  protected productionSiteService = inject(ProductionSiteService);
  protected productionSiteFormService = inject(ProductionSiteFormService);
  protected productService = inject(ProductService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductionSiteFormGroup = this.productionSiteFormService.createProductionSiteFormGroup();

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productionSite }) => {
      this.productionSite = productionSite;
      if (productionSite) {
        this.updateForm(productionSite);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const productionSite = this.productionSiteFormService.getProductionSite(this.editForm);
    if (productionSite.id !== null) {
      this.subscribeToSaveResponse(this.productionSiteService.update(productionSite));
    } else {
      this.subscribeToSaveResponse(this.productionSiteService.create(productionSite));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductionSite>>): void {
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

  protected updateForm(productionSite: IProductionSite): void {
    this.productionSite = productionSite;
    this.productionSiteFormService.resetForm(this.editForm, productionSite);

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      productionSite.productId,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing<IProduct>(products, this.productionSite?.productId),
        ),
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }
}
