package com.docswebapps.jh.homeinventory.service.impl;

import com.docswebapps.jh.homeinventory.domain.ItemImage;
import com.docswebapps.jh.homeinventory.repository.ItemImageRepository;
import com.docswebapps.jh.homeinventory.service.ItemImageService;
import com.docswebapps.jh.homeinventory.service.dto.ItemImageDTO;
import com.docswebapps.jh.homeinventory.service.mapper.ItemImageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ItemImage}.
 */
@Service
@Transactional
public class ItemImageServiceImpl implements ItemImageService {

    private final Logger log = LoggerFactory.getLogger(ItemImageServiceImpl.class);

    private final ItemImageRepository itemImageRepository;

    private final ItemImageMapper itemImageMapper;

    public ItemImageServiceImpl(ItemImageRepository itemImageRepository, ItemImageMapper itemImageMapper) {
        this.itemImageRepository = itemImageRepository;
        this.itemImageMapper = itemImageMapper;
    }

    @Override
    public ItemImageDTO save(ItemImageDTO itemImageDTO) {
        log.debug("Request to save ItemImage : {}", itemImageDTO);
        ItemImage itemImage = itemImageMapper.toEntity(itemImageDTO);
        itemImage = itemImageRepository.save(itemImage);
        return itemImageMapper.toDto(itemImage);
    }

    @Override
    public Optional<ItemImageDTO> partialUpdate(ItemImageDTO itemImageDTO) {
        log.debug("Request to partially update ItemImage : {}", itemImageDTO);

        return itemImageRepository
            .findById(itemImageDTO.getId())
            .map(
                existingItemImage -> {
                    itemImageMapper.partialUpdate(existingItemImage, itemImageDTO);
                    return existingItemImage;
                }
            )
            .map(itemImageRepository::save)
            .map(itemImageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ItemImageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ItemImages");
        return itemImageRepository.findAll(pageable).map(itemImageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ItemImageDTO> findOne(Long id) {
        log.debug("Request to get ItemImage : {}", id);
        return itemImageRepository.findById(id).map(itemImageMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ItemImage : {}", id);
        itemImageRepository.deleteById(id);
    }
}
