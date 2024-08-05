package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MaterialReceiptDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaterialReceiptDTO.class);
        MaterialReceiptDTO materialReceiptDTO1 = new MaterialReceiptDTO();
        materialReceiptDTO1.setId(1L);
        MaterialReceiptDTO materialReceiptDTO2 = new MaterialReceiptDTO();
        assertThat(materialReceiptDTO1).isNotEqualTo(materialReceiptDTO2);
        materialReceiptDTO2.setId(materialReceiptDTO1.getId());
        assertThat(materialReceiptDTO1).isEqualTo(materialReceiptDTO2);
        materialReceiptDTO2.setId(2L);
        assertThat(materialReceiptDTO1).isNotEqualTo(materialReceiptDTO2);
        materialReceiptDTO1.setId(null);
        assertThat(materialReceiptDTO1).isNotEqualTo(materialReceiptDTO2);
    }
}
