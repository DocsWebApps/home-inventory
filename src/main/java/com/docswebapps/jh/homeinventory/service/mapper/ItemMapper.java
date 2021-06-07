package com.docswebapps.jh.homeinventory.service.mapper;

import com.docswebapps.jh.homeinventory.domain.Item;
import com.docswebapps.jh.homeinventory.service.dto.ItemDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Item} and its DTO {@link ItemDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { ItemCategoryMapper.class, ItemOwnerMapper.class, ItemLocationMapper.class, ItemModelMapper.class }
)
public interface ItemMapper extends EntityMapper<ItemDTO, Item> {
    @Mapping(target = "itemCategory", source = "itemCategory", qualifiedByName = "id")
    @Mapping(target = "itemOwner", source = "itemOwner", qualifiedByName = "id")
    @Mapping(target = "itemLocation", source = "itemLocation", qualifiedByName = "id")
    @Mapping(target = "itemModel", source = "itemModel", qualifiedByName = "id")
    ItemDTO toDto(Item s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ItemDTO toDtoId(Item item);
}
