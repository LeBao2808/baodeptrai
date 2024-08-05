package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ProductReceiptDetailTestSamples.*;
import static com.mycompany.myapp.domain.ProductReceiptTestSamples.*;
import static com.mycompany.myapp.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductReceiptDetailTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductReceiptDetail.class);
        ProductReceiptDetail productReceiptDetail1 = getProductReceiptDetailSample1();
        ProductReceiptDetail productReceiptDetail2 = new ProductReceiptDetail();
        assertThat(productReceiptDetail1).isNotEqualTo(productReceiptDetail2);

        productReceiptDetail2.setId(productReceiptDetail1.getId());
        assertThat(productReceiptDetail1).isEqualTo(productReceiptDetail2);

        productReceiptDetail2 = getProductReceiptDetailSample2();
        assertThat(productReceiptDetail1).isNotEqualTo(productReceiptDetail2);
    }

    @Test
    void productTest() {
        ProductReceiptDetail productReceiptDetail = getProductReceiptDetailRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        productReceiptDetail.setProduct(productBack);
        assertThat(productReceiptDetail.getProduct()).isEqualTo(productBack);

        productReceiptDetail.product(null);
        assertThat(productReceiptDetail.getProduct()).isNull();
    }

    @Test
    void receiptTest() {
        ProductReceiptDetail productReceiptDetail = getProductReceiptDetailRandomSampleGenerator();
        ProductReceipt productReceiptBack = getProductReceiptRandomSampleGenerator();

        productReceiptDetail.setReceipt(productReceiptBack);
        assertThat(productReceiptDetail.getReceipt()).isEqualTo(productReceiptBack);

        productReceiptDetail.receipt(null);
        assertThat(productReceiptDetail.getReceipt()).isNull();
    }
}
