package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.OfferAsserts.*;
import static com.mycompany.myapp.domain.OfferTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OfferMapperTest {

    private OfferMapper offerMapper;

    @BeforeEach
    void setUp() {
        offerMapper = new OfferMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOfferSample1();
        var actual = offerMapper.toEntity(offerMapper.toDto(expected));
        assertOfferAllPropertiesEquals(expected, actual);
    }
}
