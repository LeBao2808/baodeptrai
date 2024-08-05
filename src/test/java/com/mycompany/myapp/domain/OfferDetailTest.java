package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.OfferDetailTestSamples.*;
import static com.mycompany.myapp.domain.OfferTestSamples.*;
import static com.mycompany.myapp.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OfferDetailTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OfferDetail.class);
        OfferDetail offerDetail1 = getOfferDetailSample1();
        OfferDetail offerDetail2 = new OfferDetail();
        assertThat(offerDetail1).isNotEqualTo(offerDetail2);

        offerDetail2.setId(offerDetail1.getId());
        assertThat(offerDetail1).isEqualTo(offerDetail2);

        offerDetail2 = getOfferDetailSample2();
        assertThat(offerDetail1).isNotEqualTo(offerDetail2);
    }

    @Test
    void productTest() {
        OfferDetail offerDetail = getOfferDetailRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        offerDetail.setProduct(productBack);
        assertThat(offerDetail.getProduct()).isEqualTo(productBack);

        offerDetail.product(null);
        assertThat(offerDetail.getProduct()).isNull();
    }

    @Test
    void offerTest() {
        OfferDetail offerDetail = getOfferDetailRandomSampleGenerator();
        Offer offerBack = getOfferRandomSampleGenerator();

        offerDetail.setOffer(offerBack);
        assertThat(offerDetail.getOffer()).isEqualTo(offerBack);

        offerDetail.offer(null);
        assertThat(offerDetail.getOffer()).isNull();
    }
}
