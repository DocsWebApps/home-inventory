import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IItemImage } from '../item-image.model';
import { ItemImageService } from '../service/item-image.service';

@Component({
  templateUrl: './item-image-delete-dialog.component.html',
})
export class ItemImageDeleteDialogComponent {
  itemImage?: IItemImage;

  constructor(protected itemImageService: ItemImageService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.itemImageService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
