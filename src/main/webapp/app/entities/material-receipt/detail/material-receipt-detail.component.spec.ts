import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { MaterialReceiptDetailComponent } from './material-receipt-detail.component';

describe('MaterialReceipt Management Detail Component', () => {
  let comp: MaterialReceiptDetailComponent;
  let fixture: ComponentFixture<MaterialReceiptDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MaterialReceiptDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: MaterialReceiptDetailComponent,
              resolve: { materialReceipt: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MaterialReceiptDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MaterialReceiptDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load materialReceipt on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MaterialReceiptDetailComponent);

      // THEN
      expect(instance.materialReceipt()).toEqual(expect.objectContaining({ id: 123 }));
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
