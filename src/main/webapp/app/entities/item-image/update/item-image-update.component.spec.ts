jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ItemImageService } from '../service/item-image.service';
import { IItemImage, ItemImage } from '../item-image.model';
import { IItem } from 'app/entities/item/item.model';
import { ItemService } from 'app/entities/item/service/item.service';

import { ItemImageUpdateComponent } from './item-image-update.component';

describe('Component Tests', () => {
  describe('ItemImage Management Update Component', () => {
    let comp: ItemImageUpdateComponent;
    let fixture: ComponentFixture<ItemImageUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let itemImageService: ItemImageService;
    let itemService: ItemService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ItemImageUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ItemImageUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ItemImageUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      itemImageService = TestBed.inject(ItemImageService);
      itemService = TestBed.inject(ItemService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Item query and add missing value', () => {
        const itemImage: IItemImage = { id: 456 };
        const item: IItem = { id: 63865 };
        itemImage.item = item;

        const itemCollection: IItem[] = [{ id: 13740 }];
        spyOn(itemService, 'query').and.returnValue(of(new HttpResponse({ body: itemCollection })));
        const additionalItems = [item];
        const expectedCollection: IItem[] = [...additionalItems, ...itemCollection];
        spyOn(itemService, 'addItemToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ itemImage });
        comp.ngOnInit();

        expect(itemService.query).toHaveBeenCalled();
        expect(itemService.addItemToCollectionIfMissing).toHaveBeenCalledWith(itemCollection, ...additionalItems);
        expect(comp.itemsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const itemImage: IItemImage = { id: 456 };
        const item: IItem = { id: 21064 };
        itemImage.item = item;

        activatedRoute.data = of({ itemImage });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(itemImage));
        expect(comp.itemsSharedCollection).toContain(item);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const itemImage = { id: 123 };
        spyOn(itemImageService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ itemImage });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: itemImage }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(itemImageService.update).toHaveBeenCalledWith(itemImage);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const itemImage = new ItemImage();
        spyOn(itemImageService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ itemImage });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: itemImage }));
        saveSubject.complete();

        // THEN
        expect(itemImageService.create).toHaveBeenCalledWith(itemImage);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const itemImage = { id: 123 };
        spyOn(itemImageService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ itemImage });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(itemImageService.update).toHaveBeenCalledWith(itemImage);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackItemById', () => {
        it('Should return tracked Item primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackItemById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
