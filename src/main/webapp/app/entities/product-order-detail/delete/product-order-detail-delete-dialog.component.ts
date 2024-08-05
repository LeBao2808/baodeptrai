import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IProductOrderDetail } from '../product-order-detail.model';
import { ProductOrderDetailService } from '../service/product-order-detail.service';

@Component({
  standalone: true,
  templateUrl: './product-order-detail-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ProductOrderDetailDeleteDialogComponent {
  productOrderDetail?: IProductOrderDetail;

  protected productOrderDetailService = inject(ProductOrderDetailService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.productOrderDetailService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
