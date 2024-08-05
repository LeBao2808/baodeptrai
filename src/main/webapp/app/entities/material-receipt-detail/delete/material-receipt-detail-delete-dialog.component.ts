import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMaterialReceiptDetail } from '../material-receipt-detail.model';
import { MaterialReceiptDetailService } from '../service/material-receipt-detail.service';

@Component({
  standalone: true,
  templateUrl: './material-receipt-detail-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MaterialReceiptDetailDeleteDialogComponent {
  materialReceiptDetail?: IMaterialReceiptDetail;

  protected materialReceiptDetailService = inject(MaterialReceiptDetailService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.materialReceiptDetailService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
