package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ProductInventoryTestSamples.*;
import static com.mycompany.myapp.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductInventoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductInventory.class);
        ProductInventory productInventory1 = getProductInventorySample1();
        ProductInventory productInventory2 = new ProductInventory();
        assertThat(productInventory1).isNotEqualTo(productInventory2);

        productInventory2.setId(productInventory1.getId());
        assertThat(productInventory1).isEqualTo(productInventory2);

        productInventory2 = getProductInventorySample2();
        assertThat(productInventory1).isNotEqualTo(productInventory2);
    }

    @Test
    void productTest() {
        ProductInventory productInventory = getProductInventoryRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        productInventory.setProduct(productBack);
        assertThat(productInventory.getProduct()).isEqualTo(productBack);

        productInventory.product(null);
        assertThat(productInventory.getProduct()).isNull();
    }
}
