package com.docswebapps.jh.homeinventory.service.impl;

import com.docswebapps.jh.homeinventory.domain.ItemOwner;
import com.docswebapps.jh.homeinventory.repository.ItemOwnerRepository;
import com.docswebapps.jh.homeinventory.service.ItemOwnerService;
import com.docswebapps.jh.homeinventory.service.dto.ItemOwnerDTO;
import com.docswebapps.jh.homeinventory.service.mapper.ItemOwnerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ItemOwner}.
 */
@Service
@Transactional
public class ItemOwnerServiceImpl implements ItemOwnerService {

    private final Logger log = LoggerFactory.getLogger(ItemOwnerServiceImpl.class);

    private final ItemOwnerRepository itemOwnerRepository;

    private final ItemOwnerMapper itemOwnerMapper;

    public ItemOwnerServiceImpl(ItemOwnerRepository itemOwnerRepository, ItemOwnerMapper itemOwnerMapper) {
        this.itemOwnerRepository = itemOwnerRepository;
        this.itemOwnerMapper = itemOwnerMapper;
    }

    @Override
    public ItemOwnerDTO save(ItemOwnerDTO itemOwnerDTO) {
        log.debug("Request to save ItemOwner : {}", itemOwnerDTO);
        ItemOwner itemOwner = itemOwnerMapper.toEntity(itemOwnerDTO);
        itemOwner = itemOwnerRepository.save(itemOwner);
        return itemOwnerMapper.toDto(itemOwner);
    }

    @Override
    public Optional<ItemOwnerDTO> partialUpdate(ItemOwnerDTO itemOwnerDTO) {
        log.debug("Request to partially update ItemOwner : {}", itemOwnerDTO);

        return itemOwnerRepository
            .findById(itemOwnerDTO.getId())
            .map(
                existingItemOwner -> {
                    itemOwnerMapper.partialUpdate(existingItemOwner, itemOwnerDTO);
                    return existingItemOwner;
                }
            )
            .map(itemOwnerRepository::save)
            .map(itemOwnerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ItemOwnerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ItemOwners");
        return itemOwnerRepository.findAll(pageable).map(itemOwnerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ItemOwnerDTO> findOne(Long id) {
        log.debug("Request to get ItemOwner : {}", id);
        return itemOwnerRepository.findById(id).map(itemOwnerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ItemOwner : {}", id);
        itemOwnerRepository.deleteById(id);
    }
}
