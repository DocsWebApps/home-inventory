package com.docswebapps.jh.homeinventory.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link com.docswebapps.jh.homeinventory.domain.ItemCategory} entity.
 */
public class ItemCategoryDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private LocalDate createdDate;

    private LocalDate lastModifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemCategoryDTO)) {
            return false;
        }

        ItemCategoryDTO itemCategoryDTO = (ItemCategoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, itemCategoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemCategoryDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
