package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.QuantificationAsserts.*;
import static com.mycompany.myapp.domain.QuantificationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuantificationMapperTest {

    private QuantificationMapper quantificationMapper;

    @BeforeEach
    void setUp() {
        quantificationMapper = new QuantificationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getQuantificationSample1();
        var actual = quantificationMapper.toEntity(quantificationMapper.toDto(expected));
        assertQuantificationAllPropertiesEquals(expected, actual);
    }
}
