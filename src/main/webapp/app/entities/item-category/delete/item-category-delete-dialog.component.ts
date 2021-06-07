import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IItemCategory } from '../item-category.model';
import { ItemCategoryService } from '../service/item-category.service';

@Component({
  templateUrl: './item-category-delete-dialog.component.html',
})
export class ItemCategoryDeleteDialogComponent {
  itemCategory?: IItemCategory;

  constructor(protected itemCategoryService: ItemCategoryService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.itemCategoryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
