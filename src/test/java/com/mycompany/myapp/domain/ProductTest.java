package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.MaterialTestSamples.*;
import static com.mycompany.myapp.domain.ProductTestSamples.*;
import static com.mycompany.myapp.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = getProductSample1();
        Product product2 = new Product();
        assertThat(product1).isNotEqualTo(product2);

        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);

        product2 = getProductSample2();
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void setIdProductTest() {
        Product product = getProductRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        product.setSetIdProduct(productBack);
        assertThat(product.getSetIdProduct()).isEqualTo(productBack);

        product.setIdProduct(null);
        assertThat(product.getSetIdProduct()).isNull();
    }

    @Test
    void parentProductTest() {
        Product product = getProductRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        product.setParentProduct(productBack);
        assertThat(product.getParentProduct()).isEqualTo(productBack);

        product.parentProduct(null);
        assertThat(product.getParentProduct()).isNull();
    }

    @Test
    void materialTest() {
        Product product = getProductRandomSampleGenerator();
        Material materialBack = getMaterialRandomSampleGenerator();

        product.setMaterial(materialBack);
        assertThat(product.getMaterial()).isEqualTo(materialBack);

        product.material(null);
        assertThat(product.getMaterial()).isNull();
    }
}
