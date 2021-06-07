import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IItemOwner } from '../item-owner.model';
import { ItemOwnerService } from '../service/item-owner.service';

@Component({
  templateUrl: './item-owner-delete-dialog.component.html',
})
export class ItemOwnerDeleteDialogComponent {
  itemOwner?: IItemOwner;

  constructor(protected itemOwnerService: ItemOwnerService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.itemOwnerService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
