package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MaterialReceiptDetailDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaterialReceiptDetailDTO.class);
        MaterialReceiptDetailDTO materialReceiptDetailDTO1 = new MaterialReceiptDetailDTO();
        materialReceiptDetailDTO1.setId(1L);
        MaterialReceiptDetailDTO materialReceiptDetailDTO2 = new MaterialReceiptDetailDTO();
        assertThat(materialReceiptDetailDTO1).isNotEqualTo(materialReceiptDetailDTO2);
        materialReceiptDetailDTO2.setId(materialReceiptDetailDTO1.getId());
        assertThat(materialReceiptDetailDTO1).isEqualTo(materialReceiptDetailDTO2);
        materialReceiptDetailDTO2.setId(2L);
        assertThat(materialReceiptDetailDTO1).isNotEqualTo(materialReceiptDetailDTO2);
        materialReceiptDetailDTO1.setId(null);
        assertThat(materialReceiptDetailDTO1).isNotEqualTo(materialReceiptDetailDTO2);
    }
}
