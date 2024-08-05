import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IProductOrderDetail } from '../product-order-detail.model';

@Component({
  standalone: true,
  selector: 'jhi-product-order-detail-detail',
  templateUrl: './product-order-detail-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ProductOrderDetailDetailComponent {
  productOrderDetail = input<IProductOrderDetail | null>(null);

  previousState(): void {
    window.history.back();
  }
}
