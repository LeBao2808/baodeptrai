import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IPlanning } from '../planning.model';

@Component({
  standalone: true,
  selector: 'jhi-planning-detail',
  templateUrl: './planning-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PlanningDetailComponent {
  planning = input<IPlanning | null>(null);

  previousState(): void {
    window.history.back();
  }
}
