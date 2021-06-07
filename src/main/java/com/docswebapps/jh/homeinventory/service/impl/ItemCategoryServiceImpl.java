package com.docswebapps.jh.homeinventory.service.impl;

import com.docswebapps.jh.homeinventory.domain.ItemCategory;
import com.docswebapps.jh.homeinventory.repository.ItemCategoryRepository;
import com.docswebapps.jh.homeinventory.service.ItemCategoryService;
import com.docswebapps.jh.homeinventory.service.dto.ItemCategoryDTO;
import com.docswebapps.jh.homeinventory.service.mapper.ItemCategoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ItemCategory}.
 */
@Service
@Transactional
public class ItemCategoryServiceImpl implements ItemCategoryService {

    private final Logger log = LoggerFactory.getLogger(ItemCategoryServiceImpl.class);

    private final ItemCategoryRepository itemCategoryRepository;

    private final ItemCategoryMapper itemCategoryMapper;

    public ItemCategoryServiceImpl(ItemCategoryRepository itemCategoryRepository, ItemCategoryMapper itemCategoryMapper) {
        this.itemCategoryRepository = itemCategoryRepository;
        this.itemCategoryMapper = itemCategoryMapper;
    }

    @Override
    public ItemCategoryDTO save(ItemCategoryDTO itemCategoryDTO) {
        log.debug("Request to save ItemCategory : {}", itemCategoryDTO);
        ItemCategory itemCategory = itemCategoryMapper.toEntity(itemCategoryDTO);
        itemCategory = itemCategoryRepository.save(itemCategory);
        return itemCategoryMapper.toDto(itemCategory);
    }

    @Override
    public Optional<ItemCategoryDTO> partialUpdate(ItemCategoryDTO itemCategoryDTO) {
        log.debug("Request to partially update ItemCategory : {}", itemCategoryDTO);

        return itemCategoryRepository
            .findById(itemCategoryDTO.getId())
            .map(
                existingItemCategory -> {
                    itemCategoryMapper.partialUpdate(existingItemCategory, itemCategoryDTO);
                    return existingItemCategory;
                }
            )
            .map(itemCategoryRepository::save)
            .map(itemCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ItemCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ItemCategories");
        return itemCategoryRepository.findAll(pageable).map(itemCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ItemCategoryDTO> findOne(Long id) {
        log.debug("Request to get ItemCategory : {}", id);
        return itemCategoryRepository.findById(id).map(itemCategoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ItemCategory : {}", id);
        itemCategoryRepository.deleteById(id);
    }
}
