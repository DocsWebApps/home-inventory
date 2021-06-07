package com.docswebapps.jh.homeinventory.service.mapper;

import com.docswebapps.jh.homeinventory.domain.ItemModel;
import com.docswebapps.jh.homeinventory.service.dto.ItemModelDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link ItemModel} and its DTO {@link ItemModelDTO}.
 */
@Mapper(componentModel = "spring", uses = { ItemMakeMapper.class })
public interface ItemModelMapper extends EntityMapper<ItemModelDTO, ItemModel> {
    @Mapping(target = "itemMake", source = "itemMake", qualifiedByName = "id")
    ItemModelDTO toDto(ItemModel s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ItemModelDTO toDtoId(ItemModel itemModel);
}
