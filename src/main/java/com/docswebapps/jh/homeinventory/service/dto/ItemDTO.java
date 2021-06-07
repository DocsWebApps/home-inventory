package com.docswebapps.jh.homeinventory.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link com.docswebapps.jh.homeinventory.domain.Item} entity.
 */
public class ItemDTO implements Serializable {

    private Long id;

    private Double cost;

    @NotNull
    private Boolean isCostEstimate;

    private String serialNumber;

    @NotNull
    private LocalDate purchaseDate;

    @NotNull
    private Boolean isPurchaseDateEstimate;

    @NotNull
    private Boolean haveReceipt;

    @Lob
    private String additionalInfo;

    private LocalDate createdDate;

    private LocalDate lastModifiedDate;

    private ItemCategoryDTO itemCategory;

    private ItemOwnerDTO itemOwner;

    private ItemLocationDTO itemLocation;

    private ItemModelDTO itemModel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Boolean getIsCostEstimate() {
        return isCostEstimate;
    }

    public void setIsCostEstimate(Boolean isCostEstimate) {
        this.isCostEstimate = isCostEstimate;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Boolean getIsPurchaseDateEstimate() {
        return isPurchaseDateEstimate;
    }

    public void setIsPurchaseDateEstimate(Boolean isPurchaseDateEstimate) {
        this.isPurchaseDateEstimate = isPurchaseDateEstimate;
    }

    public Boolean getHaveReceipt() {
        return haveReceipt;
    }

    public void setHaveReceipt(Boolean haveReceipt) {
        this.haveReceipt = haveReceipt;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDate lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public ItemCategoryDTO getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(ItemCategoryDTO itemCategory) {
        this.itemCategory = itemCategory;
    }

    public ItemOwnerDTO getItemOwner() {
        return itemOwner;
    }

    public void setItemOwner(ItemOwnerDTO itemOwner) {
        this.itemOwner = itemOwner;
    }

    public ItemLocationDTO getItemLocation() {
        return itemLocation;
    }

    public void setItemLocation(ItemLocationDTO itemLocation) {
        this.itemLocation = itemLocation;
    }

    public ItemModelDTO getItemModel() {
        return itemModel;
    }

    public void setItemModel(ItemModelDTO itemModel) {
        this.itemModel = itemModel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemDTO)) {
            return false;
        }

        ItemDTO itemDTO = (ItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, itemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemDTO{" +
            "id=" + getId() +
            ", cost=" + getCost() +
            ", isCostEstimate='" + getIsCostEstimate() + "'" +
            ", serialNumber='" + getSerialNumber() + "'" +
            ", purchaseDate='" + getPurchaseDate() + "'" +
            ", isPurchaseDateEstimate='" + getIsPurchaseDateEstimate() + "'" +
            ", haveReceipt='" + getHaveReceipt() + "'" +
            ", additionalInfo='" + getAdditionalInfo() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", itemCategory=" + getItemCategory() +
            ", itemOwner=" + getItemOwner() +
            ", itemLocation=" + getItemLocation() +
            ", itemModel=" + getItemModel() +
            "}";
    }
}
