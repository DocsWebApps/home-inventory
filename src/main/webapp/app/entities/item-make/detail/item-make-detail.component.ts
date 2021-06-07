import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IItemMake } from '../item-make.model';

@Component({
  selector: 'jhi-item-make-detail',
  templateUrl: './item-make-detail.component.html',
})
export class ItemMakeDetailComponent implements OnInit {
  itemMake: IItemMake | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ itemMake }) => {
      this.itemMake = itemMake;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
