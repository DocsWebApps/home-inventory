package com.docswebapps.jh.homeinventory.service.mapper;

import com.docswebapps.jh.homeinventory.domain.ItemImage;
import com.docswebapps.jh.homeinventory.service.dto.ItemImageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link ItemImage} and its DTO {@link ItemImageDTO}.
 */
@Mapper(componentModel = "spring", uses = { ItemMapper.class })
public interface ItemImageMapper extends EntityMapper<ItemImageDTO, ItemImage> {
    @Mapping(target = "item", source = "item", qualifiedByName = "id")
    ItemImageDTO toDto(ItemImage s);
}
