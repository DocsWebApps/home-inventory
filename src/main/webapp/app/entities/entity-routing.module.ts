import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'item-category',
        data: { pageTitle: 'ItemCategories' },
        loadChildren: () => import('./item-category/item-category.module').then(m => m.ItemCategoryModule),
      },
      {
        path: 'item',
        data: { pageTitle: 'Items' },
        loadChildren: () => import('./item/item.module').then(m => m.ItemModule),
      },
      {
        path: 'item-image',
        data: { pageTitle: 'ItemImages' },
        loadChildren: () => import('./item-image/item-image.module').then(m => m.ItemImageModule),
      },
      {
        path: 'item-location',
        data: { pageTitle: 'ItemLocations' },
        loadChildren: () => import('./item-location/item-location.module').then(m => m.ItemLocationModule),
      },
      {
        path: 'item-make',
        data: { pageTitle: 'ItemMakes' },
        loadChildren: () => import('./item-make/item-make.module').then(m => m.ItemMakeModule),
      },
      {
        path: 'item-model',
        data: { pageTitle: 'ItemModels' },
        loadChildren: () => import('./item-model/item-model.module').then(m => m.ItemModelModule),
      },
      {
        path: 'item-owner',
        data: { pageTitle: 'ItemOwners' },
        loadChildren: () => import('./item-owner/item-owner.module').then(m => m.ItemOwnerModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
