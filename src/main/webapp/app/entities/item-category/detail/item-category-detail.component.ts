import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IItemCategory } from '../item-category.model';

@Component({
  selector: 'jhi-item-category-detail',
  templateUrl: './item-category-detail.component.html',
})
export class ItemCategoryDetailComponent implements OnInit {
  itemCategory: IItemCategory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ itemCategory }) => {
      this.itemCategory = itemCategory;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
