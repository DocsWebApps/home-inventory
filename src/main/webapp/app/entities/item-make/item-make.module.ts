import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ItemMakeComponent } from './list/item-make.component';
import { ItemMakeDetailComponent } from './detail/item-make-detail.component';
import { ItemMakeUpdateComponent } from './update/item-make-update.component';
import { ItemMakeDeleteDialogComponent } from './delete/item-make-delete-dialog.component';
import { ItemMakeRoutingModule } from './route/item-make-routing.module';

@NgModule({
  imports: [SharedModule, ItemMakeRoutingModule],
  declarations: [ItemMakeComponent, ItemMakeDetailComponent, ItemMakeUpdateComponent, ItemMakeDeleteDialogComponent],
  entryComponents: [ItemMakeDeleteDialogComponent],
})
export class ItemMakeModule {}
