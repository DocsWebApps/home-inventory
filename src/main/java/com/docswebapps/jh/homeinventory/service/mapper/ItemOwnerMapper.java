package com.docswebapps.jh.homeinventory.service.mapper;

import com.docswebapps.jh.homeinventory.domain.ItemOwner;
import com.docswebapps.jh.homeinventory.service.dto.ItemOwnerDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link ItemOwner} and its DTO {@link ItemOwnerDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ItemOwnerMapper extends EntityMapper<ItemOwnerDTO, ItemOwner> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ItemOwnerDTO toDtoId(ItemOwner itemOwner);
}
