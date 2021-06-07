jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ItemMakeService } from '../service/item-make.service';
import { IItemMake, ItemMake } from '../item-make.model';

import { ItemMakeUpdateComponent } from './item-make-update.component';

describe('Component Tests', () => {
  describe('ItemMake Management Update Component', () => {
    let comp: ItemMakeUpdateComponent;
    let fixture: ComponentFixture<ItemMakeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let itemMakeService: ItemMakeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ItemMakeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ItemMakeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ItemMakeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      itemMakeService = TestBed.inject(ItemMakeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const itemMake: IItemMake = { id: 456 };

        activatedRoute.data = of({ itemMake });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(itemMake));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const itemMake = { id: 123 };
        spyOn(itemMakeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ itemMake });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: itemMake }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(itemMakeService.update).toHaveBeenCalledWith(itemMake);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const itemMake = new ItemMake();
        spyOn(itemMakeService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ itemMake });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: itemMake }));
        saveSubject.complete();

        // THEN
        expect(itemMakeService.create).toHaveBeenCalledWith(itemMake);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const itemMake = { id: 123 };
        spyOn(itemMakeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ itemMake });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(itemMakeService.update).toHaveBeenCalledWith(itemMake);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
