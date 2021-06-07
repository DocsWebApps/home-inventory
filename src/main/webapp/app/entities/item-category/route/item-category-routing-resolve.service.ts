import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IItemCategory, ItemCategory } from '../item-category.model';
import { ItemCategoryService } from '../service/item-category.service';

@Injectable({ providedIn: 'root' })
export class ItemCategoryRoutingResolveService implements Resolve<IItemCategory> {
  constructor(protected service: ItemCategoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IItemCategory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((itemCategory: HttpResponse<ItemCategory>) => {
          if (itemCategory.body) {
            return of(itemCategory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ItemCategory());
  }
}
