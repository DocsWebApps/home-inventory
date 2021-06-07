package com.docswebapps.jh.homeinventory.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.docswebapps.jh.homeinventory.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemImageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemImage.class);
        ItemImage itemImage1 = new ItemImage();
        itemImage1.setId(1L);
        ItemImage itemImage2 = new ItemImage();
        itemImage2.setId(itemImage1.getId());
        assertThat(itemImage1).isEqualTo(itemImage2);
        itemImage2.setId(2L);
        assertThat(itemImage1).isNotEqualTo(itemImage2);
        itemImage1.setId(null);
        assertThat(itemImage1).isNotEqualTo(itemImage2);
    }
}
