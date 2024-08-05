import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ProductReceiptDetailDetailComponent } from './product-receipt-detail-detail.component';

describe('ProductReceiptDetail Management Detail Component', () => {
  let comp: ProductReceiptDetailDetailComponent;
  let fixture: ComponentFixture<ProductReceiptDetailDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductReceiptDetailDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ProductReceiptDetailDetailComponent,
              resolve: { productReceiptDetail: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ProductReceiptDetailDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProductReceiptDetailDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load productReceiptDetail on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ProductReceiptDetailDetailComponent);

      // THEN
      expect(instance.productReceiptDetail()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
