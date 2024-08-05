import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { QuantificationDetailComponent } from './quantification-detail.component';

describe('Quantification Management Detail Component', () => {
  let comp: QuantificationDetailComponent;
  let fixture: ComponentFixture<QuantificationDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QuantificationDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: QuantificationDetailComponent,
              resolve: { quantification: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(QuantificationDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(QuantificationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load quantification on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', QuantificationDetailComponent);

      // THEN
      expect(instance.quantification()).toEqual(expect.objectContaining({ id: 123 }));
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
