import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMaterialReceipt } from '../material-receipt.model';
import { MaterialReceiptService } from '../service/material-receipt.service';

@Component({
  standalone: true,
  templateUrl: './material-receipt-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MaterialReceiptDeleteDialogComponent {
  materialReceipt?: IMaterialReceipt;

  protected materialReceiptService = inject(MaterialReceiptService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.materialReceiptService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
