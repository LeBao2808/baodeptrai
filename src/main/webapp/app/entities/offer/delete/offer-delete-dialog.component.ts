import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IOffer } from '../offer.model';
import { OfferService } from '../service/offer.service';

@Component({
  standalone: true,
  templateUrl: './offer-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class OfferDeleteDialogComponent {
  offer?: IOffer;

  protected offerService = inject(OfferService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.offerService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
