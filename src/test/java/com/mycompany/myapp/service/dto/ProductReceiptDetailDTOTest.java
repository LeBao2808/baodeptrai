package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductReceiptDetailDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductReceiptDetailDTO.class);
        ProductReceiptDetailDTO productReceiptDetailDTO1 = new ProductReceiptDetailDTO();
        productReceiptDetailDTO1.setId(1L);
        ProductReceiptDetailDTO productReceiptDetailDTO2 = new ProductReceiptDetailDTO();
        assertThat(productReceiptDetailDTO1).isNotEqualTo(productReceiptDetailDTO2);
        productReceiptDetailDTO2.setId(productReceiptDetailDTO1.getId());
        assertThat(productReceiptDetailDTO1).isEqualTo(productReceiptDetailDTO2);
        productReceiptDetailDTO2.setId(2L);
        assertThat(productReceiptDetailDTO1).isNotEqualTo(productReceiptDetailDTO2);
        productReceiptDetailDTO1.setId(null);
        assertThat(productReceiptDetailDTO1).isNotEqualTo(productReceiptDetailDTO2);
    }
}
