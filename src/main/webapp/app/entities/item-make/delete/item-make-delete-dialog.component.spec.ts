jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, fakeAsync, inject, TestBed, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ItemMakeService } from '../service/item-make.service';

import { ItemMakeDeleteDialogComponent } from './item-make-delete-dialog.component';

describe('Component Tests', () => {
  describe('ItemMake Management Delete Component', () => {
    let comp: ItemMakeDeleteDialogComponent;
    let fixture: ComponentFixture<ItemMakeDeleteDialogComponent>;
    let service: ItemMakeService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ItemMakeDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(ItemMakeDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ItemMakeDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ItemMakeService);
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
