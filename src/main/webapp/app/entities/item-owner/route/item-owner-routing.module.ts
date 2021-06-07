import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ItemOwnerComponent } from '../list/item-owner.component';
import { ItemOwnerDetailComponent } from '../detail/item-owner-detail.component';
import { ItemOwnerUpdateComponent } from '../update/item-owner-update.component';
import { ItemOwnerRoutingResolveService } from './item-owner-routing-resolve.service';

const itemOwnerRoute: Routes = [
  {
    path: '',
    component: ItemOwnerComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ItemOwnerDetailComponent,
    resolve: {
      itemOwner: ItemOwnerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ItemOwnerUpdateComponent,
    resolve: {
      itemOwner: ItemOwnerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ItemOwnerUpdateComponent,
    resolve: {
      itemOwner: ItemOwnerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(itemOwnerRoute)],
  exports: [RouterModule],
})
export class ItemOwnerRoutingModule {}
