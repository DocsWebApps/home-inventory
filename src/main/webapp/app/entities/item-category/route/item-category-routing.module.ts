import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ItemCategoryComponent } from '../list/item-category.component';
import { ItemCategoryDetailComponent } from '../detail/item-category-detail.component';
import { ItemCategoryUpdateComponent } from '../update/item-category-update.component';
import { ItemCategoryRoutingResolveService } from './item-category-routing-resolve.service';

const itemCategoryRoute: Routes = [
  {
    path: '',
    component: ItemCategoryComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ItemCategoryDetailComponent,
    resolve: {
      itemCategory: ItemCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ItemCategoryUpdateComponent,
    resolve: {
      itemCategory: ItemCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ItemCategoryUpdateComponent,
    resolve: {
      itemCategory: ItemCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(itemCategoryRoute)],
  exports: [RouterModule],
})
export class ItemCategoryRoutingModule {}
