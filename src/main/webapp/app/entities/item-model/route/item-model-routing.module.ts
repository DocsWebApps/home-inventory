import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ItemModelComponent } from '../list/item-model.component';
import { ItemModelDetailComponent } from '../detail/item-model-detail.component';
import { ItemModelUpdateComponent } from '../update/item-model-update.component';
import { ItemModelRoutingResolveService } from './item-model-routing-resolve.service';

const itemModelRoute: Routes = [
  {
    path: '',
    component: ItemModelComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ItemModelDetailComponent,
    resolve: {
      itemModel: ItemModelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ItemModelUpdateComponent,
    resolve: {
      itemModel: ItemModelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ItemModelUpdateComponent,
    resolve: {
      itemModel: ItemModelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(itemModelRoute)],
  exports: [RouterModule],
})
export class ItemModelRoutingModule {}
