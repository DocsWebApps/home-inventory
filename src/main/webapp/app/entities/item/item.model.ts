import * as dayjs from 'dayjs';
import { IItemImage } from 'app/entities/item-image/item-image.model';
import { IItemCategory } from 'app/entities/item-category/item-category.model';
import { IItemOwner } from 'app/entities/item-owner/item-owner.model';
import { IItemLocation } from 'app/entities/item-location/item-location.model';
import { IItemModel } from 'app/entities/item-model/item-model.model';

export interface IItem {
  id?: number;
  cost?: number | null;
  isCostEstimate?: boolean;
  serialNumber?: string | null;
  purchaseDate?: dayjs.Dayjs;
  isPurchaseDateEstimate?: boolean;
  haveReceipt?: boolean;
  additionalInfo?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  itemImages?: IItemImage[] | null;
  itemCategory?: IItemCategory;
  itemOwner?: IItemOwner;
  itemLocation?: IItemLocation;
  itemModel?: IItemModel;
}

export class Item implements IItem {
  constructor(
    public id?: number,
    public cost?: number | null,
    public isCostEstimate?: boolean,
    public serialNumber?: string | null,
    public purchaseDate?: dayjs.Dayjs,
    public isPurchaseDateEstimate?: boolean,
    public haveReceipt?: boolean,
    public additionalInfo?: string | null,
    public createdDate?: dayjs.Dayjs | null,
    public lastModifiedDate?: dayjs.Dayjs | null,
    public itemImages?: IItemImage[] | null,
    public itemCategory?: IItemCategory,
    public itemOwner?: IItemOwner,
    public itemLocation?: IItemLocation,
    public itemModel?: IItemModel
  ) {
    this.isCostEstimate = this.isCostEstimate ?? false;
    this.isPurchaseDateEstimate = this.isPurchaseDateEstimate ?? false;
    this.haveReceipt = this.haveReceipt ?? false;
  }
}

export function getItemIdentifier(item: IItem): number | undefined {
  return item.id;
}
