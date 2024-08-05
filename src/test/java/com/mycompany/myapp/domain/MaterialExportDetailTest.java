package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.MaterialExportDetailTestSamples.*;
import static com.mycompany.myapp.domain.MaterialExportTestSamples.*;
import static com.mycompany.myapp.domain.MaterialTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MaterialExportDetailTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaterialExportDetail.class);
        MaterialExportDetail materialExportDetail1 = getMaterialExportDetailSample1();
        MaterialExportDetail materialExportDetail2 = new MaterialExportDetail();
        assertThat(materialExportDetail1).isNotEqualTo(materialExportDetail2);

        materialExportDetail2.setId(materialExportDetail1.getId());
        assertThat(materialExportDetail1).isEqualTo(materialExportDetail2);

        materialExportDetail2 = getMaterialExportDetailSample2();
        assertThat(materialExportDetail1).isNotEqualTo(materialExportDetail2);
    }

    @Test
    void materialExportTest() {
        MaterialExportDetail materialExportDetail = getMaterialExportDetailRandomSampleGenerator();
        MaterialExport materialExportBack = getMaterialExportRandomSampleGenerator();

        materialExportDetail.setMaterialExport(materialExportBack);
        assertThat(materialExportDetail.getMaterialExport()).isEqualTo(materialExportBack);

        materialExportDetail.materialExport(null);
        assertThat(materialExportDetail.getMaterialExport()).isNull();
    }

    @Test
    void materialTest() {
        MaterialExportDetail materialExportDetail = getMaterialExportDetailRandomSampleGenerator();
        Material materialBack = getMaterialRandomSampleGenerator();

        materialExportDetail.setMaterial(materialBack);
        assertThat(materialExportDetail.getMaterial()).isEqualTo(materialBack);

        materialExportDetail.material(null);
        assertThat(materialExportDetail.getMaterial()).isNull();
    }
}
