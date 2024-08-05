package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.OfferDetailAsserts.*;
import static com.mycompany.myapp.domain.OfferDetailTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OfferDetailMapperTest {

    private OfferDetailMapper offerDetailMapper;

    @BeforeEach
    void setUp() {
        offerDetailMapper = new OfferDetailMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOfferDetailSample1();
        var actual = offerDetailMapper.toEntity(offerDetailMapper.toDto(expected));
        assertOfferDetailAllPropertiesEquals(expected, actual);
    }
}
