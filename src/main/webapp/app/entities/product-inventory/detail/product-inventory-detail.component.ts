import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IProductInventory } from '../product-inventory.model';

@Component({
  standalone: true,
  selector: 'jhi-product-inventory-detail',
  templateUrl: './product-inventory-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ProductInventoryDetailComponent {
  productInventory = input<IProductInventory | null>(null);

  previousState(): void {
    window.history.back();
  }
}
