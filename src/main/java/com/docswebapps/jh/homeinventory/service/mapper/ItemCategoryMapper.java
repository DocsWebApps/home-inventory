package com.docswebapps.jh.homeinventory.service.mapper;

import com.docswebapps.jh.homeinventory.domain.ItemCategory;
import com.docswebapps.jh.homeinventory.service.dto.ItemCategoryDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link ItemCategory} and its DTO {@link ItemCategoryDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ItemCategoryMapper extends EntityMapper<ItemCategoryDTO, ItemCategory> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ItemCategoryDTO toDtoId(ItemCategory itemCategory);
}
