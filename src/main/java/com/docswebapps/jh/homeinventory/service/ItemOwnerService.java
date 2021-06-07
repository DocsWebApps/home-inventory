package com.docswebapps.jh.homeinventory.service;

import com.docswebapps.jh.homeinventory.service.dto.ItemOwnerDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.docswebapps.jh.homeinventory.domain.ItemOwner}.
 */
public interface ItemOwnerService {
    /**
     * Save a itemOwner.
     *
     * @param itemOwnerDTO the entity to save.
     * @return the persisted entity.
     */
    ItemOwnerDTO save(ItemOwnerDTO itemOwnerDTO);

    /**
     * Partially updates a itemOwner.
     *
     * @param itemOwnerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ItemOwnerDTO> partialUpdate(ItemOwnerDTO itemOwnerDTO);

    /**
     * Get all the itemOwners.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ItemOwnerDTO> findAll(Pageable pageable);

    /**
     * Get the "id" itemOwner.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ItemOwnerDTO> findOne(Long id);

    /**
     * Delete the "id" itemOwner.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
