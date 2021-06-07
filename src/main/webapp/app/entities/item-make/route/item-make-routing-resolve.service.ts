import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IItemMake, ItemMake } from '../item-make.model';
import { ItemMakeService } from '../service/item-make.service';

@Injectable({ providedIn: 'root' })
export class ItemMakeRoutingResolveService implements Resolve<IItemMake> {
  constructor(protected service: ItemMakeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IItemMake> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((itemMake: HttpResponse<ItemMake>) => {
          if (itemMake.body) {
            return of(itemMake.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ItemMake());
  }
}
