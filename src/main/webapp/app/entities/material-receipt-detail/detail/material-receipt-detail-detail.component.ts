import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IMaterialReceiptDetail } from '../material-receipt-detail.model';

@Component({
  standalone: true,
  selector: 'jhi-material-receipt-detail-detail',
  templateUrl: './material-receipt-detail-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class MaterialReceiptDetailDetailComponent {
  materialReceiptDetail = input<IMaterialReceiptDetail | null>(null);

  previousState(): void {
    window.history.back();
  }
}
