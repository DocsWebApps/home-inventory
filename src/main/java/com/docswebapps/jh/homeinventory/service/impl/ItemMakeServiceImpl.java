package com.docswebapps.jh.homeinventory.service.impl;

import com.docswebapps.jh.homeinventory.domain.ItemMake;
import com.docswebapps.jh.homeinventory.repository.ItemMakeRepository;
import com.docswebapps.jh.homeinventory.service.ItemMakeService;
import com.docswebapps.jh.homeinventory.service.dto.ItemMakeDTO;
import com.docswebapps.jh.homeinventory.service.mapper.ItemMakeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ItemMake}.
 */
@Service
@Transactional
public class ItemMakeServiceImpl implements ItemMakeService {

    private final Logger log = LoggerFactory.getLogger(ItemMakeServiceImpl.class);

    private final ItemMakeRepository itemMakeRepository;

    private final ItemMakeMapper itemMakeMapper;

    public ItemMakeServiceImpl(ItemMakeRepository itemMakeRepository, ItemMakeMapper itemMakeMapper) {
        this.itemMakeRepository = itemMakeRepository;
        this.itemMakeMapper = itemMakeMapper;
    }

    @Override
    public ItemMakeDTO save(ItemMakeDTO itemMakeDTO) {
        log.debug("Request to save ItemMake : {}", itemMakeDTO);
        ItemMake itemMake = itemMakeMapper.toEntity(itemMakeDTO);
        itemMake = itemMakeRepository.save(itemMake);
        return itemMakeMapper.toDto(itemMake);
    }

    @Override
    public Optional<ItemMakeDTO> partialUpdate(ItemMakeDTO itemMakeDTO) {
        log.debug("Request to partially update ItemMake : {}", itemMakeDTO);

        return itemMakeRepository
            .findById(itemMakeDTO.getId())
            .map(
                existingItemMake -> {
                    itemMakeMapper.partialUpdate(existingItemMake, itemMakeDTO);
                    return existingItemMake;
                }
            )
            .map(itemMakeRepository::save)
            .map(itemMakeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ItemMakeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ItemMakes");
        return itemMakeRepository.findAll(pageable).map(itemMakeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ItemMakeDTO> findOne(Long id) {
        log.debug("Request to get ItemMake : {}", id);
        return itemMakeRepository.findById(id).map(itemMakeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ItemMake : {}", id);
        itemMakeRepository.deleteById(id);
    }
}
