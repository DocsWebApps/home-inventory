jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IItemCategory, ItemCategory } from '../item-category.model';
import { ItemCategoryService } from '../service/item-category.service';

import { ItemCategoryRoutingResolveService } from './item-category-routing-resolve.service';

describe('Service Tests', () => {
  describe('ItemCategory routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ItemCategoryRoutingResolveService;
    let service: ItemCategoryService;
    let resultItemCategory: IItemCategory | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ItemCategoryRoutingResolveService);
      service = TestBed.inject(ItemCategoryService);
      resultItemCategory = undefined;
    });

    describe('resolve', () => {
      it('should return IItemCategory returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultItemCategory = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultItemCategory).toEqual({ id: 123 });
      });

      it('should return new IItemCategory if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultItemCategory = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultItemCategory).toEqual(new ItemCategory());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultItemCategory = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultItemCategory).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
