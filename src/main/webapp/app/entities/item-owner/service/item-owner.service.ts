import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { getItemOwnerIdentifier, IItemOwner } from '../item-owner.model';

export type EntityResponseType = HttpResponse<IItemOwner>;
export type EntityArrayResponseType = HttpResponse<IItemOwner[]>;

@Injectable({ providedIn: 'root' })
export class ItemOwnerService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/item-owners');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(itemOwner: IItemOwner): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(itemOwner);
    return this.http
      .post<IItemOwner>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(itemOwner: IItemOwner): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(itemOwner);
    return this.http
      .put<IItemOwner>(`${this.resourceUrl}/${getItemOwnerIdentifier(itemOwner) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(itemOwner: IItemOwner): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(itemOwner);
    return this.http
      .patch<IItemOwner>(`${this.resourceUrl}/${getItemOwnerIdentifier(itemOwner) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IItemOwner>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IItemOwner[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addItemOwnerToCollectionIfMissing(
    itemOwnerCollection: IItemOwner[],
    ...itemOwnersToCheck: (IItemOwner | null | undefined)[]
  ): IItemOwner[] {
    const itemOwners: IItemOwner[] = itemOwnersToCheck.filter(isPresent);
    if (itemOwners.length > 0) {
      const itemOwnerCollectionIdentifiers = itemOwnerCollection.map(itemOwnerItem => getItemOwnerIdentifier(itemOwnerItem)!);
      const itemOwnersToAdd = itemOwners.filter(itemOwnerItem => {
        const itemOwnerIdentifier = getItemOwnerIdentifier(itemOwnerItem);
        if (itemOwnerIdentifier == null || itemOwnerCollectionIdentifiers.includes(itemOwnerIdentifier)) {
          return false;
        }
        itemOwnerCollectionIdentifiers.push(itemOwnerIdentifier);
        return true;
      });
      return [...itemOwnersToAdd, ...itemOwnerCollection];
    }
    return itemOwnerCollection;
  }

  protected convertDateFromClient(itemOwner: IItemOwner): IItemOwner {
    return Object.assign({}, itemOwner, {
      createdDate: itemOwner.createdDate?.isValid() ? itemOwner.createdDate.format(DATE_FORMAT) : undefined,
      lastModifiedDate: itemOwner.lastModifiedDate?.isValid() ? itemOwner.lastModifiedDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((itemOwner: IItemOwner) => {
        itemOwner.createdDate = itemOwner.createdDate ? dayjs(itemOwner.createdDate) : undefined;
        itemOwner.lastModifiedDate = itemOwner.lastModifiedDate ? dayjs(itemOwner.lastModifiedDate) : undefined;
      });
    }
    return res;
  }
}
