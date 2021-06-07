import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IItemLocation, ItemLocation } from '../item-location.model';

import { ItemLocationService } from './item-location.service';

describe('Service Tests', () => {
  describe('ItemLocation Service', () => {
    let service: ItemLocationService;
    let httpMock: HttpTestingController;
    let elemDefault: IItemLocation;
    let expectedResult: IItemLocation | IItemLocation[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ItemLocationService);
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

      it('should create a ItemLocation', () => {
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

        service.create(new ItemLocation()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ItemLocation', () => {
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

      it('should partial update a ItemLocation', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
            createdDate: currentDate.format(DATE_FORMAT),
          },
          new ItemLocation()
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

      it('should return a list of ItemLocation', () => {
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

      it('should delete a ItemLocation', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addItemLocationToCollectionIfMissing', () => {
        it('should add a ItemLocation to an empty array', () => {
          const itemLocation: IItemLocation = { id: 123 };
          expectedResult = service.addItemLocationToCollectionIfMissing([], itemLocation);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(itemLocation);
        });

        it('should not add a ItemLocation to an array that contains it', () => {
          const itemLocation: IItemLocation = { id: 123 };
          const itemLocationCollection: IItemLocation[] = [
            {
              ...itemLocation,
            },
            { id: 456 },
          ];
          expectedResult = service.addItemLocationToCollectionIfMissing(itemLocationCollection, itemLocation);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ItemLocation to an array that doesn't contain it", () => {
          const itemLocation: IItemLocation = { id: 123 };
          const itemLocationCollection: IItemLocation[] = [{ id: 456 }];
          expectedResult = service.addItemLocationToCollectionIfMissing(itemLocationCollection, itemLocation);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(itemLocation);
        });

        it('should add only unique ItemLocation to an array', () => {
          const itemLocationArray: IItemLocation[] = [{ id: 123 }, { id: 456 }, { id: 51735 }];
          const itemLocationCollection: IItemLocation[] = [{ id: 123 }];
          expectedResult = service.addItemLocationToCollectionIfMissing(itemLocationCollection, ...itemLocationArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const itemLocation: IItemLocation = { id: 123 };
          const itemLocation2: IItemLocation = { id: 456 };
          expectedResult = service.addItemLocationToCollectionIfMissing([], itemLocation, itemLocation2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(itemLocation);
          expect(expectedResult).toContain(itemLocation2);
        });

        it('should accept null and undefined values', () => {
          const itemLocation: IItemLocation = { id: 123 };
          expectedResult = service.addItemLocationToCollectionIfMissing([], null, itemLocation, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(itemLocation);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
