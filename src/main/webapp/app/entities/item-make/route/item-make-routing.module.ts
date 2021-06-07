import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ItemMakeComponent } from '../list/item-make.component';
import { ItemMakeDetailComponent } from '../detail/item-make-detail.component';
import { ItemMakeUpdateComponent } from '../update/item-make-update.component';
import { ItemMakeRoutingResolveService } from './item-make-routing-resolve.service';

const itemMakeRoute: Routes = [
  {
    path: '',
    component: ItemMakeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ItemMakeDetailComponent,
    resolve: {
      itemMake: ItemMakeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ItemMakeUpdateComponent,
    resolve: {
      itemMake: ItemMakeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ItemMakeUpdateComponent,
    resolve: {
      itemMake: ItemMakeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(itemMakeRoute)],
  exports: [RouterModule],
})
export class ItemMakeRoutingModule {}
