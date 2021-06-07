package com.docswebapps.jh.homeinventory.service.mapper;

import com.docswebapps.jh.homeinventory.domain.ItemMake;
import com.docswebapps.jh.homeinventory.service.dto.ItemMakeDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link ItemMake} and its DTO {@link ItemMakeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ItemMakeMapper extends EntityMapper<ItemMakeDTO, ItemMake> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ItemMakeDTO toDtoId(ItemMake itemMake);
}
