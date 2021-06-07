import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { getItemMakeIdentifier, IItemMake } from '../item-make.model';

export type EntityResponseType = HttpResponse<IItemMake>;
export type EntityArrayResponseType = HttpResponse<IItemMake[]>;

@Injectable({ providedIn: 'root' })
export class ItemMakeService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/item-makes');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(itemMake: IItemMake): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(itemMake);
    return this.http
      .post<IItemMake>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(itemMake: IItemMake): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(itemMake);
    return this.http
      .put<IItemMake>(`${this.resourceUrl}/${getItemMakeIdentifier(itemMake) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(itemMake: IItemMake): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(itemMake);
    return this.http
      .patch<IItemMake>(`${this.resourceUrl}/${getItemMakeIdentifier(itemMake) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IItemMake>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IItemMake[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addItemMakeToCollectionIfMissing(itemMakeCollection: IItemMake[], ...itemMakesToCheck: (IItemMake | null | undefined)[]): IItemMake[] {
    const itemMakes: IItemMake[] = itemMakesToCheck.filter(isPresent);
    if (itemMakes.length > 0) {
      const itemMakeCollectionIdentifiers = itemMakeCollection.map(itemMakeItem => getItemMakeIdentifier(itemMakeItem)!);
      const itemMakesToAdd = itemMakes.filter(itemMakeItem => {
        const itemMakeIdentifier = getItemMakeIdentifier(itemMakeItem);
        if (itemMakeIdentifier == null || itemMakeCollectionIdentifiers.includes(itemMakeIdentifier)) {
          return false;
        }
        itemMakeCollectionIdentifiers.push(itemMakeIdentifier);
        return true;
      });
      return [...itemMakesToAdd, ...itemMakeCollection];
    }
    return itemMakeCollection;
  }

  protected convertDateFromClient(itemMake: IItemMake): IItemMake {
    return Object.assign({}, itemMake, {
      createdDate: itemMake.createdDate?.isValid() ? itemMake.createdDate.format(DATE_FORMAT) : undefined,
      lastModifiedDate: itemMake.lastModifiedDate?.isValid() ? itemMake.lastModifiedDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((itemMake: IItemMake) => {
        itemMake.createdDate = itemMake.createdDate ? dayjs(itemMake.createdDate) : undefined;
        itemMake.lastModifiedDate = itemMake.lastModifiedDate ? dayjs(itemMake.lastModifiedDate) : undefined;
      });
    }
    return res;
  }
}
