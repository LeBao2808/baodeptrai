import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IOffer } from 'app/entities/offer/offer.model';
import { OfferService } from 'app/entities/offer/service/offer.service';
import { OfferDetailService } from '../service/offer-detail.service';
import { IOfferDetail } from '../offer-detail.model';
import { OfferDetailFormService, OfferDetailFormGroup } from './offer-detail-form.service';

@Component({
  standalone: true,
  selector: 'jhi-offer-detail-update',
  templateUrl: './offer-detail-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OfferDetailUpdateComponent implements OnInit {
  isSaving = false;
  offerDetail: IOfferDetail | null = null;

  productsSharedCollection: IProduct[] = [];
  offersSharedCollection: IOffer[] = [];

  protected offerDetailService = inject(OfferDetailService);
  protected offerDetailFormService = inject(OfferDetailFormService);
  protected productService = inject(ProductService);
  protected offerService = inject(OfferService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OfferDetailFormGroup = this.offerDetailFormService.createOfferDetailFormGroup();

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareOffer = (o1: IOffer | null, o2: IOffer | null): boolean => this.offerService.compareOffer(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ offerDetail }) => {
      this.offerDetail = offerDetail;
      if (offerDetail) {
        this.updateForm(offerDetail);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const offerDetail = this.offerDetailFormService.getOfferDetail(this.editForm);
    if (offerDetail.id !== null) {
      this.subscribeToSaveResponse(this.offerDetailService.update(offerDetail));
    } else {
      this.subscribeToSaveResponse(this.offerDetailService.create(offerDetail));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOfferDetail>>): void {
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

  protected updateForm(offerDetail: IOfferDetail): void {
    this.offerDetail = offerDetail;
    this.offerDetailFormService.resetForm(this.editForm, offerDetail);

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      offerDetail.product,
    );
    this.offersSharedCollection = this.offerService.addOfferToCollectionIfMissing<IOffer>(this.offersSharedCollection, offerDetail.offer);
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing<IProduct>(products, this.offerDetail?.product)),
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));

    this.offerService
      .query()
      .pipe(map((res: HttpResponse<IOffer[]>) => res.body ?? []))
      .pipe(map((offers: IOffer[]) => this.offerService.addOfferToCollectionIfMissing<IOffer>(offers, this.offerDetail?.offer)))
      .subscribe((offers: IOffer[]) => (this.offersSharedCollection = offers));
  }
}
