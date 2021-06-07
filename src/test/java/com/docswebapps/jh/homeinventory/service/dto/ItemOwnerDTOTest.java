package com.docswebapps.jh.homeinventory.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.docswebapps.jh.homeinventory.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemOwnerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemOwnerDTO.class);
        ItemOwnerDTO itemOwnerDTO1 = new ItemOwnerDTO();
        itemOwnerDTO1.setId(1L);
        ItemOwnerDTO itemOwnerDTO2 = new ItemOwnerDTO();
        assertThat(itemOwnerDTO1).isNotEqualTo(itemOwnerDTO2);
        itemOwnerDTO2.setId(itemOwnerDTO1.getId());
        assertThat(itemOwnerDTO1).isEqualTo(itemOwnerDTO2);
        itemOwnerDTO2.setId(2L);
        assertThat(itemOwnerDTO1).isNotEqualTo(itemOwnerDTO2);
        itemOwnerDTO1.setId(null);
        assertThat(itemOwnerDTO1).isNotEqualTo(itemOwnerDTO2);
    }
}
