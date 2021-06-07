jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IItemLocation, ItemLocation } from '../item-location.model';
import { ItemLocationService } from '../service/item-location.service';

import { ItemLocationRoutingResolveService } from './item-location-routing-resolve.service';

describe('Service Tests', () => {
  describe('ItemLocation routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ItemLocationRoutingResolveService;
    let service: ItemLocationService;
    let resultItemLocation: IItemLocation | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ItemLocationRoutingResolveService);
      service = TestBed.inject(ItemLocationService);
      resultItemLocation = undefined;
    });

    describe('resolve', () => {
      it('should return IItemLocation returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultItemLocation = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultItemLocation).toEqual({ id: 123 });
      });

      it('should return new IItemLocation if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultItemLocation = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultItemLocation).toEqual(new ItemLocation());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultItemLocation = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultItemLocation).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
