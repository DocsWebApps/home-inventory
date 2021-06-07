import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ItemMakeDetailComponent } from './item-make-detail.component';

describe('Component Tests', () => {
  describe('ItemMake Management Detail Component', () => {
    let comp: ItemMakeDetailComponent;
    let fixture: ComponentFixture<ItemMakeDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ItemMakeDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ itemMake: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ItemMakeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ItemMakeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load itemMake on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.itemMake).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
