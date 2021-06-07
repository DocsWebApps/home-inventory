package com.docswebapps.jh.homeinventory.service.impl;

import com.docswebapps.jh.homeinventory.domain.ItemModel;
import com.docswebapps.jh.homeinventory.repository.ItemModelRepository;
import com.docswebapps.jh.homeinventory.service.ItemModelService;
import com.docswebapps.jh.homeinventory.service.dto.ItemModelDTO;
import com.docswebapps.jh.homeinventory.service.mapper.ItemModelMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ItemModel}.
 */
@Service
@Transactional
public class ItemModelServiceImpl implements ItemModelService {

    private final Logger log = LoggerFactory.getLogger(ItemModelServiceImpl.class);

    private final ItemModelRepository itemModelRepository;

    private final ItemModelMapper itemModelMapper;

    public ItemModelServiceImpl(ItemModelRepository itemModelRepository, ItemModelMapper itemModelMapper) {
        this.itemModelRepository = itemModelRepository;
        this.itemModelMapper = itemModelMapper;
    }

    @Override
    public ItemModelDTO save(ItemModelDTO itemModelDTO) {
        log.debug("Request to save ItemModel : {}", itemModelDTO);
        ItemModel itemModel = itemModelMapper.toEntity(itemModelDTO);
        itemModel = itemModelRepository.save(itemModel);
        return itemModelMapper.toDto(itemModel);
    }

    @Override
    public Optional<ItemModelDTO> partialUpdate(ItemModelDTO itemModelDTO) {
        log.debug("Request to partially update ItemModel : {}", itemModelDTO);

        return itemModelRepository
            .findById(itemModelDTO.getId())
            .map(
                existingItemModel -> {
                    itemModelMapper.partialUpdate(existingItemModel, itemModelDTO);
                    return existingItemModel;
                }
            )
            .map(itemModelRepository::save)
            .map(itemModelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ItemModelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ItemModels");
        return itemModelRepository.findAll(pageable).map(itemModelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ItemModelDTO> findOne(Long id) {
        log.debug("Request to get ItemModel : {}", id);
        return itemModelRepository.findById(id).map(itemModelMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ItemModel : {}", id);
        itemModelRepository.deleteById(id);
    }
}
