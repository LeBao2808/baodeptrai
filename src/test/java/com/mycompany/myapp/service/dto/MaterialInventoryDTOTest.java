package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MaterialInventoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaterialInventoryDTO.class);
        MaterialInventoryDTO materialInventoryDTO1 = new MaterialInventoryDTO();
        materialInventoryDTO1.setId(1L);
        MaterialInventoryDTO materialInventoryDTO2 = new MaterialInventoryDTO();
        assertThat(materialInventoryDTO1).isNotEqualTo(materialInventoryDTO2);
        materialInventoryDTO2.setId(materialInventoryDTO1.getId());
        assertThat(materialInventoryDTO1).isEqualTo(materialInventoryDTO2);
        materialInventoryDTO2.setId(2L);
        assertThat(materialInventoryDTO1).isNotEqualTo(materialInventoryDTO2);
        materialInventoryDTO1.setId(null);
        assertThat(materialInventoryDTO1).isNotEqualTo(materialInventoryDTO2);
    }
}
