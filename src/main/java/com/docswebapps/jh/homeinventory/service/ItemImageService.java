package com.docswebapps.jh.homeinventory.service;

import com.docswebapps.jh.homeinventory.service.dto.ItemImageDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.docswebapps.jh.homeinventory.domain.ItemImage}.
 */
public interface ItemImageService {
    /**
     * Save a itemImage.
     *
     * @param itemImageDTO the entity to save.
     * @return the persisted entity.
     */
    ItemImageDTO save(ItemImageDTO itemImageDTO);

    /**
     * Partially updates a itemImage.
     *
     * @param itemImageDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ItemImageDTO> partialUpdate(ItemImageDTO itemImageDTO);

    /**
     * Get all the itemImages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ItemImageDTO> findAll(Pageable pageable);

    /**
     * Get the "id" itemImage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ItemImageDTO> findOne(Long id);

    /**
     * Delete the "id" itemImage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
