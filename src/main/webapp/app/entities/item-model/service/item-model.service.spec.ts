import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IItemModel, ItemModel } from '../item-model.model';

import { ItemModelService } from './item-model.service';

describe('Service Tests', () => {
  describe('ItemModel Service', () => {
    let service: ItemModelService;
    let httpMock: HttpTestingController;
    let elemDefault: IItemModel;
    let expectedResult: IItemModel | IItemModel[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ItemModelService);
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

      it('should create a ItemModel', () => {
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

        service.create(new ItemModel()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ItemModel', () => {
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

      it('should partial update a ItemModel', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
          },
          new ItemModel()
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

      it('should return a list of ItemModel', () => {
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

      it('should delete a ItemModel', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addItemModelToCollectionIfMissing', () => {
        it('should add a ItemModel to an empty array', () => {
          const itemModel: IItemModel = { id: 123 };
          expectedResult = service.addItemModelToCollectionIfMissing([], itemModel);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(itemModel);
        });

        it('should not add a ItemModel to an array that contains it', () => {
          const itemModel: IItemModel = { id: 123 };
          const itemModelCollection: IItemModel[] = [
            {
              ...itemModel,
            },
            { id: 456 },
          ];
          expectedResult = service.addItemModelToCollectionIfMissing(itemModelCollection, itemModel);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ItemModel to an array that doesn't contain it", () => {
          const itemModel: IItemModel = { id: 123 };
          const itemModelCollection: IItemModel[] = [{ id: 456 }];
          expectedResult = service.addItemModelToCollectionIfMissing(itemModelCollection, itemModel);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(itemModel);
        });

        it('should add only unique ItemModel to an array', () => {
          const itemModelArray: IItemModel[] = [{ id: 123 }, { id: 456 }, { id: 31774 }];
          const itemModelCollection: IItemModel[] = [{ id: 123 }];
          expectedResult = service.addItemModelToCollectionIfMissing(itemModelCollection, ...itemModelArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const itemModel: IItemModel = { id: 123 };
          const itemModel2: IItemModel = { id: 456 };
          expectedResult = service.addItemModelToCollectionIfMissing([], itemModel, itemModel2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(itemModel);
          expect(expectedResult).toContain(itemModel2);
        });

        it('should accept null and undefined values', () => {
          const itemModel: IItemModel = { id: 123 };
          expectedResult = service.addItemModelToCollectionIfMissing([], null, itemModel, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(itemModel);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
