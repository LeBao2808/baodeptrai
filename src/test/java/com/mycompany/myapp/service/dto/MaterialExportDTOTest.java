package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MaterialExportDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaterialExportDTO.class);
        MaterialExportDTO materialExportDTO1 = new MaterialExportDTO();
        materialExportDTO1.setId(1L);
        MaterialExportDTO materialExportDTO2 = new MaterialExportDTO();
        assertThat(materialExportDTO1).isNotEqualTo(materialExportDTO2);
        materialExportDTO2.setId(materialExportDTO1.getId());
        assertThat(materialExportDTO1).isEqualTo(materialExportDTO2);
        materialExportDTO2.setId(2L);
        assertThat(materialExportDTO1).isNotEqualTo(materialExportDTO2);
        materialExportDTO1.setId(null);
        assertThat(materialExportDTO1).isNotEqualTo(materialExportDTO2);
    }
}
