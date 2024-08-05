package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductReceiptDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductReceiptDTO.class);
        ProductReceiptDTO productReceiptDTO1 = new ProductReceiptDTO();
        productReceiptDTO1.setId(1L);
        ProductReceiptDTO productReceiptDTO2 = new ProductReceiptDTO();
        assertThat(productReceiptDTO1).isNotEqualTo(productReceiptDTO2);
        productReceiptDTO2.setId(productReceiptDTO1.getId());
        assertThat(productReceiptDTO1).isEqualTo(productReceiptDTO2);
        productReceiptDTO2.setId(2L);
        assertThat(productReceiptDTO1).isNotEqualTo(productReceiptDTO2);
        productReceiptDTO1.setId(null);
        assertThat(productReceiptDTO1).isNotEqualTo(productReceiptDTO2);
    }
}
