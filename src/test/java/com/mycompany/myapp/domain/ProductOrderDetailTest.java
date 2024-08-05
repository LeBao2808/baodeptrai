package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ProductOrderDetailTestSamples.*;
import static com.mycompany.myapp.domain.ProductOrderTestSamples.*;
import static com.mycompany.myapp.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductOrderDetailTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductOrderDetail.class);
        ProductOrderDetail productOrderDetail1 = getProductOrderDetailSample1();
        ProductOrderDetail productOrderDetail2 = new ProductOrderDetail();
        assertThat(productOrderDetail1).isNotEqualTo(productOrderDetail2);

        productOrderDetail2.setId(productOrderDetail1.getId());
        assertThat(productOrderDetail1).isEqualTo(productOrderDetail2);

        productOrderDetail2 = getProductOrderDetailSample2();
        assertThat(productOrderDetail1).isNotEqualTo(productOrderDetail2);
    }

    @Test
    void orderTest() {
        ProductOrderDetail productOrderDetail = getProductOrderDetailRandomSampleGenerator();
        ProductOrder productOrderBack = getProductOrderRandomSampleGenerator();

        productOrderDetail.setOrder(productOrderBack);
        assertThat(productOrderDetail.getOrder()).isEqualTo(productOrderBack);

        productOrderDetail.order(null);
        assertThat(productOrderDetail.getOrder()).isNull();
    }

    @Test
    void productTest() {
        ProductOrderDetail productOrderDetail = getProductOrderDetailRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        productOrderDetail.setProduct(productBack);
        assertThat(productOrderDetail.getProduct()).isEqualTo(productBack);

        productOrderDetail.product(null);
        assertThat(productOrderDetail.getProduct()).isNull();
    }
}
