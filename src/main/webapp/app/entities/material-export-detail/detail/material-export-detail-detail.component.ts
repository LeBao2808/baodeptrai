import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IMaterialExportDetail } from '../material-export-detail.model';

@Component({
  standalone: true,
  selector: 'jhi-material-export-detail-detail',
  templateUrl: './material-export-detail-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class MaterialExportDetailDetailComponent {
  materialExportDetail = input<IMaterialExportDetail | null>(null);

  previousState(): void {
    window.history.back();
  }
}
