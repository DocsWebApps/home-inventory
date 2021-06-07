import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IItemModel, ItemModel } from '../item-model.model';
import { ItemModelService } from '../service/item-model.service';

@Injectable({ providedIn: 'root' })
export class ItemModelRoutingResolveService implements Resolve<IItemModel> {
  constructor(protected service: ItemModelService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IItemModel> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((itemModel: HttpResponse<ItemModel>) => {
          if (itemModel.body) {
            return of(itemModel.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ItemModel());
  }
}
