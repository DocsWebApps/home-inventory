import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IItemImage, ItemImage } from '../item-image.model';

import { ItemImageService } from './item-image.service';

describe('Service Tests', () => {
  describe('ItemImage Service', () => {
    let service: ItemImageService;
    let httpMock: HttpTestingController;
    let elemDefault: IItemImage;
    let expectedResult: IItemImage | IItemImage[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ItemImageService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        imageContentType: 'image/png',
        image: 'AAAAAAA',
        createdDate: currentDate,
        lastModifiedDate: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            createdDate: currentDate.format(DATE_FORMAT),
            lastModifiedDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ItemImage', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            createdDate: currentDate.format(DATE_FORMAT),
            lastModifiedDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdDate: currentDate,
            lastModifiedDate: currentDate,
          },
          returnedFromService
        );

        service.create(new ItemImage()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ItemImage', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            image: 'BBBBBB',
            createdDate: currentDate.format(DATE_FORMAT),
            lastModifiedDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdDate: currentDate,
            lastModifiedDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ItemImage', () => {
        const patchObject = Object.assign(
          {
            createdDate: currentDate.format(DATE_FORMAT),
            lastModifiedDate: currentDate.format(DATE_FORMAT),
          },
          new ItemImage()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            createdDate: currentDate,
            lastModifiedDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ItemImage', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            image: 'BBBBBB',
            createdDate: currentDate.format(DATE_FORMAT),
            lastModifiedDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdDate: currentDate,
            lastModifiedDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ItemImage', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addItemImageToCollectionIfMissing', () => {
        it('should add a ItemImage to an empty array', () => {
          const itemImage: IItemImage = { id: 123 };
          expectedResult = service.addItemImageToCollectionIfMissing([], itemImage);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(itemImage);
        });

        it('should not add a ItemImage to an array that contains it', () => {
          const itemImage: IItemImage = { id: 123 };
          const itemImageCollection: IItemImage[] = [
            {
              ...itemImage,
            },
            { id: 456 },
          ];
          expectedResult = service.addItemImageToCollectionIfMissing(itemImageCollection, itemImage);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ItemImage to an array that doesn't contain it", () => {
          const itemImage: IItemImage = { id: 123 };
          const itemImageCollection: IItemImage[] = [{ id: 456 }];
          expectedResult = service.addItemImageToCollectionIfMissing(itemImageCollection, itemImage);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(itemImage);
        });

        it('should add only unique ItemImage to an array', () => {
          const itemImageArray: IItemImage[] = [{ id: 123 }, { id: 456 }, { id: 85620 }];
          const itemImageCollection: IItemImage[] = [{ id: 123 }];
          expectedResult = service.addItemImageToCollectionIfMissing(itemImageCollection, ...itemImageArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const itemImage: IItemImage = { id: 123 };
          const itemImage2: IItemImage = { id: 456 };
          expectedResult = service.addItemImageToCollectionIfMissing([], itemImage, itemImage2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(itemImage);
          expect(expectedResult).toContain(itemImage2);
        });

        it('should accept null and undefined values', () => {
          const itemImage: IItemImage = { id: 123 };
          expectedResult = service.addItemImageToCollectionIfMissing([], null, itemImage, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(itemImage);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
