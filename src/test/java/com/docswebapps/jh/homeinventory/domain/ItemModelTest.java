package com.docswebapps.jh.homeinventory.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.docswebapps.jh.homeinventory.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemModelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemModel.class);
        ItemModel itemModel1 = new ItemModel();
        itemModel1.setId(1L);
        ItemModel itemModel2 = new ItemModel();
        itemModel2.setId(itemModel1.getId());
        assertThat(itemModel1).isEqualTo(itemModel2);
        itemModel2.setId(2L);
        assertThat(itemModel1).isNotEqualTo(itemModel2);
        itemModel1.setId(null);
        assertThat(itemModel1).isNotEqualTo(itemModel2);
    }
}
