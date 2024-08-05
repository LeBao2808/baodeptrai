import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IMaterialExport } from '../material-export.model';

@Component({
  standalone: true,
  selector: 'jhi-material-export-detail',
  templateUrl: './material-export-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class MaterialExportDetailComponent {
  materialExport = input<IMaterialExport | null>(null);

  previousState(): void {
    window.history.back();
  }
}
