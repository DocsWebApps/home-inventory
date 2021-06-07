import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ItemLocationDetailComponent } from './item-location-detail.component';

describe('Component Tests', () => {
  describe('ItemLocation Management Detail Component', () => {
    let comp: ItemLocationDetailComponent;
    let fixture: ComponentFixture<ItemLocationDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ItemLocationDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ itemLocation: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ItemLocationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ItemLocationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load itemLocation on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.itemLocation).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
