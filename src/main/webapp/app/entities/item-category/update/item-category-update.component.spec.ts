jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ItemCategoryService } from '../service/item-category.service';
import { IItemCategory, ItemCategory } from '../item-category.model';

import { ItemCategoryUpdateComponent } from './item-category-update.component';

describe('Component Tests', () => {
  describe('ItemCategory Management Update Component', () => {
    let comp: ItemCategoryUpdateComponent;
    let fixture: ComponentFixture<ItemCategoryUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let itemCategoryService: ItemCategoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ItemCategoryUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ItemCategoryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ItemCategoryUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      itemCategoryService = TestBed.inject(ItemCategoryService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const itemCategory: IItemCategory = { id: 456 };

        activatedRoute.data = of({ itemCategory });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(itemCategory));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const itemCategory = { id: 123 };
        spyOn(itemCategoryService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ itemCategory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: itemCategory }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(itemCategoryService.update).toHaveBeenCalledWith(itemCategory);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const itemCategory = new ItemCategory();
        spyOn(itemCategoryService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ itemCategory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: itemCategory }));
        saveSubject.complete();

        // THEN
        expect(itemCategoryService.create).toHaveBeenCalledWith(itemCategory);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const itemCategory = { id: 123 };
        spyOn(itemCategoryService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ itemCategory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(itemCategoryService.update).toHaveBeenCalledWith(itemCategory);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
