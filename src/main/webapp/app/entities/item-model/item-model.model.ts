import * as dayjs from 'dayjs';
import { IItem } from 'app/entities/item/item.model';
import { IItemMake } from 'app/entities/item-make/item-make.model';

export interface IItemModel {
  id?: number;
  name?: string;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  items?: IItem[] | null;
  itemMake?: IItemMake;
}

export class ItemModel implements IItemModel {
  constructor(
    public id?: number,
    public name?: string,
    public createdDate?: dayjs.Dayjs | null,
    public lastModifiedDate?: dayjs.Dayjs | null,
    public items?: IItem[] | null,
    public itemMake?: IItemMake
  ) {}
}

export function getItemModelIdentifier(itemModel: IItemModel): number | undefined {
  return itemModel.id;
}
