import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IOffer } from '../offer.model';

@Component({
  standalone: true,
  selector: 'jhi-offer-detail',
  templateUrl: './offer-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class OfferDetailComponent {
  offer = input<IOffer | null>(null);

  previousState(): void {
    window.history.back();
  }
}
