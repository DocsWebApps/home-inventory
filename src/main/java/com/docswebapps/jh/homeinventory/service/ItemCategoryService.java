package com.docswebapps.jh.homeinventory.service;

import com.docswebapps.jh.homeinventory.service.dto.ItemCategoryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.docswebapps.jh.homeinventory.domain.ItemCategory}.
 */
public interface ItemCategoryService {
    /**
     * Save a itemCategory.
     *
     * @param itemCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    ItemCategoryDTO save(ItemCategoryDTO itemCategoryDTO);

    /**
     * Partially updates a itemCategory.
     *
     * @param itemCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ItemCategoryDTO> partialUpdate(ItemCategoryDTO itemCategoryDTO);

    /**
     * Get all the itemCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ItemCategoryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" itemCategory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ItemCategoryDTO> findOne(Long id);

    /**
     * Delete the "id" itemCategory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
