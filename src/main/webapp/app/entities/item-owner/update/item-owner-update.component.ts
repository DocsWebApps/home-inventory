import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IItemOwner, ItemOwner } from '../item-owner.model';
import { ItemOwnerService } from '../service/item-owner.service';

@Component({
  selector: 'jhi-item-owner-update',
  templateUrl: './item-owner-update.component.html',
})
export class ItemOwnerUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    createdDate: [],
    lastModifiedDate: [],
  });

  constructor(protected itemOwnerService: ItemOwnerService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ itemOwner }) => {
      this.updateForm(itemOwner);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const itemOwner = this.createFromForm();
    if (itemOwner.id !== undefined) {
      this.subscribeToSaveResponse(this.itemOwnerService.update(itemOwner));
    } else {
      this.subscribeToSaveResponse(this.itemOwnerService.create(itemOwner));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IItemOwner>>): void {
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

  protected updateForm(itemOwner: IItemOwner): void {
    this.editForm.patchValue({
      id: itemOwner.id,
      name: itemOwner.name,
      createdDate: itemOwner.createdDate,
      lastModifiedDate: itemOwner.lastModifiedDate,
    });
  }

  protected createFromForm(): IItemOwner {
    return {
      ...new ItemOwner(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value,
      lastModifiedDate: this.editForm.get(['lastModifiedDate'])!.value,
    };
  }
}
