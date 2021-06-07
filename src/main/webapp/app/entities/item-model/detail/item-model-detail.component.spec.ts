import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ItemModelDetailComponent } from './item-model-detail.component';

describe('Component Tests', () => {
  describe('ItemModel Management Detail Component', () => {
    let comp: ItemModelDetailComponent;
    let fixture: ComponentFixture<ItemModelDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ItemModelDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ itemModel: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ItemModelDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ItemModelDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load itemModel on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.itemModel).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
