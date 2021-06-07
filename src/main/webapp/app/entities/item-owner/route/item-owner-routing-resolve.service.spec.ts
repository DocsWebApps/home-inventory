jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IItemOwner, ItemOwner } from '../item-owner.model';
import { ItemOwnerService } from '../service/item-owner.service';

import { ItemOwnerRoutingResolveService } from './item-owner-routing-resolve.service';

describe('Service Tests', () => {
  describe('ItemOwner routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ItemOwnerRoutingResolveService;
    let service: ItemOwnerService;
    let resultItemOwner: IItemOwner | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ItemOwnerRoutingResolveService);
      service = TestBed.inject(ItemOwnerService);
      resultItemOwner = undefined;
    });

    describe('resolve', () => {
      it('should return IItemOwner returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultItemOwner = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultItemOwner).toEqual({ id: 123 });
      });

      it('should return new IItemOwner if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultItemOwner = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultItemOwner).toEqual(new ItemOwner());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultItemOwner = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultItemOwner).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
