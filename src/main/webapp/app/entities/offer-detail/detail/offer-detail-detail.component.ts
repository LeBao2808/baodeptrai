import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IOfferDetail } from '../offer-detail.model';

@Component({
  standalone: true,
  selector: 'jhi-offer-detail-detail',
  templateUrl: './offer-detail-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class OfferDetailDetailComponent {
  offerDetail = input<IOfferDetail | null>(null);

  previousState(): void {
    window.history.back();
  }
}
