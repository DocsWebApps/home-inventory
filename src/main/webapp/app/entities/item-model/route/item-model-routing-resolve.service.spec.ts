jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IItemModel, ItemModel } from '../item-model.model';
import { ItemModelService } from '../service/item-model.service';

import { ItemModelRoutingResolveService } from './item-model-routing-resolve.service';

describe('Service Tests', () => {
  describe('ItemModel routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ItemModelRoutingResolveService;
    let service: ItemModelService;
    let resultItemModel: IItemModel | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ItemModelRoutingResolveService);
      service = TestBed.inject(ItemModelService);
      resultItemModel = undefined;
    });

    describe('resolve', () => {
      it('should return IItemModel returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultItemModel = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultItemModel).toEqual({ id: 123 });
      });

      it('should return new IItemModel if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultItemModel = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultItemModel).toEqual(new ItemModel());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultItemModel = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultItemModel).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
