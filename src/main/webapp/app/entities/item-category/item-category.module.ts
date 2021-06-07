import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ItemCategoryComponent } from './list/item-category.component';
import { ItemCategoryDetailComponent } from './detail/item-category-detail.component';
import { ItemCategoryUpdateComponent } from './update/item-category-update.component';
import { ItemCategoryDeleteDialogComponent } from './delete/item-category-delete-dialog.component';
import { ItemCategoryRoutingModule } from './route/item-category-routing.module';

@NgModule({
  imports: [SharedModule, ItemCategoryRoutingModule],
  declarations: [ItemCategoryComponent, ItemCategoryDetailComponent, ItemCategoryUpdateComponent, ItemCategoryDeleteDialogComponent],
  entryComponents: [ItemCategoryDeleteDialogComponent],
})
export class ItemCategoryModule {}
