import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ItemModelComponent } from './list/item-model.component';
import { ItemModelDetailComponent } from './detail/item-model-detail.component';
import { ItemModelUpdateComponent } from './update/item-model-update.component';
import { ItemModelDeleteDialogComponent } from './delete/item-model-delete-dialog.component';
import { ItemModelRoutingModule } from './route/item-model-routing.module';

@NgModule({
  imports: [SharedModule, ItemModelRoutingModule],
  declarations: [ItemModelComponent, ItemModelDetailComponent, ItemModelUpdateComponent, ItemModelDeleteDialogComponent],
  entryComponents: [ItemModelDeleteDialogComponent],
})
export class ItemModelModule {}
