package com.docswebapps.jh.homeinventory.service.mapper;

import com.docswebapps.jh.homeinventory.domain.ItemLocation;
import com.docswebapps.jh.homeinventory.service.dto.ItemLocationDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link ItemLocation} and its DTO {@link ItemLocationDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ItemLocationMapper extends EntityMapper<ItemLocationDTO, ItemLocation> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ItemLocationDTO toDtoId(ItemLocation itemLocation);
}
