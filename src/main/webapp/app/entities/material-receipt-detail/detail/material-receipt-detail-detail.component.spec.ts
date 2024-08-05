import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { MaterialReceiptDetailDetailComponent } from './material-receipt-detail-detail.component';

describe('MaterialReceiptDetail Management Detail Component', () => {
  let comp: MaterialReceiptDetailDetailComponent;
  let fixture: ComponentFixture<MaterialReceiptDetailDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MaterialReceiptDetailDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: MaterialReceiptDetailDetailComponent,
              resolve: { materialReceiptDetail: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MaterialReceiptDetailDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MaterialReceiptDetailDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load materialReceiptDetail on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MaterialReceiptDetailDetailComponent);

      // THEN
      expect(instance.materialReceiptDetail()).toEqual(expect.objectContaining({ id: 123 }));
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
