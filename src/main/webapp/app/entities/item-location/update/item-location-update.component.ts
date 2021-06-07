import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IItemLocation, ItemLocation } from '../item-location.model';
import { ItemLocationService } from '../service/item-location.service';

@Component({
  selector: 'jhi-item-location-update',
  templateUrl: './item-location-update.component.html',
})
export class ItemLocationUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    createdDate: [],
    lastModifiedDate: [],
  });

  constructor(protected itemLocationService: ItemLocationService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ itemLocation }) => {
      this.updateForm(itemLocation);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const itemLocation = this.createFromForm();
    if (itemLocation.id !== undefined) {
      this.subscribeToSaveResponse(this.itemLocationService.update(itemLocation));
    } else {
      this.subscribeToSaveResponse(this.itemLocationService.create(itemLocation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IItemLocation>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(itemLocation: IItemLocation): void {
    this.editForm.patchValue({
      id: itemLocation.id,
      name: itemLocation.name,
      createdDate: itemLocation.createdDate,
      lastModifiedDate: itemLocation.lastModifiedDate,
    });
  }

  protected createFromForm(): IItemLocation {
    return {
      ...new ItemLocation(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value,
      lastModifiedDate: this.editForm.get(['lastModifiedDate'])!.value,
    };
  }
}
