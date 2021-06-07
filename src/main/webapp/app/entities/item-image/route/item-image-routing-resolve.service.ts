import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IItemImage, ItemImage } from '../item-image.model';
import { ItemImageService } from '../service/item-image.service';

@Injectable({ providedIn: 'root' })
export class ItemImageRoutingResolveService implements Resolve<IItemImage> {
  constructor(protected service: ItemImageService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IItemImage> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((itemImage: HttpResponse<ItemImage>) => {
          if (itemImage.body) {
            return of(itemImage.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ItemImage());
  }
}
