package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductOrderDetailDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductOrderDetailDTO.class);
        ProductOrderDetailDTO productOrderDetailDTO1 = new ProductOrderDetailDTO();
        productOrderDetailDTO1.setId(1L);
        ProductOrderDetailDTO productOrderDetailDTO2 = new ProductOrderDetailDTO();
        assertThat(productOrderDetailDTO1).isNotEqualTo(productOrderDetailDTO2);
        productOrderDetailDTO2.setId(productOrderDetailDTO1.getId());
        assertThat(productOrderDetailDTO1).isEqualTo(productOrderDetailDTO2);
        productOrderDetailDTO2.setId(2L);
        assertThat(productOrderDetailDTO1).isNotEqualTo(productOrderDetailDTO2);
        productOrderDetailDTO1.setId(null);
        assertThat(productOrderDetailDTO1).isNotEqualTo(productOrderDetailDTO2);
    }
}
