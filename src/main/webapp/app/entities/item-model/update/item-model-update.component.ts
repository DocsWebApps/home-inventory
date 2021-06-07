import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IItemModel, ItemModel } from '../item-model.model';
import { ItemModelService } from '../service/item-model.service';
import { IItemMake } from 'app/entities/item-make/item-make.model';
import { ItemMakeService } from 'app/entities/item-make/service/item-make.service';

@Component({
  selector: 'jhi-item-model-update',
  templateUrl: './item-model-update.component.html',
})
export class ItemModelUpdateComponent implements OnInit {
  isSaving = false;

  itemMakesSharedCollection: IItemMake[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    createdDate: [],
    lastModifiedDate: [],
    itemMake: [null, Validators.required],
  });

  constructor(
    protected itemModelService: ItemModelService,
    protected itemMakeService: ItemMakeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ itemModel }) => {
      this.updateForm(itemModel);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const itemModel = this.createFromForm();
    if (itemModel.id !== undefined) {
      this.subscribeToSaveResponse(this.itemModelService.update(itemModel));
    } else {
      this.subscribeToSaveResponse(this.itemModelService.create(itemModel));
    }
  }

  trackItemMakeById(index: number, item: IItemMake): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IItemModel>>): void {
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

  protected updateForm(itemModel: IItemModel): void {
    this.editForm.patchValue({
      id: itemModel.id,
      name: itemModel.name,
      createdDate: itemModel.createdDate,
      lastModifiedDate: itemModel.lastModifiedDate,
      itemMake: itemModel.itemMake,
    });

    this.itemMakesSharedCollection = this.itemMakeService.addItemMakeToCollectionIfMissing(
      this.itemMakesSharedCollection,
      itemModel.itemMake
    );
  }

  protected loadRelationshipsOptions(): void {
    this.itemMakeService
      .query()
      .pipe(map((res: HttpResponse<IItemMake[]>) => res.body ?? []))
      .pipe(
        map((itemMakes: IItemMake[]) =>
          this.itemMakeService.addItemMakeToCollectionIfMissing(itemMakes, this.editForm.get('itemMake')!.value)
        )
      )
      .subscribe((itemMakes: IItemMake[]) => (this.itemMakesSharedCollection = itemMakes));
  }

  protected createFromForm(): IItemModel {
    return {
      ...new ItemModel(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value,
      lastModifiedDate: this.editForm.get(['lastModifiedDate'])!.value,
      itemMake: this.editForm.get(['itemMake'])!.value,
    };
  }
}
