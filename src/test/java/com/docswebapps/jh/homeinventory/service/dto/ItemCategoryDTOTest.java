package com.docswebapps.jh.homeinventory.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.docswebapps.jh.homeinventory.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemCategoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemCategoryDTO.class);
        ItemCategoryDTO itemCategoryDTO1 = new ItemCategoryDTO();
        itemCategoryDTO1.setId(1L);
        ItemCategoryDTO itemCategoryDTO2 = new ItemCategoryDTO();
        assertThat(itemCategoryDTO1).isNotEqualTo(itemCategoryDTO2);
        itemCategoryDTO2.setId(itemCategoryDTO1.getId());
        assertThat(itemCategoryDTO1).isEqualTo(itemCategoryDTO2);
        itemCategoryDTO2.setId(2L);
        assertThat(itemCategoryDTO1).isNotEqualTo(itemCategoryDTO2);
        itemCategoryDTO1.setId(null);
        assertThat(itemCategoryDTO1).isNotEqualTo(itemCategoryDTO2);
    }
}
