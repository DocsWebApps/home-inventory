import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IItemMake } from '../item-make.model';
import { ItemMakeService } from '../service/item-make.service';

@Component({
  templateUrl: './item-make-delete-dialog.component.html',
})
export class ItemMakeDeleteDialogComponent {
  itemMake?: IItemMake;

  constructor(protected itemMakeService: ItemMakeService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.itemMakeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
