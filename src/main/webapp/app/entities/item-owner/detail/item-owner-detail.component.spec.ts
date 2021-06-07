import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ItemOwnerDetailComponent } from './item-owner-detail.component';

describe('Component Tests', () => {
  describe('ItemOwner Management Detail Component', () => {
    let comp: ItemOwnerDetailComponent;
    let fixture: ComponentFixture<ItemOwnerDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ItemOwnerDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ itemOwner: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ItemOwnerDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ItemOwnerDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load itemOwner on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.itemOwner).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
