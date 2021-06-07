import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IItemCategory, ItemCategory } from '../item-category.model';

import { ItemCategoryService } from './item-category.service';

describe('Service Tests', () => {
  describe('ItemCategory Service', () => {
    let service: ItemCategoryService;
    let httpMock: HttpTestingController;
    let elemDefault: IItemCategory;
    let expectedResult: IItemCategory | IItemCategory[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ItemCategoryService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
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

      it('should create a ItemCategory', () => {
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

        service.create(new ItemCategory()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ItemCategory', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
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

      it('should partial update a ItemCategory', () => {
        const patchObject = Object.assign(
          {
            lastModifiedDate: currentDate.format(DATE_FORMAT),
          },
          new ItemCategory()
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

      it('should return a list of ItemCategory', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
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

      it('should delete a ItemCategory', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addItemCategoryToCollectionIfMissing', () => {
        it('should add a ItemCategory to an empty array', () => {
          const itemCategory: IItemCategory = { id: 123 };
          expectedResult = service.addItemCategoryToCollectionIfMissing([], itemCategory);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(itemCategory);
        });

        it('should not add a ItemCategory to an array that contains it', () => {
          const itemCategory: IItemCategory = { id: 123 };
          const itemCategoryCollection: IItemCategory[] = [
            {
              ...itemCategory,
            },
            { id: 456 },
          ];
          expectedResult = service.addItemCategoryToCollectionIfMissing(itemCategoryCollection, itemCategory);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ItemCategory to an array that doesn't contain it", () => {
          const itemCategory: IItemCategory = { id: 123 };
          const itemCategoryCollection: IItemCategory[] = [{ id: 456 }];
          expectedResult = service.addItemCategoryToCollectionIfMissing(itemCategoryCollection, itemCategory);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(itemCategory);
        });

        it('should add only unique ItemCategory to an array', () => {
          const itemCategoryArray: IItemCategory[] = [{ id: 123 }, { id: 456 }, { id: 47530 }];
          const itemCategoryCollection: IItemCategory[] = [{ id: 123 }];
          expectedResult = service.addItemCategoryToCollectionIfMissing(itemCategoryCollection, ...itemCategoryArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const itemCategory: IItemCategory = { id: 123 };
          const itemCategory2: IItemCategory = { id: 456 };
          expectedResult = service.addItemCategoryToCollectionIfMissing([], itemCategory, itemCategory2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(itemCategory);
          expect(expectedResult).toContain(itemCategory2);
        });

        it('should accept null and undefined values', () => {
          const itemCategory: IItemCategory = { id: 123 };
          expectedResult = service.addItemCategoryToCollectionIfMissing([], null, itemCategory, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(itemCategory);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
