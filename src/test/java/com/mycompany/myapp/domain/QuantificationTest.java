package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.MaterialTestSamples.*;
import static com.mycompany.myapp.domain.ProductTestSamples.*;
import static com.mycompany.myapp.domain.QuantificationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuantificationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Quantification.class);
        Quantification quantification1 = getQuantificationSample1();
        Quantification quantification2 = new Quantification();
        assertThat(quantification1).isNotEqualTo(quantification2);

        quantification2.setId(quantification1.getId());
        assertThat(quantification1).isEqualTo(quantification2);

        quantification2 = getQuantificationSample2();
        assertThat(quantification1).isNotEqualTo(quantification2);
    }

    @Test
    void productTest() {
        Quantification quantification = getQuantificationRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        quantification.setProduct(productBack);
        assertThat(quantification.getProduct()).isEqualTo(productBack);

        quantification.product(null);
        assertThat(quantification.getProduct()).isNull();
    }

    @Test
    void materialTest() {
        Quantification quantification = getQuantificationRandomSampleGenerator();
        Material materialBack = getMaterialRandomSampleGenerator();

        quantification.setMaterial(materialBack);
        assertThat(quantification.getMaterial()).isEqualTo(materialBack);

        quantification.material(null);
        assertThat(quantification.getMaterial()).isNull();
    }
}
