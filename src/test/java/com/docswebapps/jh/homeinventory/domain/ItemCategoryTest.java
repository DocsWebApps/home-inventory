package com.docswebapps.jh.homeinventory.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.docswebapps.jh.homeinventory.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemCategory.class);
        ItemCategory itemCategory1 = new ItemCategory();
        itemCategory1.setId(1L);
        ItemCategory itemCategory2 = new ItemCategory();
        itemCategory2.setId(itemCategory1.getId());
        assertThat(itemCategory1).isEqualTo(itemCategory2);
        itemCategory2.setId(2L);
        assertThat(itemCategory1).isNotEqualTo(itemCategory2);
        itemCategory1.setId(null);
        assertThat(itemCategory1).isNotEqualTo(itemCategory2);
    }
}
