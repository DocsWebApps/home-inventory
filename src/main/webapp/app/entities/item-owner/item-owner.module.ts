import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ItemOwnerComponent } from './list/item-owner.component';
import { ItemOwnerDetailComponent } from './detail/item-owner-detail.component';
import { ItemOwnerUpdateComponent } from './update/item-owner-update.component';
import { ItemOwnerDeleteDialogComponent } from './delete/item-owner-delete-dialog.component';
import { ItemOwnerRoutingModule } from './route/item-owner-routing.module';

@NgModule({
  imports: [SharedModule, ItemOwnerRoutingModule],
  declarations: [ItemOwnerComponent, ItemOwnerDetailComponent, ItemOwnerUpdateComponent, ItemOwnerDeleteDialogComponent],
  entryComponents: [ItemOwnerDeleteDialogComponent],
})
export class ItemOwnerModule {}
