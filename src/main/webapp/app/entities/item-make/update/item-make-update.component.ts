import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IItemMake, ItemMake } from '../item-make.model';
import { ItemMakeService } from '../service/item-make.service';

@Component({
  selector: 'jhi-item-make-update',
  templateUrl: './item-make-update.component.html',
})
export class ItemMakeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    createdDate: [],
    lastModifiedDate: [],
  });

  constructor(protected itemMakeService: ItemMakeService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ itemMake }) => {
      this.updateForm(itemMake);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const itemMake = this.createFromForm();
    if (itemMake.id !== undefined) {
      this.subscribeToSaveResponse(this.itemMakeService.update(itemMake));
    } else {
      this.subscribeToSaveResponse(this.itemMakeService.create(itemMake));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IItemMake>>): void {
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

  protected updateForm(itemMake: IItemMake): void {
    this.editForm.patchValue({
      id: itemMake.id,
      name: itemMake.name,
      createdDate: itemMake.createdDate,
      lastModifiedDate: itemMake.lastModifiedDate,
    });
  }

  protected createFromForm(): IItemMake {
    return {
      ...new ItemMake(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value,
      lastModifiedDate: this.editForm.get(['lastModifiedDate'])!.value,
    };
  }
}
