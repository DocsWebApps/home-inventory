jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ItemLocationService } from '../service/item-location.service';
import { IItemLocation, ItemLocation } from '../item-location.model';

import { ItemLocationUpdateComponent } from './item-location-update.component';

describe('Component Tests', () => {
  describe('ItemLocation Management Update Component', () => {
    let comp: ItemLocationUpdateComponent;
    let fixture: ComponentFixture<ItemLocationUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let itemLocationService: ItemLocationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ItemLocationUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ItemLocationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ItemLocationUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      itemLocationService = TestBed.inject(ItemLocationService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const itemLocation: IItemLocation = { id: 456 };

        activatedRoute.data = of({ itemLocation });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(itemLocation));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const itemLocation = { id: 123 };
        spyOn(itemLocationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ itemLocation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: itemLocation }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(itemLocationService.update).toHaveBeenCalledWith(itemLocation);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const itemLocation = new ItemLocation();
        spyOn(itemLocationService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ itemLocation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: itemLocation }));
        saveSubject.complete();

        // THEN
        expect(itemLocationService.create).toHaveBeenCalledWith(itemLocation);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const itemLocation = { id: 123 };
        spyOn(itemLocationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ itemLocation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(itemLocationService.update).toHaveBeenCalledWith(itemLocation);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
