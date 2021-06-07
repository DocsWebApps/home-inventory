jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ItemModelService } from '../service/item-model.service';
import { IItemModel, ItemModel } from '../item-model.model';
import { IItemMake } from 'app/entities/item-make/item-make.model';
import { ItemMakeService } from 'app/entities/item-make/service/item-make.service';

import { ItemModelUpdateComponent } from './item-model-update.component';

describe('Component Tests', () => {
  describe('ItemModel Management Update Component', () => {
    let comp: ItemModelUpdateComponent;
    let fixture: ComponentFixture<ItemModelUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let itemModelService: ItemModelService;
    let itemMakeService: ItemMakeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ItemModelUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ItemModelUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ItemModelUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      itemModelService = TestBed.inject(ItemModelService);
      itemMakeService = TestBed.inject(ItemMakeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ItemMake query and add missing value', () => {
        const itemModel: IItemModel = { id: 456 };
        const itemMake: IItemMake = { id: 26339 };
        itemModel.itemMake = itemMake;

        const itemMakeCollection: IItemMake[] = [{ id: 33774 }];
        spyOn(itemMakeService, 'query').and.returnValue(of(new HttpResponse({ body: itemMakeCollection })));
        const additionalItemMakes = [itemMake];
        const expectedCollection: IItemMake[] = [...additionalItemMakes, ...itemMakeCollection];
        spyOn(itemMakeService, 'addItemMakeToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ itemModel });
        comp.ngOnInit();

        expect(itemMakeService.query).toHaveBeenCalled();
        expect(itemMakeService.addItemMakeToCollectionIfMissing).toHaveBeenCalledWith(itemMakeCollection, ...additionalItemMakes);
        expect(comp.itemMakesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const itemModel: IItemModel = { id: 456 };
        const itemMake: IItemMake = { id: 33547 };
        itemModel.itemMake = itemMake;

        activatedRoute.data = of({ itemModel });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(itemModel));
        expect(comp.itemMakesSharedCollection).toContain(itemMake);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const itemModel = { id: 123 };
        spyOn(itemModelService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ itemModel });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: itemModel }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(itemModelService.update).toHaveBeenCalledWith(itemModel);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const itemModel = new ItemModel();
        spyOn(itemModelService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ itemModel });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: itemModel }));
        saveSubject.complete();

        // THEN
        expect(itemModelService.create).toHaveBeenCalledWith(itemModel);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const itemModel = { id: 123 };
        spyOn(itemModelService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ itemModel });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(itemModelService.update).toHaveBeenCalledWith(itemModel);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackItemMakeById', () => {
        it('Should return tracked ItemMake primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackItemMakeById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
