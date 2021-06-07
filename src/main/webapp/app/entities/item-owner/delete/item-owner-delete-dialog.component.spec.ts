jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, fakeAsync, inject, TestBed, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ItemOwnerService } from '../service/item-owner.service';

import { ItemOwnerDeleteDialogComponent } from './item-owner-delete-dialog.component';

describe('Component Tests', () => {
  describe('ItemOwner Management Delete Component', () => {
    let comp: ItemOwnerDeleteDialogComponent;
    let fixture: ComponentFixture<ItemOwnerDeleteDialogComponent>;
    let service: ItemOwnerService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ItemOwnerDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(ItemOwnerDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ItemOwnerDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ItemOwnerService);
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
