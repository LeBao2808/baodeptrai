import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IProductReceipt } from 'app/entities/product-receipt/product-receipt.model';
import { ProductReceiptService } from 'app/entities/product-receipt/service/product-receipt.service';
import { ProductReceiptDetailService } from '../service/product-receipt-detail.service';
import { IProductReceiptDetail } from '../product-receipt-detail.model';
import { ProductReceiptDetailFormService, ProductReceiptDetailFormGroup } from './product-receipt-detail-form.service';

@Component({
  standalone: true,
  selector: 'jhi-product-receipt-detail-update',
  templateUrl: './product-receipt-detail-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProductReceiptDetailUpdateComponent implements OnInit {
  isSaving = false;
  productReceiptDetail: IProductReceiptDetail | null = null;

  productsSharedCollection: IProduct[] = [];
  productReceiptsSharedCollection: IProductReceipt[] = [];

  protected productReceiptDetailService = inject(ProductReceiptDetailService);
  protected productReceiptDetailFormService = inject(ProductReceiptDetailFormService);
  protected productService = inject(ProductService);
  protected productReceiptService = inject(ProductReceiptService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductReceiptDetailFormGroup = this.productReceiptDetailFormService.createProductReceiptDetailFormGroup();

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareProductReceipt = (o1: IProductReceipt | null, o2: IProductReceipt | null): boolean =>
    this.productReceiptService.compareProductReceipt(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productReceiptDetail }) => {
      this.productReceiptDetail = productReceiptDetail;
      if (productReceiptDetail) {
        this.updateForm(productReceiptDetail);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const productReceiptDetail = this.productReceiptDetailFormService.getProductReceiptDetail(this.editForm);
    if (productReceiptDetail.id !== null) {
      this.subscribeToSaveResponse(this.productReceiptDetailService.update(productReceiptDetail));
    } else {
      this.subscribeToSaveResponse(this.productReceiptDetailService.create(productReceiptDetail));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductReceiptDetail>>): void {
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

  protected updateForm(productReceiptDetail: IProductReceiptDetail): void {
    this.productReceiptDetail = productReceiptDetail;
    this.productReceiptDetailFormService.resetForm(this.editForm, productReceiptDetail);

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      productReceiptDetail.product,
    );
    this.productReceiptsSharedCollection = this.productReceiptService.addProductReceiptToCollectionIfMissing<IProductReceipt>(
      this.productReceiptsSharedCollection,
      productReceiptDetail.receipt,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing<IProduct>(products, this.productReceiptDetail?.product),
        ),
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));

    this.productReceiptService
      .query()
      .pipe(map((res: HttpResponse<IProductReceipt[]>) => res.body ?? []))
      .pipe(
        map((productReceipts: IProductReceipt[]) =>
          this.productReceiptService.addProductReceiptToCollectionIfMissing<IProductReceipt>(
            productReceipts,
            this.productReceiptDetail?.receipt,
          ),
        ),
      )
      .subscribe((productReceipts: IProductReceipt[]) => (this.productReceiptsSharedCollection = productReceipts));
  }
}
