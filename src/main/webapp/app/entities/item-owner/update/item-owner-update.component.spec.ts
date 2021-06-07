jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ItemOwnerService } from '../service/item-owner.service';
import { IItemOwner, ItemOwner } from '../item-owner.model';

import { ItemOwnerUpdateComponent } from './item-owner-update.component';

describe('Component Tests', () => {
  describe('ItemOwner Management Update Component', () => {
    let comp: ItemOwnerUpdateComponent;
    let fixture: ComponentFixture<ItemOwnerUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let itemOwnerService: ItemOwnerService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ItemOwnerUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ItemOwnerUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ItemOwnerUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      itemOwnerService = TestBed.inject(ItemOwnerService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const itemOwner: IItemOwner = { id: 456 };

        activatedRoute.data = of({ itemOwner });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(itemOwner));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const itemOwner = { id: 123 };
        spyOn(itemOwnerService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ itemOwner });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: itemOwner }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(itemOwnerService.update).toHaveBeenCalledWith(itemOwner);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const itemOwner = new ItemOwner();
        spyOn(itemOwnerService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ itemOwner });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: itemOwner }));
        saveSubject.complete();

        // THEN
        expect(itemOwnerService.create).toHaveBeenCalledWith(itemOwner);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const itemOwner = { id: 123 };
        spyOn(itemOwnerService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ itemOwner });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(itemOwnerService.update).toHaveBeenCalledWith(itemOwner);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
