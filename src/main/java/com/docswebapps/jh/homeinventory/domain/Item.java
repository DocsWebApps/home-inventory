package com.docswebapps.jh.homeinventory.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Item.
 */
@Entity
@Table(name = "item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "cost")
    private Double cost;

    @NotNull
    @Column(name = "is_cost_estimate", nullable = false)
    private Boolean isCostEstimate;

    @Column(name = "serial_number")
    private String serialNumber;

    @NotNull
    @Column(name = "purchase_date", nullable = false)
    private LocalDate purchaseDate;

    @NotNull
    @Column(name = "is_purchase_date_estimate", nullable = false)
    private Boolean isPurchaseDateEstimate;

    @NotNull
    @Column(name = "have_receipt", nullable = false)
    private Boolean haveReceipt;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "additional_info")
    private String additionalInfo;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "last_modified_date")
    private LocalDate lastModifiedDate;

    @OneToMany(mappedBy = "item")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "item" }, allowSetters = true)
    private Set<ItemImage> itemImages = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "items" }, allowSetters = true)
    private ItemCategory itemCategory;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "items" }, allowSetters = true)
    private ItemOwner itemOwner;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "items" }, allowSetters = true)
    private ItemLocation itemLocation;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "items", "itemMake" }, allowSetters = true)
    private ItemModel itemModel;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Item id(Long id) {
        this.id = id;
        return this;
    }

    public Double getCost() {
        return this.cost;
    }

    public Item cost(Double cost) {
        this.cost = cost;
        return this;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Boolean getIsCostEstimate() {
        return this.isCostEstimate;
    }

    public Item isCostEstimate(Boolean isCostEstimate) {
        this.isCostEstimate = isCostEstimate;
        return this;
    }

    public void setIsCostEstimate(Boolean isCostEstimate) {
        this.isCostEstimate = isCostEstimate;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public Item serialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
        return this;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public LocalDate getPurchaseDate() {
        return this.purchaseDate;
    }

    public Item purchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
        return this;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Boolean getIsPurchaseDateEstimate() {
        return this.isPurchaseDateEstimate;
    }

    public Item isPurchaseDateEstimate(Boolean isPurchaseDateEstimate) {
        this.isPurchaseDateEstimate = isPurchaseDateEstimate;
        return this;
    }

    public void setIsPurchaseDateEstimate(Boolean isPurchaseDateEstimate) {
        this.isPurchaseDateEstimate = isPurchaseDateEstimate;
    }

    public Boolean getHaveReceipt() {
        return this.haveReceipt;
    }

    public Item haveReceipt(Boolean haveReceipt) {
        this.haveReceipt = haveReceipt;
        return this;
    }

    public void setHaveReceipt(Boolean haveReceipt) {
        this.haveReceipt = haveReceipt;
    }

    public String getAdditionalInfo() {
        return this.additionalInfo;
    }

    public Item additionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
        return this;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public LocalDate getCreatedDate() {
        return this.createdDate;
    }

    public Item createdDate(LocalDate createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Item lastModifiedDate(LocalDate lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public void setLastModifiedDate(LocalDate lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<ItemImage> getItemImages() {
        return this.itemImages;
    }

    public Item itemImages(Set<ItemImage> itemImages) {
        this.setItemImages(itemImages);
        return this;
    }

    public Item addItemImage(ItemImage itemImage) {
        this.itemImages.add(itemImage);
        itemImage.setItem(this);
        return this;
    }

    public Item removeItemImage(ItemImage itemImage) {
        this.itemImages.remove(itemImage);
        itemImage.setItem(null);
        return this;
    }

    public void setItemImages(Set<ItemImage> itemImages) {
        if (this.itemImages != null) {
            this.itemImages.forEach(i -> i.setItem(null));
        }
        if (itemImages != null) {
            itemImages.forEach(i -> i.setItem(this));
        }
        this.itemImages = itemImages;
    }

    public ItemCategory getItemCategory() {
        return this.itemCategory;
    }

    public Item itemCategory(ItemCategory itemCategory) {
        this.setItemCategory(itemCategory);
        return this;
    }

    public void setItemCategory(ItemCategory itemCategory) {
        this.itemCategory = itemCategory;
    }

    public ItemOwner getItemOwner() {
        return this.itemOwner;
    }

    public Item itemOwner(ItemOwner itemOwner) {
        this.setItemOwner(itemOwner);
        return this;
    }

    public void setItemOwner(ItemOwner itemOwner) {
        this.itemOwner = itemOwner;
    }

    public ItemLocation getItemLocation() {
        return this.itemLocation;
    }

    public Item itemLocation(ItemLocation itemLocation) {
        this.setItemLocation(itemLocation);
        return this;
    }

    public void setItemLocation(ItemLocation itemLocation) {
        this.itemLocation = itemLocation;
    }

    public ItemModel getItemModel() {
        return this.itemModel;
    }

    public Item itemModel(ItemModel itemModel) {
        this.setItemModel(itemModel);
        return this;
    }

    public void setItemModel(ItemModel itemModel) {
        this.itemModel = itemModel;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Item)) {
            return false;
        }
        return id != null && id.equals(((Item) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Item{" +
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
            "}";
    }
}
