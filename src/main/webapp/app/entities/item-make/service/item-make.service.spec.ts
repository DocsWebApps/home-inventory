import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IItemMake, ItemMake } from '../item-make.model';

import { ItemMakeService } from './item-make.service';

describe('Service Tests', () => {
  describe('ItemMake Service', () => {
    let service: ItemMakeService;
    let httpMock: HttpTestingController;
    let elemDefault: IItemMake;
    let expectedResult: IItemMake | IItemMake[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ItemMakeService);
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

      it('should create a ItemMake', () => {
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

        service.create(new ItemMake()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ItemMake', () => {
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

      it('should partial update a ItemMake', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
          },
          new ItemMake()
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

      it('should return a list of ItemMake', () => {
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

      it('should delete a ItemMake', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addItemMakeToCollectionIfMissing', () => {
        it('should add a ItemMake to an empty array', () => {
          const itemMake: IItemMake = { id: 123 };
          expectedResult = service.addItemMakeToCollectionIfMissing([], itemMake);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(itemMake);
        });

        it('should not add a ItemMake to an array that contains it', () => {
          const itemMake: IItemMake = { id: 123 };
          const itemMakeCollection: IItemMake[] = [
            {
              ...itemMake,
            },
            { id: 456 },
          ];
          expectedResult = service.addItemMakeToCollectionIfMissing(itemMakeCollection, itemMake);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ItemMake to an array that doesn't contain it", () => {
          const itemMake: IItemMake = { id: 123 };
          const itemMakeCollection: IItemMake[] = [{ id: 456 }];
          expectedResult = service.addItemMakeToCollectionIfMissing(itemMakeCollection, itemMake);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(itemMake);
        });

        it('should add only unique ItemMake to an array', () => {
          const itemMakeArray: IItemMake[] = [{ id: 123 }, { id: 456 }, { id: 52990 }];
          const itemMakeCollection: IItemMake[] = [{ id: 123 }];
          expectedResult = service.addItemMakeToCollectionIfMissing(itemMakeCollection, ...itemMakeArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const itemMake: IItemMake = { id: 123 };
          const itemMake2: IItemMake = { id: 456 };
          expectedResult = service.addItemMakeToCollectionIfMissing([], itemMake, itemMake2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(itemMake);
          expect(expectedResult).toContain(itemMake2);
        });

        it('should accept null and undefined values', () => {
          const itemMake: IItemMake = { id: 123 };
          expectedResult = service.addItemMakeToCollectionIfMissing([], null, itemMake, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(itemMake);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
