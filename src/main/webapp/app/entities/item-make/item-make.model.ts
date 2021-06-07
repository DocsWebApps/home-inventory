import * as dayjs from 'dayjs';
import { IItemModel } from 'app/entities/item-model/item-model.model';

export interface IItemMake {
  id?: number;
  name?: string;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  itemModels?: IItemModel[] | null;
}

export class ItemMake implements IItemMake {
  constructor(
    public id?: number,
    public name?: string,
    public createdDate?: dayjs.Dayjs | null,
    public lastModifiedDate?: dayjs.Dayjs | null,
    public itemModels?: IItemModel[] | null
  ) {}
}

export function getItemMakeIdentifier(itemMake: IItemMake): number | undefined {
  return itemMake.id;
}
