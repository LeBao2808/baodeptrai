package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuantificationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuantificationDTO.class);
        QuantificationDTO quantificationDTO1 = new QuantificationDTO();
        quantificationDTO1.setId(1L);
        QuantificationDTO quantificationDTO2 = new QuantificationDTO();
        assertThat(quantificationDTO1).isNotEqualTo(quantificationDTO2);
        quantificationDTO2.setId(quantificationDTO1.getId());
        assertThat(quantificationDTO1).isEqualTo(quantificationDTO2);
        quantificationDTO2.setId(2L);
        assertThat(quantificationDTO1).isNotEqualTo(quantificationDTO2);
        quantificationDTO1.setId(null);
        assertThat(quantificationDTO1).isNotEqualTo(quantificationDTO2);
    }
}
