import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IQuantification } from '../quantification.model';

@Component({
  standalone: true,
  selector: 'jhi-quantification-detail',
  templateUrl: './quantification-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class QuantificationDetailComponent {
  quantification = input<IQuantification | null>(null);

  previousState(): void {
    window.history.back();
  }
}
