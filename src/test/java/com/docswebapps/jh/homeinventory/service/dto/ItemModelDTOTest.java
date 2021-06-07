package com.docswebapps.jh.homeinventory.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.docswebapps.jh.homeinventory.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemModelDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemModelDTO.class);
        ItemModelDTO itemModelDTO1 = new ItemModelDTO();
        itemModelDTO1.setId(1L);
        ItemModelDTO itemModelDTO2 = new ItemModelDTO();
        assertThat(itemModelDTO1).isNotEqualTo(itemModelDTO2);
        itemModelDTO2.setId(itemModelDTO1.getId());
        assertThat(itemModelDTO1).isEqualTo(itemModelDTO2);
        itemModelDTO2.setId(2L);
        assertThat(itemModelDTO1).isNotEqualTo(itemModelDTO2);
        itemModelDTO1.setId(null);
        assertThat(itemModelDTO1).isNotEqualTo(itemModelDTO2);
    }
}
