import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { getItemModelIdentifier, IItemModel } from '../item-model.model';

export type EntityResponseType = HttpResponse<IItemModel>;
export type EntityArrayResponseType = HttpResponse<IItemModel[]>;

@Injectable({ providedIn: 'root' })
export class ItemModelService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/item-models');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(itemModel: IItemModel): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(itemModel);
    return this.http
      .post<IItemModel>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(itemModel: IItemModel): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(itemModel);
    return this.http
      .put<IItemModel>(`${this.resourceUrl}/${getItemModelIdentifier(itemModel) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(itemModel: IItemModel): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(itemModel);
    return this.http
      .patch<IItemModel>(`${this.resourceUrl}/${getItemModelIdentifier(itemModel) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IItemModel>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IItemModel[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addItemModelToCollectionIfMissing(
    itemModelCollection: IItemModel[],
    ...itemModelsToCheck: (IItemModel | null | undefined)[]
  ): IItemModel[] {
    const itemModels: IItemModel[] = itemModelsToCheck.filter(isPresent);
    if (itemModels.length > 0) {
      const itemModelCollectionIdentifiers = itemModelCollection.map(itemModelItem => getItemModelIdentifier(itemModelItem)!);
      const itemModelsToAdd = itemModels.filter(itemModelItem => {
        const itemModelIdentifier = getItemModelIdentifier(itemModelItem);
        if (itemModelIdentifier == null || itemModelCollectionIdentifiers.includes(itemModelIdentifier)) {
          return false;
        }
        itemModelCollectionIdentifiers.push(itemModelIdentifier);
        return true;
      });
      return [...itemModelsToAdd, ...itemModelCollection];
    }
    return itemModelCollection;
  }

  protected convertDateFromClient(itemModel: IItemModel): IItemModel {
    return Object.assign({}, itemModel, {
      createdDate: itemModel.createdDate?.isValid() ? itemModel.createdDate.format(DATE_FORMAT) : undefined,
      lastModifiedDate: itemModel.lastModifiedDate?.isValid() ? itemModel.lastModifiedDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((itemModel: IItemModel) => {
        itemModel.createdDate = itemModel.createdDate ? dayjs(itemModel.createdDate) : undefined;
        itemModel.lastModifiedDate = itemModel.lastModifiedDate ? dayjs(itemModel.lastModifiedDate) : undefined;
      });
    }
    return res;
  }
}
