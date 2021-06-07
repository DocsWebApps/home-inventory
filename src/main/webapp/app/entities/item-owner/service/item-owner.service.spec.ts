import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IItemOwner, ItemOwner } from '../item-owner.model';

import { ItemOwnerService } from './item-owner.service';

describe('Service Tests', () => {
  describe('ItemOwner Service', () => {
    let service: ItemOwnerService;
    let httpMock: HttpTestingController;
    let elemDefault: IItemOwner;
    let expectedResult: IItemOwner | IItemOwner[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ItemOwnerService);
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

      it('should create a ItemOwner', () => {
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

        service.create(new ItemOwner()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ItemOwner', () => {
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

      it('should partial update a ItemOwner', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
          },
          new ItemOwner()
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

      it('should return a list of ItemOwner', () => {
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

      it('should delete a ItemOwner', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addItemOwnerToCollectionIfMissing', () => {
        it('should add a ItemOwner to an empty array', () => {
          const itemOwner: IItemOwner = { id: 123 };
          expectedResult = service.addItemOwnerToCollectionIfMissing([], itemOwner);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(itemOwner);
        });

        it('should not add a ItemOwner to an array that contains it', () => {
          const itemOwner: IItemOwner = { id: 123 };
          const itemOwnerCollection: IItemOwner[] = [
            {
              ...itemOwner,
            },
            { id: 456 },
          ];
          expectedResult = service.addItemOwnerToCollectionIfMissing(itemOwnerCollection, itemOwner);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ItemOwner to an array that doesn't contain it", () => {
          const itemOwner: IItemOwner = { id: 123 };
          const itemOwnerCollection: IItemOwner[] = [{ id: 456 }];
          expectedResult = service.addItemOwnerToCollectionIfMissing(itemOwnerCollection, itemOwner);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(itemOwner);
        });

        it('should add only unique ItemOwner to an array', () => {
          const itemOwnerArray: IItemOwner[] = [{ id: 123 }, { id: 456 }, { id: 13935 }];
          const itemOwnerCollection: IItemOwner[] = [{ id: 123 }];
          expectedResult = service.addItemOwnerToCollectionIfMissing(itemOwnerCollection, ...itemOwnerArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const itemOwner: IItemOwner = { id: 123 };
          const itemOwner2: IItemOwner = { id: 456 };
          expectedResult = service.addItemOwnerToCollectionIfMissing([], itemOwner, itemOwner2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(itemOwner);
          expect(expectedResult).toContain(itemOwner2);
        });

        it('should accept null and undefined values', () => {
          const itemOwner: IItemOwner = { id: 123 };
          expectedResult = service.addItemOwnerToCollectionIfMissing([], null, itemOwner, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(itemOwner);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
