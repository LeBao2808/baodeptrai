package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.MaterialExportTestSamples.*;
import static com.mycompany.myapp.domain.ProductionSiteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MaterialExportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaterialExport.class);
        MaterialExport materialExport1 = getMaterialExportSample1();
        MaterialExport materialExport2 = new MaterialExport();
        assertThat(materialExport1).isNotEqualTo(materialExport2);

        materialExport2.setId(materialExport1.getId());
        assertThat(materialExport1).isEqualTo(materialExport2);

        materialExport2 = getMaterialExportSample2();
        assertThat(materialExport1).isNotEqualTo(materialExport2);
    }

    @Test
    void productionSiteTest() {
        MaterialExport materialExport = getMaterialExportRandomSampleGenerator();
        ProductionSite productionSiteBack = getProductionSiteRandomSampleGenerator();

        materialExport.setProductionSite(productionSiteBack);
        assertThat(materialExport.getProductionSite()).isEqualTo(productionSiteBack);

        materialExport.productionSite(null);
        assertThat(materialExport.getProductionSite()).isNull();
    }
}
