package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CustomerTestSamples.*;
import static com.mycompany.myapp.domain.PlanningTestSamples.*;
import static com.mycompany.myapp.domain.ProductOrderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductOrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductOrder.class);
        ProductOrder productOrder1 = getProductOrderSample1();
        ProductOrder productOrder2 = new ProductOrder();
        assertThat(productOrder1).isNotEqualTo(productOrder2);

        productOrder2.setId(productOrder1.getId());
        assertThat(productOrder1).isEqualTo(productOrder2);

        productOrder2 = getProductOrderSample2();
        assertThat(productOrder1).isNotEqualTo(productOrder2);
    }

    @Test
    void quantificationOrderTest() {
        ProductOrder productOrder = getProductOrderRandomSampleGenerator();
        Planning planningBack = getPlanningRandomSampleGenerator();

        productOrder.setQuantificationOrder(planningBack);
        assertThat(productOrder.getQuantificationOrder()).isEqualTo(planningBack);

        productOrder.quantificationOrder(null);
        assertThat(productOrder.getQuantificationOrder()).isNull();
    }

    @Test
    void customerTest() {
        ProductOrder productOrder = getProductOrderRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        productOrder.setCustomer(customerBack);
        assertThat(productOrder.getCustomer()).isEqualTo(customerBack);

        productOrder.customer(null);
        assertThat(productOrder.getCustomer()).isNull();
    }
}
