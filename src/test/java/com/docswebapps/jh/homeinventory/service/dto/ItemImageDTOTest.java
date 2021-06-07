package com.docswebapps.jh.homeinventory.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.docswebapps.jh.homeinventory.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemImageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemImageDTO.class);
        ItemImageDTO itemImageDTO1 = new ItemImageDTO();
        itemImageDTO1.setId(1L);
        ItemImageDTO itemImageDTO2 = new ItemImageDTO();
        assertThat(itemImageDTO1).isNotEqualTo(itemImageDTO2);
        itemImageDTO2.setId(itemImageDTO1.getId());
        assertThat(itemImageDTO1).isEqualTo(itemImageDTO2);
        itemImageDTO2.setId(2L);
        assertThat(itemImageDTO1).isNotEqualTo(itemImageDTO2);
        itemImageDTO1.setId(null);
        assertThat(itemImageDTO1).isNotEqualTo(itemImageDTO2);
    }
}
