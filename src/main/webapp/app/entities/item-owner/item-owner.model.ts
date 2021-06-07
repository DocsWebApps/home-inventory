import * as dayjs from 'dayjs';
import { IItem } from 'app/entities/item/item.model';

export interface IItemOwner {
  id?: number;
  name?: string;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  items?: IItem[] | null;
}

export class ItemOwner implements IItemOwner {
  constructor(
    public id?: number,
    public name?: string,
    public createdDate?: dayjs.Dayjs | null,
    public lastModifiedDate?: dayjs.Dayjs | null,
    public items?: IItem[] | null
  ) {}
}

export function getItemOwnerIdentifier(itemOwner: IItemOwner): number | undefined {
  return itemOwner.id;
}
