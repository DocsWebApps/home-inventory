import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IItemLocation, ItemLocation } from '../item-location.model';
import { ItemLocationService } from '../service/item-location.service';

@Injectable({ providedIn: 'root' })
export class ItemLocationRoutingResolveService implements Resolve<IItemLocation> {
  constructor(protected service: ItemLocationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IItemLocation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((itemLocation: HttpResponse<ItemLocation>) => {
          if (itemLocation.body) {
            return of(itemLocation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ItemLocation());
  }
}
