import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IItemLocation } from '../item-location.model';
import { ItemLocationService } from '../service/item-location.service';

@Component({
  templateUrl: './item-location-delete-dialog.component.html',
})
export class ItemLocationDeleteDialogComponent {
  itemLocation?: IItemLocation;

  constructor(protected itemLocationService: ItemLocationService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.itemLocationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
