package com.docswebapps.jh.homeinventory.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.docswebapps.jh.homeinventory.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemOwnerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemOwner.class);
        ItemOwner itemOwner1 = new ItemOwner();
        itemOwner1.setId(1L);
        ItemOwner itemOwner2 = new ItemOwner();
        itemOwner2.setId(itemOwner1.getId());
        assertThat(itemOwner1).isEqualTo(itemOwner2);
        itemOwner2.setId(2L);
        assertThat(itemOwner1).isNotEqualTo(itemOwner2);
        itemOwner1.setId(null);
        assertThat(itemOwner1).isNotEqualTo(itemOwner2);
    }
}
