import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IMaterialInventory } from '../material-inventory.model';

@Component({
  standalone: true,
  selector: 'jhi-material-inventory-detail',
  templateUrl: './material-inventory-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class MaterialInventoryDetailComponent {
  materialInventory = input<IMaterialInventory | null>(null);

  previousState(): void {
    window.history.back();
  }
}
