package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.MaterialTestSamples.*;
import static com.mycompany.myapp.domain.SupplierTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SupplierTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Supplier.class);
        Supplier supplier1 = getSupplierSample1();
        Supplier supplier2 = new Supplier();
        assertThat(supplier1).isNotEqualTo(supplier2);

        supplier2.setId(supplier1.getId());
        assertThat(supplier1).isEqualTo(supplier2);

        supplier2 = getSupplierSample2();
        assertThat(supplier1).isNotEqualTo(supplier2);
    }

    @Test
    void materialIdTest() {
        Supplier supplier = getSupplierRandomSampleGenerator();
        Material materialBack = getMaterialRandomSampleGenerator();

        supplier.setMaterialId(materialBack);
        assertThat(supplier.getMaterialId()).isEqualTo(materialBack);

        supplier.materialId(null);
        assertThat(supplier.getMaterialId()).isNull();
    }
}
