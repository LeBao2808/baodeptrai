import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IProductReceipt } from '../product-receipt.model';

@Component({
  standalone: true,
  selector: 'jhi-product-receipt-detail',
  templateUrl: './product-receipt-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ProductReceiptDetailComponent {
  productReceipt = input<IProductReceipt | null>(null);

  previousState(): void {
    window.history.back();
  }
}
