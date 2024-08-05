import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPlanning } from '../planning.model';
import { PlanningService } from '../service/planning.service';

@Component({
  standalone: true,
  templateUrl: './planning-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PlanningDeleteDialogComponent {
  planning?: IPlanning;

  protected planningService = inject(PlanningService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.planningService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
