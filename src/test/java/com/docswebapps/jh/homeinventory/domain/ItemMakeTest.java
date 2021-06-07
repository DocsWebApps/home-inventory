package com.docswebapps.jh.homeinventory.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.docswebapps.jh.homeinventory.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemMakeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemMake.class);
        ItemMake itemMake1 = new ItemMake();
        itemMake1.setId(1L);
        ItemMake itemMake2 = new ItemMake();
        itemMake2.setId(itemMake1.getId());
        assertThat(itemMake1).isEqualTo(itemMake2);
        itemMake2.setId(2L);
        assertThat(itemMake1).isNotEqualTo(itemMake2);
        itemMake1.setId(null);
        assertThat(itemMake1).isNotEqualTo(itemMake2);
    }
}
