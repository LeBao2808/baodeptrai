package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.MaterialReceiptDetailTestSamples.*;
import static com.mycompany.myapp.domain.MaterialReceiptTestSamples.*;
import static com.mycompany.myapp.domain.MaterialTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MaterialReceiptDetailTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaterialReceiptDetail.class);
        MaterialReceiptDetail materialReceiptDetail1 = getMaterialReceiptDetailSample1();
        MaterialReceiptDetail materialReceiptDetail2 = new MaterialReceiptDetail();
        assertThat(materialReceiptDetail1).isNotEqualTo(materialReceiptDetail2);

        materialReceiptDetail2.setId(materialReceiptDetail1.getId());
        assertThat(materialReceiptDetail1).isEqualTo(materialReceiptDetail2);

        materialReceiptDetail2 = getMaterialReceiptDetailSample2();
        assertThat(materialReceiptDetail1).isNotEqualTo(materialReceiptDetail2);
    }

    @Test
    void materialTest() {
        MaterialReceiptDetail materialReceiptDetail = getMaterialReceiptDetailRandomSampleGenerator();
        Material materialBack = getMaterialRandomSampleGenerator();

        materialReceiptDetail.setMaterial(materialBack);
        assertThat(materialReceiptDetail.getMaterial()).isEqualTo(materialBack);

        materialReceiptDetail.material(null);
        assertThat(materialReceiptDetail.getMaterial()).isNull();
    }

    @Test
    void receiptTest() {
        MaterialReceiptDetail materialReceiptDetail = getMaterialReceiptDetailRandomSampleGenerator();
        MaterialReceipt materialReceiptBack = getMaterialReceiptRandomSampleGenerator();

        materialReceiptDetail.setReceipt(materialReceiptBack);
        assertThat(materialReceiptDetail.getReceipt()).isEqualTo(materialReceiptBack);

        materialReceiptDetail.receipt(null);
        assertThat(materialReceiptDetail.getReceipt()).isNull();
    }
}
