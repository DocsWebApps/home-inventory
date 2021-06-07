jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ItemService } from '../service/item.service';
import { IItem, Item } from '../item.model';
import { IItemCategory } from 'app/entities/item-category/item-category.model';
import { ItemCategoryService } from 'app/entities/item-category/service/item-category.service';
import { IItemOwner } from 'app/entities/item-owner/item-owner.model';
import { ItemOwnerService } from 'app/entities/item-owner/service/item-owner.service';
import { IItemLocation } from 'app/entities/item-location/item-location.model';
import { ItemLocationService } from 'app/entities/item-location/service/item-location.service';
import { IItemModel } from 'app/entities/item-model/item-model.model';
import { ItemModelService } from 'app/entities/item-model/service/item-model.service';

import { ItemUpdateComponent } from './item-update.component';

describe('Component Tests', () => {
  describe('Item Management Update Component', () => {
    let comp: ItemUpdateComponent;
    let fixture: ComponentFixture<ItemUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let itemService: ItemService;
    let itemCategoryService: ItemCategoryService;
    let itemOwnerService: ItemOwnerService;
    let itemLocationService: ItemLocationService;
    let itemModelService: ItemModelService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ItemUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ItemUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ItemUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      itemService = TestBed.inject(ItemService);
      itemCategoryService = TestBed.inject(ItemCategoryService);
      itemOwnerService = TestBed.inject(ItemOwnerService);
      itemLocationService = TestBed.inject(ItemLocationService);
      itemModelService = TestBed.inject(ItemModelService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ItemCategory query and add missing value', () => {
        const item: IItem = { id: 456 };
        const itemCategory: IItemCategory = { id: 81775 };
        item.itemCategory = itemCategory;

        const itemCategoryCollection: IItemCategory[] = [{ id: 4249 }];
        spyOn(itemCategoryService, 'query').and.returnValue(of(new HttpResponse({ body: itemCategoryCollection })));
        const additionalItemCategories = [itemCategory];
        const expectedCollection: IItemCategory[] = [...additionalItemCategories, ...itemCategoryCollection];
        spyOn(itemCategoryService, 'addItemCategoryToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ item });
        comp.ngOnInit();

        expect(itemCategoryService.query).toHaveBeenCalled();
        expect(itemCategoryService.addItemCategoryToCollectionIfMissing).toHaveBeenCalledWith(
          itemCategoryCollection,
          ...additionalItemCategories
        );
        expect(comp.itemCategoriesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ItemOwner query and add missing value', () => {
        const item: IItem = { id: 456 };
        const itemOwner: IItemOwner = { id: 52314 };
        item.itemOwner = itemOwner;

        const itemOwnerCollection: IItemOwner[] = [{ id: 7697 }];
        spyOn(itemOwnerService, 'query').and.returnValue(of(new HttpResponse({ body: itemOwnerCollection })));
        const additionalItemOwners = [itemOwner];
        const expectedCollection: IItemOwner[] = [...additionalItemOwners, ...itemOwnerCollection];
        spyOn(itemOwnerService, 'addItemOwnerToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ item });
        comp.ngOnInit();

        expect(itemOwnerService.query).toHaveBeenCalled();
        expect(itemOwnerService.addItemOwnerToCollectionIfMissing).toHaveBeenCalledWith(itemOwnerCollection, ...additionalItemOwners);
        expect(comp.itemOwnersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ItemLocation query and add missing value', () => {
        const item: IItem = { id: 456 };
        const itemLocation: IItemLocation = { id: 44796 };
        item.itemLocation = itemLocation;

        const itemLocationCollection: IItemLocation[] = [{ id: 39486 }];
        spyOn(itemLocationService, 'query').and.returnValue(of(new HttpResponse({ body: itemLocationCollection })));
        const additionalItemLocations = [itemLocation];
        const expectedCollection: IItemLocation[] = [...additionalItemLocations, ...itemLocationCollection];
        spyOn(itemLocationService, 'addItemLocationToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ item });
        comp.ngOnInit();

        expect(itemLocationService.query).toHaveBeenCalled();
        expect(itemLocationService.addItemLocationToCollectionIfMissing).toHaveBeenCalledWith(
          itemLocationCollection,
          ...additionalItemLocations
        );
        expect(comp.itemLocationsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ItemModel query and add missing value', () => {
        const item: IItem = { id: 456 };
        const itemModel: IItemModel = { id: 71681 };
        item.itemModel = itemModel;

        const itemModelCollection: IItemModel[] = [{ id: 98791 }];
        spyOn(itemModelService, 'query').and.returnValue(of(new HttpResponse({ body: itemModelCollection })));
        const additionalItemModels = [itemModel];
        const expectedCollection: IItemModel[] = [...additionalItemModels, ...itemModelCollection];
        spyOn(itemModelService, 'addItemModelToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ item });
        comp.ngOnInit();

        expect(itemModelService.query).toHaveBeenCalled();
        expect(itemModelService.addItemModelToCollectionIfMissing).toHaveBeenCalledWith(itemModelCollection, ...additionalItemModels);
        expect(comp.itemModelsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const item: IItem = { id: 456 };
        const itemCategory: IItemCategory = { id: 52101 };
        item.itemCategory = itemCategory;
        const itemOwner: IItemOwner = { id: 38846 };
        item.itemOwner = itemOwner;
        const itemLocation: IItemLocation = { id: 94146 };
        item.itemLocation = itemLocation;
        const itemModel: IItemModel = { id: 34753 };
        item.itemModel = itemModel;

        activatedRoute.data = of({ item });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(item));
        expect(comp.itemCategoriesSharedCollection).toContain(itemCategory);
        expect(comp.itemOwnersSharedCollection).toContain(itemOwner);
        expect(comp.itemLocationsSharedCollection).toContain(itemLocation);
        expect(comp.itemModelsSharedCollection).toContain(itemModel);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const item = { id: 123 };
        spyOn(itemService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ item });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: item }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(itemService.update).toHaveBeenCalledWith(item);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const item = new Item();
        spyOn(itemService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ item });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: item }));
        saveSubject.complete();

        // THEN
        expect(itemService.create).toHaveBeenCalledWith(item);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const item = { id: 123 };
        spyOn(itemService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ item });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(itemService.update).toHaveBeenCalledWith(item);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackItemCategoryById', () => {
        it('Should return tracked ItemCategory primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackItemCategoryById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackItemOwnerById', () => {
        it('Should return tracked ItemOwner primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackItemOwnerById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackItemLocationById', () => {
        it('Should return tracked ItemLocation primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackItemLocationById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackItemModelById', () => {
        it('Should return tracked ItemModel primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackItemModelById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
