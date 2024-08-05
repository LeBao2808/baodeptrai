import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPlanning } from 'app/entities/planning/planning.model';
import { PlanningService } from 'app/entities/planning/service/planning.service';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { ProductOrderService } from '../service/product-order.service';
import { IProductOrder } from '../product-order.model';
import { ProductOrderFormService, ProductOrderFormGroup } from './product-order-form.service';

@Component({
  standalone: true,
  selector: 'jhi-product-order-update',
  templateUrl: './product-order-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProductOrderUpdateComponent implements OnInit {
  isSaving = false;
  productOrder: IProductOrder | null = null;

  planningsSharedCollection: IPlanning[] = [];
  customersSharedCollection: ICustomer[] = [];
  usersSharedCollection: IUser[] = [];

  protected productOrderService = inject(ProductOrderService);
  protected productOrderFormService = inject(ProductOrderFormService);
  protected planningService = inject(PlanningService);
  protected customerService = inject(CustomerService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductOrderFormGroup = this.productOrderFormService.createProductOrderFormGroup();

  comparePlanning = (o1: IPlanning | null, o2: IPlanning | null): boolean => this.planningService.comparePlanning(o1, o2);

  compareCustomer = (o1: ICustomer | null, o2: ICustomer | null): boolean => this.customerService.compareCustomer(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productOrder }) => {
      this.productOrder = productOrder;
      if (productOrder) {
        this.updateForm(productOrder);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const productOrder = this.productOrderFormService.getProductOrder(this.editForm);
    if (productOrder.id !== null) {
      this.subscribeToSaveResponse(this.productOrderService.update(productOrder));
    } else {
      this.subscribeToSaveResponse(this.productOrderService.create(productOrder));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductOrder>>): void {
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

  protected updateForm(productOrder: IProductOrder): void {
    this.productOrder = productOrder;
    this.productOrderFormService.resetForm(this.editForm, productOrder);

    this.planningsSharedCollection = this.planningService.addPlanningToCollectionIfMissing<IPlanning>(
      this.planningsSharedCollection,
      productOrder.quantificationOrder,
    );
    this.customersSharedCollection = this.customerService.addCustomerToCollectionIfMissing<ICustomer>(
      this.customersSharedCollection,
      productOrder.customer,
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
      this.usersSharedCollection,
      productOrder.createdByUser,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.planningService
      .query()
      .pipe(map((res: HttpResponse<IPlanning[]>) => res.body ?? []))
      .pipe(
        map((plannings: IPlanning[]) =>
          this.planningService.addPlanningToCollectionIfMissing<IPlanning>(plannings, this.productOrder?.quantificationOrder),
        ),
      )
      .subscribe((plannings: IPlanning[]) => (this.planningsSharedCollection = plannings));

    this.customerService
      .query()
      .pipe(map((res: HttpResponse<ICustomer[]>) => res.body ?? []))
      .pipe(
        map((customers: ICustomer[]) =>
          this.customerService.addCustomerToCollectionIfMissing<ICustomer>(customers, this.productOrder?.customer),
        ),
      )
      .subscribe((customers: ICustomer[]) => (this.customersSharedCollection = customers));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.productOrder?.createdByUser)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
