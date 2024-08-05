package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OfferDetailDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OfferDetailDTO.class);
        OfferDetailDTO offerDetailDTO1 = new OfferDetailDTO();
        offerDetailDTO1.setId(1L);
        OfferDetailDTO offerDetailDTO2 = new OfferDetailDTO();
        assertThat(offerDetailDTO1).isNotEqualTo(offerDetailDTO2);
        offerDetailDTO2.setId(offerDetailDTO1.getId());
        assertThat(offerDetailDTO1).isEqualTo(offerDetailDTO2);
        offerDetailDTO2.setId(2L);
        assertThat(offerDetailDTO1).isNotEqualTo(offerDetailDTO2);
        offerDetailDTO1.setId(null);
        assertThat(offerDetailDTO1).isNotEqualTo(offerDetailDTO2);
    }
}
