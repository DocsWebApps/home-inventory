import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IItemCategory, ItemCategory } from '../item-category.model';
import { ItemCategoryService } from '../service/item-category.service';

@Component({
  selector: 'jhi-item-category-update',
  templateUrl: './item-category-update.component.html',
})
export class ItemCategoryUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    createdDate: [],
    lastModifiedDate: [],
  });

  constructor(protected itemCategoryService: ItemCategoryService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ itemCategory }) => {
      this.updateForm(itemCategory);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const itemCategory = this.createFromForm();
    if (itemCategory.id !== undefined) {
      this.subscribeToSaveResponse(this.itemCategoryService.update(itemCategory));
    } else {
      this.subscribeToSaveResponse(this.itemCategoryService.create(itemCategory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IItemCategory>>): void {
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

  protected updateForm(itemCategory: IItemCategory): void {
    this.editForm.patchValue({
      id: itemCategory.id,
      name: itemCategory.name,
      createdDate: itemCategory.createdDate,
      lastModifiedDate: itemCategory.lastModifiedDate,
    });
  }

  protected createFromForm(): IItemCategory {
    return {
      ...new ItemCategory(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value,
      lastModifiedDate: this.editForm.get(['lastModifiedDate'])!.value,
    };
  }
}
