package com.docswebapps.jh.homeinventory.service;

import com.docswebapps.jh.homeinventory.service.dto.ItemLocationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.docswebapps.jh.homeinventory.domain.ItemLocation}.
 */
public interface ItemLocationService {
    /**
     * Save a itemLocation.
     *
     * @param itemLocationDTO the entity to save.
     * @return the persisted entity.
     */
    ItemLocationDTO save(ItemLocationDTO itemLocationDTO);

    /**
     * Partially updates a itemLocation.
     *
     * @param itemLocationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ItemLocationDTO> partialUpdate(ItemLocationDTO itemLocationDTO);

    /**
     * Get all the itemLocations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ItemLocationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" itemLocation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ItemLocationDTO> findOne(Long id);

    /**
     * Delete the "id" itemLocation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
