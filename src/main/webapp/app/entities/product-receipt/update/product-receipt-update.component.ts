import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IProductReceipt } from '../product-receipt.model';
import { ProductReceiptService } from '../service/product-receipt.service';
import { ProductReceiptFormService, ProductReceiptFormGroup } from './product-receipt-form.service';

@Component({
  standalone: true,
  selector: 'jhi-product-receipt-update',
  templateUrl: './product-receipt-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProductReceiptUpdateComponent implements OnInit {
  isSaving = false;
  productReceipt: IProductReceipt | null = null;

  usersSharedCollection: IUser[] = [];

  protected productReceiptService = inject(ProductReceiptService);
  protected productReceiptFormService = inject(ProductReceiptFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductReceiptFormGroup = this.productReceiptFormService.createProductReceiptFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productReceipt }) => {
      this.productReceipt = productReceipt;
      if (productReceipt) {
        this.updateForm(productReceipt);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const productReceipt = this.productReceiptFormService.getProductReceipt(this.editForm);
    if (productReceipt.id !== null) {
      this.subscribeToSaveResponse(this.productReceiptService.update(productReceipt));
    } else {
      this.subscribeToSaveResponse(this.productReceiptService.create(productReceipt));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductReceipt>>): void {
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

  protected updateForm(productReceipt: IProductReceipt): void {
    this.productReceipt = productReceipt;
    this.productReceiptFormService.resetForm(this.editForm, productReceipt);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, productReceipt.created);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.productReceipt?.created)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
