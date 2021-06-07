package com.docswebapps.jh.homeinventory.service;

import com.docswebapps.jh.homeinventory.service.dto.ItemMakeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.docswebapps.jh.homeinventory.domain.ItemMake}.
 */
public interface ItemMakeService {
    /**
     * Save a itemMake.
     *
     * @param itemMakeDTO the entity to save.
     * @return the persisted entity.
     */
    ItemMakeDTO save(ItemMakeDTO itemMakeDTO);

    /**
     * Partially updates a itemMake.
     *
     * @param itemMakeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ItemMakeDTO> partialUpdate(ItemMakeDTO itemMakeDTO);

    /**
     * Get all the itemMakes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ItemMakeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" itemMake.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ItemMakeDTO> findOne(Long id);

    /**
     * Delete the "id" itemMake.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
