import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IItemModel } from '../item-model.model';
import { ItemModelService } from '../service/item-model.service';

@Component({
  templateUrl: './item-model-delete-dialog.component.html',
})
export class ItemModelDeleteDialogComponent {
  itemModel?: IItemModel;

  constructor(protected itemModelService: ItemModelService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.itemModelService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
