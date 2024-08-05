package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MaterialExportDetailDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaterialExportDetailDTO.class);
        MaterialExportDetailDTO materialExportDetailDTO1 = new MaterialExportDetailDTO();
        materialExportDetailDTO1.setId(1L);
        MaterialExportDetailDTO materialExportDetailDTO2 = new MaterialExportDetailDTO();
        assertThat(materialExportDetailDTO1).isNotEqualTo(materialExportDetailDTO2);
        materialExportDetailDTO2.setId(materialExportDetailDTO1.getId());
        assertThat(materialExportDetailDTO1).isEqualTo(materialExportDetailDTO2);
        materialExportDetailDTO2.setId(2L);
        assertThat(materialExportDetailDTO1).isNotEqualTo(materialExportDetailDTO2);
        materialExportDetailDTO1.setId(null);
        assertThat(materialExportDetailDTO1).isNotEqualTo(materialExportDetailDTO2);
    }
}
