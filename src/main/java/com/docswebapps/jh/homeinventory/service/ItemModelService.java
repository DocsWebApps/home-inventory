package com.docswebapps.jh.homeinventory.service;

import com.docswebapps.jh.homeinventory.service.dto.ItemModelDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.docswebapps.jh.homeinventory.domain.ItemModel}.
 */
public interface ItemModelService {
    /**
     * Save a itemModel.
     *
     * @param itemModelDTO the entity to save.
     * @return the persisted entity.
     */
    ItemModelDTO save(ItemModelDTO itemModelDTO);

    /**
     * Partially updates a itemModel.
     *
     * @param itemModelDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ItemModelDTO> partialUpdate(ItemModelDTO itemModelDTO);

    /**
     * Get all the itemModels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ItemModelDTO> findAll(Pageable pageable);

    /**
     * Get the "id" itemModel.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ItemModelDTO> findOne(Long id);

    /**
     * Delete the "id" itemModel.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
