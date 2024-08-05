package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.MaterialReceiptTestSamples.*;
import static com.mycompany.myapp.domain.PlanningTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MaterialReceiptTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaterialReceipt.class);
        MaterialReceipt materialReceipt1 = getMaterialReceiptSample1();
        MaterialReceipt materialReceipt2 = new MaterialReceipt();
        assertThat(materialReceipt1).isNotEqualTo(materialReceipt2);

        materialReceipt2.setId(materialReceipt1.getId());
        assertThat(materialReceipt1).isEqualTo(materialReceipt2);

        materialReceipt2 = getMaterialReceiptSample2();
        assertThat(materialReceipt1).isNotEqualTo(materialReceipt2);
    }

    @Test
    void quantificationOrderTest() {
        MaterialReceipt materialReceipt = getMaterialReceiptRandomSampleGenerator();
        Planning planningBack = getPlanningRandomSampleGenerator();

        materialReceipt.setQuantificationOrder(planningBack);
        assertThat(materialReceipt.getQuantificationOrder()).isEqualTo(planningBack);

        materialReceipt.quantificationOrder(null);
        assertThat(materialReceipt.getQuantificationOrder()).isNull();
    }
}
