import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { getItemImageIdentifier, IItemImage } from '../item-image.model';

export type EntityResponseType = HttpResponse<IItemImage>;
export type EntityArrayResponseType = HttpResponse<IItemImage[]>;

@Injectable({ providedIn: 'root' })
export class ItemImageService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/item-images');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(itemImage: IItemImage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(itemImage);
    return this.http
      .post<IItemImage>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(itemImage: IItemImage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(itemImage);
    return this.http
      .put<IItemImage>(`${this.resourceUrl}/${getItemImageIdentifier(itemImage) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(itemImage: IItemImage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(itemImage);
    return this.http
      .patch<IItemImage>(`${this.resourceUrl}/${getItemImageIdentifier(itemImage) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IItemImage>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IItemImage[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addItemImageToCollectionIfMissing(
    itemImageCollection: IItemImage[],
    ...itemImagesToCheck: (IItemImage | null | undefined)[]
  ): IItemImage[] {
    const itemImages: IItemImage[] = itemImagesToCheck.filter(isPresent);
    if (itemImages.length > 0) {
      const itemImageCollectionIdentifiers = itemImageCollection.map(itemImageItem => getItemImageIdentifier(itemImageItem)!);
      const itemImagesToAdd = itemImages.filter(itemImageItem => {
        const itemImageIdentifier = getItemImageIdentifier(itemImageItem);
        if (itemImageIdentifier == null || itemImageCollectionIdentifiers.includes(itemImageIdentifier)) {
          return false;
        }
        itemImageCollectionIdentifiers.push(itemImageIdentifier);
        return true;
      });
      return [...itemImagesToAdd, ...itemImageCollection];
    }
    return itemImageCollection;
  }

  protected convertDateFromClient(itemImage: IItemImage): IItemImage {
    return Object.assign({}, itemImage, {
      createdDate: itemImage.createdDate?.isValid() ? itemImage.createdDate.format(DATE_FORMAT) : undefined,
      lastModifiedDate: itemImage.lastModifiedDate?.isValid() ? itemImage.lastModifiedDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((itemImage: IItemImage) => {
        itemImage.createdDate = itemImage.createdDate ? dayjs(itemImage.createdDate) : undefined;
        itemImage.lastModifiedDate = itemImage.lastModifiedDate ? dayjs(itemImage.lastModifiedDate) : undefined;
      });
    }
    return res;
  }
}
