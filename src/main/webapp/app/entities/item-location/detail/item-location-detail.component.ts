import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IItemLocation } from '../item-location.model';

@Component({
  selector: 'jhi-item-location-detail',
  templateUrl: './item-location-detail.component.html',
})
export class ItemLocationDetailComponent implements OnInit {
  itemLocation: IItemLocation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ itemLocation }) => {
      this.itemLocation = itemLocation;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
