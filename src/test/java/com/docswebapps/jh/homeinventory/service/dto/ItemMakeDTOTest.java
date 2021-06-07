package com.docswebapps.jh.homeinventory.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.docswebapps.jh.homeinventory.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemMakeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemMakeDTO.class);
        ItemMakeDTO itemMakeDTO1 = new ItemMakeDTO();
        itemMakeDTO1.setId(1L);
        ItemMakeDTO itemMakeDTO2 = new ItemMakeDTO();
        assertThat(itemMakeDTO1).isNotEqualTo(itemMakeDTO2);
        itemMakeDTO2.setId(itemMakeDTO1.getId());
        assertThat(itemMakeDTO1).isEqualTo(itemMakeDTO2);
        itemMakeDTO2.setId(2L);
        assertThat(itemMakeDTO1).isNotEqualTo(itemMakeDTO2);
        itemMakeDTO1.setId(null);
        assertThat(itemMakeDTO1).isNotEqualTo(itemMakeDTO2);
    }
}
