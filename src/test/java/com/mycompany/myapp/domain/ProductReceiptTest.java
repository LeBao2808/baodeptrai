package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ProductReceiptTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductReceiptTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductReceipt.class);
        ProductReceipt productReceipt1 = getProductReceiptSample1();
        ProductReceipt productReceipt2 = new ProductReceipt();
        assertThat(productReceipt1).isNotEqualTo(productReceipt2);

        productReceipt2.setId(productReceipt1.getId());
        assertThat(productReceipt1).isEqualTo(productReceipt2);

        productReceipt2 = getProductReceiptSample2();
        assertThat(productReceipt1).isNotEqualTo(productReceipt2);
    }
}
