import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IItemOwner } from '../item-owner.model';

@Component({
  selector: 'jhi-item-owner-detail',
  templateUrl: './item-owner-detail.component.html',
})
export class ItemOwnerDetailComponent implements OnInit {
  itemOwner: IItemOwner | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ itemOwner }) => {
      this.itemOwner = itemOwner;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
