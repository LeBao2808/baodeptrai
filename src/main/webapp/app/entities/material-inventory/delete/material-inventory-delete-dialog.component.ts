import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMaterialInventory } from '../material-inventory.model';
import { MaterialInventoryService } from '../service/material-inventory.service';

@Component({
  standalone: true,
  templateUrl: './material-inventory-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MaterialInventoryDeleteDialogComponent {
  materialInventory?: IMaterialInventory;

  protected materialInventoryService = inject(MaterialInventoryService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.materialInventoryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
