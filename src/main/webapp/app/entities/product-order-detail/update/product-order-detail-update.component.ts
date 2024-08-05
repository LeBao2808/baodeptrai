import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProductOrder } from 'app/entities/product-order/product-order.model';
import { ProductOrderService } from 'app/entities/product-order/service/product-order.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { ProductOrderDetailService } from '../service/product-order-detail.service';
import { IProductOrderDetail } from '../product-order-detail.model';
import { ProductOrderDetailFormService, ProductOrderDetailFormGroup } from './product-order-detail-form.service';

@Component({
  standalone: true,
  selector: 'jhi-product-order-detail-update',
  templateUrl: './product-order-detail-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProductOrderDetailUpdateComponent implements OnInit {
  isSaving = false;
  productOrderDetail: IProductOrderDetail | null = null;

  productOrdersSharedCollection: IProductOrder[] = [];
  productsSharedCollection: IProduct[] = [];

  protected productOrderDetailService = inject(ProductOrderDetailService);
  protected productOrderDetailFormService = inject(ProductOrderDetailFormService);
  protected productOrderService = inject(ProductOrderService);
  protected productService = inject(ProductService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductOrderDetailFormGroup = this.productOrderDetailFormService.createProductOrderDetailFormGroup();

  compareProductOrder = (o1: IProductOrder | null, o2: IProductOrder | null): boolean =>
    this.productOrderService.compareProductOrder(o1, o2);

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productOrderDetail }) => {
      this.productOrderDetail = productOrderDetail;
      if (productOrderDetail) {
        this.updateForm(productOrderDetail);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const productOrderDetail = this.productOrderDetailFormService.getProductOrderDetail(this.editForm);
    if (productOrderDetail.id !== null) {
      this.subscribeToSaveResponse(this.productOrderDetailService.update(productOrderDetail));
    } else {
      this.subscribeToSaveResponse(this.productOrderDetailService.create(productOrderDetail));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductOrderDetail>>): void {
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

  protected updateForm(productOrderDetail: IProductOrderDetail): void {
    this.productOrderDetail = productOrderDetail;
    this.productOrderDetailFormService.resetForm(this.editForm, productOrderDetail);

    this.productOrdersSharedCollection = this.productOrderService.addProductOrderToCollectionIfMissing<IProductOrder>(
      this.productOrdersSharedCollection,
      productOrderDetail.order,
    );
    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      productOrderDetail.product,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productOrderService
      .query()
      .pipe(map((res: HttpResponse<IProductOrder[]>) => res.body ?? []))
      .pipe(
        map((productOrders: IProductOrder[]) =>
          this.productOrderService.addProductOrderToCollectionIfMissing<IProductOrder>(productOrders, this.productOrderDetail?.order),
        ),
      )
      .subscribe((productOrders: IProductOrder[]) => (this.productOrdersSharedCollection = productOrders));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing<IProduct>(products, this.productOrderDetail?.product),
        ),
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }
}
