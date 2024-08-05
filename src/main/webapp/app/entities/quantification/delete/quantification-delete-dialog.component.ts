import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IQuantification } from '../quantification.model';
import { QuantificationService } from '../service/quantification.service';

@Component({
  standalone: true,
  templateUrl: './quantification-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class QuantificationDeleteDialogComponent {
  quantification?: IQuantification;

  protected quantificationService = inject(QuantificationService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.quantificationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
