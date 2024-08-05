import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IProductReceiptDetail } from '../product-receipt-detail.model';
import { ProductReceiptDetailService } from '../service/product-receipt-detail.service';

@Component({
  standalone: true,
  templateUrl: './product-receipt-detail-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ProductReceiptDetailDeleteDialogComponent {
  productReceiptDetail?: IProductReceiptDetail;

  protected productReceiptDetailService = inject(ProductReceiptDetailService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.productReceiptDetailService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
