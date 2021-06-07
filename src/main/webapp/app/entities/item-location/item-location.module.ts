import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ItemLocationComponent } from './list/item-location.component';
import { ItemLocationDetailComponent } from './detail/item-location-detail.component';
import { ItemLocationUpdateComponent } from './update/item-location-update.component';
import { ItemLocationDeleteDialogComponent } from './delete/item-location-delete-dialog.component';
import { ItemLocationRoutingModule } from './route/item-location-routing.module';

@NgModule({
  imports: [SharedModule, ItemLocationRoutingModule],
  declarations: [ItemLocationComponent, ItemLocationDetailComponent, ItemLocationUpdateComponent, ItemLocationDeleteDialogComponent],
  entryComponents: [ItemLocationDeleteDialogComponent],
})
export class ItemLocationModule {}
