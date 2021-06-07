import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IItem, Item } from '../item.model';
import { ItemService } from '../service/item.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IItemCategory } from 'app/entities/item-category/item-category.model';
import { ItemCategoryService } from 'app/entities/item-category/service/item-category.service';
import { IItemOwner } from 'app/entities/item-owner/item-owner.model';
import { ItemOwnerService } from 'app/entities/item-owner/service/item-owner.service';
import { IItemLocation } from 'app/entities/item-location/item-location.model';
import { ItemLocationService } from 'app/entities/item-location/service/item-location.service';
import { IItemModel } from 'app/entities/item-model/item-model.model';
import { ItemModelService } from 'app/entities/item-model/service/item-model.service';

@Component({
  selector: 'jhi-item-update',
  templateUrl: './item-update.component.html',
})
export class ItemUpdateComponent implements OnInit {
  isSaving = false;

  itemCategoriesSharedCollection: IItemCategory[] = [];
  itemOwnersSharedCollection: IItemOwner[] = [];
  itemLocationsSharedCollection: IItemLocation[] = [];
  itemModelsSharedCollection: IItemModel[] = [];

  editForm = this.fb.group({
    id: [],
    cost: [],
    isCostEstimate: [null, [Validators.required]],
    serialNumber: [],
    purchaseDate: [null, [Validators.required]],
    isPurchaseDateEstimate: [null, [Validators.required]],
    haveReceipt: [null, [Validators.required]],
    additionalInfo: [],
    createdDate: [],
    lastModifiedDate: [],
    itemCategory: [null, Validators.required],
    itemOwner: [null, Validators.required],
    itemLocation: [null, Validators.required],
    itemModel: [null, Validators.required],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected itemService: ItemService,
    protected itemCategoryService: ItemCategoryService,
    protected itemOwnerService: ItemOwnerService,
    protected itemLocationService: ItemLocationService,
    protected itemModelService: ItemModelService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ item }) => {
      this.updateForm(item);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('homeInventoryApp.error', { message: err.message })
        ),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const item = this.createFromForm();
    if (item.id !== undefined) {
      this.subscribeToSaveResponse(this.itemService.update(item));
    } else {
      this.subscribeToSaveResponse(this.itemService.create(item));
    }
  }

  trackItemCategoryById(index: number, item: IItemCategory): number {
    return item.id!;
  }

  trackItemOwnerById(index: number, item: IItemOwner): number {
    return item.id!;
  }

  trackItemLocationById(index: number, item: IItemLocation): number {
    return item.id!;
  }

  trackItemModelById(index: number, item: IItemModel): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IItem>>): void {
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

  protected updateForm(item: IItem): void {
    this.editForm.patchValue({
      id: item.id,
      cost: item.cost,
      isCostEstimate: item.isCostEstimate,
      serialNumber: item.serialNumber,
      purchaseDate: item.purchaseDate,
      isPurchaseDateEstimate: item.isPurchaseDateEstimate,
      haveReceipt: item.haveReceipt,
      additionalInfo: item.additionalInfo,
      createdDate: item.createdDate,
      lastModifiedDate: item.lastModifiedDate,
      itemCategory: item.itemCategory,
      itemOwner: item.itemOwner,
      itemLocation: item.itemLocation,
      itemModel: item.itemModel,
    });

    this.itemCategoriesSharedCollection = this.itemCategoryService.addItemCategoryToCollectionIfMissing(
      this.itemCategoriesSharedCollection,
      item.itemCategory
    );
    this.itemOwnersSharedCollection = this.itemOwnerService.addItemOwnerToCollectionIfMissing(
      this.itemOwnersSharedCollection,
      item.itemOwner
    );
    this.itemLocationsSharedCollection = this.itemLocationService.addItemLocationToCollectionIfMissing(
      this.itemLocationsSharedCollection,
      item.itemLocation
    );
    this.itemModelsSharedCollection = this.itemModelService.addItemModelToCollectionIfMissing(
      this.itemModelsSharedCollection,
      item.itemModel
    );
  }

  protected loadRelationshipsOptions(): void {
    this.itemCategoryService
      .query()
      .pipe(map((res: HttpResponse<IItemCategory[]>) => res.body ?? []))
      .pipe(
        map((itemCategories: IItemCategory[]) =>
          this.itemCategoryService.addItemCategoryToCollectionIfMissing(itemCategories, this.editForm.get('itemCategory')!.value)
        )
      )
      .subscribe((itemCategories: IItemCategory[]) => (this.itemCategoriesSharedCollection = itemCategories));

    this.itemOwnerService
      .query()
      .pipe(map((res: HttpResponse<IItemOwner[]>) => res.body ?? []))
      .pipe(
        map((itemOwners: IItemOwner[]) =>
          this.itemOwnerService.addItemOwnerToCollectionIfMissing(itemOwners, this.editForm.get('itemOwner')!.value)
        )
      )
      .subscribe((itemOwners: IItemOwner[]) => (this.itemOwnersSharedCollection = itemOwners));

    this.itemLocationService
      .query()
      .pipe(map((res: HttpResponse<IItemLocation[]>) => res.body ?? []))
      .pipe(
        map((itemLocations: IItemLocation[]) =>
          this.itemLocationService.addItemLocationToCollectionIfMissing(itemLocations, this.editForm.get('itemLocation')!.value)
        )
      )
      .subscribe((itemLocations: IItemLocation[]) => (this.itemLocationsSharedCollection = itemLocations));

    this.itemModelService
      .query()
      .pipe(map((res: HttpResponse<IItemModel[]>) => res.body ?? []))
      .pipe(
        map((itemModels: IItemModel[]) =>
          this.itemModelService.addItemModelToCollectionIfMissing(itemModels, this.editForm.get('itemModel')!.value)
        )
      )
      .subscribe((itemModels: IItemModel[]) => (this.itemModelsSharedCollection = itemModels));
  }

  protected createFromForm(): IItem {
    return {
      ...new Item(),
      id: this.editForm.get(['id'])!.value,
      cost: this.editForm.get(['cost'])!.value,
      isCostEstimate: this.editForm.get(['isCostEstimate'])!.value,
      serialNumber: this.editForm.get(['serialNumber'])!.value,
      purchaseDate: this.editForm.get(['purchaseDate'])!.value,
      isPurchaseDateEstimate: this.editForm.get(['isPurchaseDateEstimate'])!.value,
      haveReceipt: this.editForm.get(['haveReceipt'])!.value,
      additionalInfo: this.editForm.get(['additionalInfo'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value,
      lastModifiedDate: this.editForm.get(['lastModifiedDate'])!.value,
      itemCategory: this.editForm.get(['itemCategory'])!.value,
      itemOwner: this.editForm.get(['itemOwner'])!.value,
      itemLocation: this.editForm.get(['itemLocation'])!.value,
      itemModel: this.editForm.get(['itemModel'])!.value,
    };
  }
}
