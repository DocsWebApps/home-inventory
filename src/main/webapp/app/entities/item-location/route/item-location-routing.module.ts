import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ItemLocationComponent } from '../list/item-location.component';
import { ItemLocationDetailComponent } from '../detail/item-location-detail.component';
import { ItemLocationUpdateComponent } from '../update/item-location-update.component';
import { ItemLocationRoutingResolveService } from './item-location-routing-resolve.service';

const itemLocationRoute: Routes = [
  {
    path: '',
    component: ItemLocationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ItemLocationDetailComponent,
    resolve: {
      itemLocation: ItemLocationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ItemLocationUpdateComponent,
    resolve: {
      itemLocation: ItemLocationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ItemLocationUpdateComponent,
    resolve: {
      itemLocation: ItemLocationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(itemLocationRoute)],
  exports: [RouterModule],
})
export class ItemLocationRoutingModule {}
