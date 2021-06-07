import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IItemModel } from '../item-model.model';

@Component({
  selector: 'jhi-item-model-detail',
  templateUrl: './item-model-detail.component.html',
})
export class ItemModelDetailComponent implements OnInit {
  itemModel: IItemModel | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ itemModel }) => {
      this.itemModel = itemModel;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
