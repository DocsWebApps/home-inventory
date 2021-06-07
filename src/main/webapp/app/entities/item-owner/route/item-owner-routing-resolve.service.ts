import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IItemOwner, ItemOwner } from '../item-owner.model';
import { ItemOwnerService } from '../service/item-owner.service';

@Injectable({ providedIn: 'root' })
export class ItemOwnerRoutingResolveService implements Resolve<IItemOwner> {
  constructor(protected service: ItemOwnerService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IItemOwner> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((itemOwner: HttpResponse<ItemOwner>) => {
          if (itemOwner.body) {
            return of(itemOwner.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ItemOwner());
  }
}
