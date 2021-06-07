import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { getItemCategoryIdentifier, IItemCategory } from '../item-category.model';

export type EntityResponseType = HttpResponse<IItemCategory>;
export type EntityArrayResponseType = HttpResponse<IItemCategory[]>;

@Injectable({ providedIn: 'root' })
export class ItemCategoryService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/item-categories');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(itemCategory: IItemCategory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(itemCategory);
    return this.http
      .post<IItemCategory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(itemCategory: IItemCategory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(itemCategory);
    return this.http
      .put<IItemCategory>(`${this.resourceUrl}/${getItemCategoryIdentifier(itemCategory) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(itemCategory: IItemCategory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(itemCategory);
    return this.http
      .patch<IItemCategory>(`${this.resourceUrl}/${getItemCategoryIdentifier(itemCategory) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IItemCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IItemCategory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addItemCategoryToCollectionIfMissing(
    itemCategoryCollection: IItemCategory[],
    ...itemCategoriesToCheck: (IItemCategory | null | undefined)[]
  ): IItemCategory[] {
    const itemCategories: IItemCategory[] = itemCategoriesToCheck.filter(isPresent);
    if (itemCategories.length > 0) {
      const itemCategoryCollectionIdentifiers = itemCategoryCollection.map(
        itemCategoryItem => getItemCategoryIdentifier(itemCategoryItem)!
      );
      const itemCategoriesToAdd = itemCategories.filter(itemCategoryItem => {
        const itemCategoryIdentifier = getItemCategoryIdentifier(itemCategoryItem);
        if (itemCategoryIdentifier == null || itemCategoryCollectionIdentifiers.includes(itemCategoryIdentifier)) {
          return false;
        }
        itemCategoryCollectionIdentifiers.push(itemCategoryIdentifier);
        return true;
      });
      return [...itemCategoriesToAdd, ...itemCategoryCollection];
    }
    return itemCategoryCollection;
  }

  protected convertDateFromClient(itemCategory: IItemCategory): IItemCategory {
    return Object.assign({}, itemCategory, {
      createdDate: itemCategory.createdDate?.isValid() ? itemCategory.createdDate.format(DATE_FORMAT) : undefined,
      lastModifiedDate: itemCategory.lastModifiedDate?.isValid() ? itemCategory.lastModifiedDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdDate = res.body.createdDate ? dayjs(res.body.createdDate) : undefined;
      res.body.lastModifiedDate = res.body.lastModifiedDate ? dayjs(res.body.lastModifiedDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((itemCategory: IItemCategory) => {
        itemCategory.createdDate = itemCategory.createdDate ? dayjs(itemCategory.createdDate) : undefined;
        itemCategory.lastModifiedDate = itemCategory.lastModifiedDate ? dayjs(itemCategory.lastModifiedDate) : undefined;
      });
    }
    return res;
  }
}
