import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { getItemLocationIdentifier, IItemLocation } from '../item-location.model';

export type EntityResponseType = HttpResponse<IItemLocation>;
export type EntityArrayResponseType = HttpResponse<IItemLocation[]>;

@Injectable({ providedIn: 'root' })
export class ItemLocationService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/item-locations');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(itemLocation: IItemLocation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(itemLocation);
    return this.http
      .post<IItemLocation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(itemLocation: IItemLocation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(itemLocation);
    return this.http
      .put<IItemLocation>(`${this.resourceUrl}/${getItemLocationIdentifier(itemLocation) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(itemLocation: IItemLocation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(itemLocation);
    return this.http
      .patch<IItemLocation>(`${this.resourceUrl}/${getItemLocationIdentifier(itemLocation) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IItemLocation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IItemLocation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addItemLocationToCollectionIfMissing(
    itemLocationCollection: IItemLocation[],
    ...itemLocationsToCheck: (IItemLocation | null | undefined)[]
  ): IItemLocation[] {
    const itemLocations: IItemLocation[] = itemLocationsToCheck.filter(isPresent);
    if (itemLocations.length > 0) {
      const itemLocationCollectionIdentifiers = itemLocationCollection.map(
        itemLocationItem => getItemLocationIdentifier(itemLocationItem)!
      );
      const itemLocationsToAdd = itemLocations.filter(itemLocationItem => {
        const itemLocationIdentifier = getItemLocationIdentifier(itemLocationItem);
        if (itemLocationIdentifier == null || itemLocationCollectionIdentifiers.includes(itemLocationIdentifier)) {
          return false;
        }
        itemLocationCollectionIdentifiers.push(itemLocationIdentifier);
        return true;
      });
      return [...itemLocationsToAdd, ...itemLocationCollection];
    }
    return itemLocationCollection;
  }

  protected convertDateFromClient(itemLocation: IItemLocation): IItemLocation {
    return Object.assign({}, itemLocation, {
      createdDate: itemLocation.createdDate?.isValid() ? itemLocation.createdDate.format(DATE_FORMAT) : undefined,
      lastModifiedDate: itemLocation.lastModifiedDate?.isValid() ? itemLocation.lastModifiedDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((itemLocation: IItemLocation) => {
        itemLocation.createdDate = itemLocation.createdDate ? dayjs(itemLocation.createdDate) : undefined;
        itemLocation.lastModifiedDate = itemLocation.lastModifiedDate ? dayjs(itemLocation.lastModifiedDate) : undefined;
      });
    }
    return res;
  }
}
