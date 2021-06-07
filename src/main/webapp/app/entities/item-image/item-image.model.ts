import * as dayjs from 'dayjs';
import { IItem } from 'app/entities/item/item.model';

export interface IItemImage {
  id?: number;
  name?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  item?: IItem;
}

export class ItemImage implements IItemImage {
  constructor(
    public id?: number,
    public name?: string | null,
    public imageContentType?: string | null,
    public image?: string | null,
    public createdDate?: dayjs.Dayjs | null,
    public lastModifiedDate?: dayjs.Dayjs | null,
    public item?: IItem
  ) {}
}

export function getItemImageIdentifier(itemImage: IItemImage): number | undefined {
  return itemImage.id;
}
