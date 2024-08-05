package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ProductTestSamples.*;
import static com.mycompany.myapp.domain.ProductionSiteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductionSiteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductionSite.class);
        ProductionSite productionSite1 = getProductionSiteSample1();
        ProductionSite productionSite2 = new ProductionSite();
        assertThat(productionSite1).isNotEqualTo(productionSite2);

        productionSite2.setId(productionSite1.getId());
        assertThat(productionSite1).isEqualTo(productionSite2);

        productionSite2 = getProductionSiteSample2();
        assertThat(productionSite1).isNotEqualTo(productionSite2);
    }

    @Test
    void productIdTest() {
        ProductionSite productionSite = getProductionSiteRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        productionSite.setProductId(productBack);
        assertThat(productionSite.getProductId()).isEqualTo(productBack);

        productionSite.productId(null);
        assertThat(productionSite.getProductId()).isNull();
    }
}
