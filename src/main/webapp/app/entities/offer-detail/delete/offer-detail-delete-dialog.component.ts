import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IOfferDetail } from '../offer-detail.model';
import { OfferDetailService } from '../service/offer-detail.service';

@Component({
  standalone: true,
  templateUrl: './offer-detail-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class OfferDetailDeleteDialogComponent {
  offerDetail?: IOfferDetail;

  protected offerDetailService = inject(OfferDetailService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.offerDetailService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
