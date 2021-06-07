jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IItemImage, ItemImage } from '../item-image.model';
import { ItemImageService } from '../service/item-image.service';

import { ItemImageRoutingResolveService } from './item-image-routing-resolve.service';

describe('Service Tests', () => {
  describe('ItemImage routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ItemImageRoutingResolveService;
    let service: ItemImageService;
    let resultItemImage: IItemImage | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ItemImageRoutingResolveService);
      service = TestBed.inject(ItemImageService);
      resultItemImage = undefined;
    });

    describe('resolve', () => {
      it('should return IItemImage returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultItemImage = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultItemImage).toEqual({ id: 123 });
      });

      it('should return new IItemImage if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultItemImage = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultItemImage).toEqual(new ItemImage());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultItemImage = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultItemImage).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
