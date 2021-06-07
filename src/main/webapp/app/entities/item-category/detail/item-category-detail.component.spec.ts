import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ItemCategoryDetailComponent } from './item-category-detail.component';

describe('Component Tests', () => {
  describe('ItemCategory Management Detail Component', () => {
    let comp: ItemCategoryDetailComponent;
    let fixture: ComponentFixture<ItemCategoryDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ItemCategoryDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ itemCategory: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ItemCategoryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ItemCategoryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load itemCategory on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.itemCategory).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
