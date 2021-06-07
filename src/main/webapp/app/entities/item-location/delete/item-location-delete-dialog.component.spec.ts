jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, fakeAsync, inject, TestBed, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ItemLocationService } from '../service/item-location.service';

import { ItemLocationDeleteDialogComponent } from './item-location-delete-dialog.component';

describe('Component Tests', () => {
  describe('ItemLocation Management Delete Component', () => {
    let comp: ItemLocationDeleteDialogComponent;
    let fixture: ComponentFixture<ItemLocationDeleteDialogComponent>;
    let service: ItemLocationService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ItemLocationDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(ItemLocationDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ItemLocationDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ItemLocationService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
