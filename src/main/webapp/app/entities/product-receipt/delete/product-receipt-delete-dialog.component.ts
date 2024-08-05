import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IProductReceipt } from '../product-receipt.model';
import { ProductReceiptService } from '../service/product-receipt.service';

@Component({
  standalone: true,
  templateUrl: './product-receipt-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ProductReceiptDeleteDialogComponent {
  productReceipt?: IProductReceipt;

  protected productReceiptService = inject(ProductReceiptService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.productReceiptService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
