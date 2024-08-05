package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.ProductOrderDetailAsserts.*;
import static com.mycompany.myapp.domain.ProductOrderDetailTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductOrderDetailMapperTest {

    private ProductOrderDetailMapper productOrderDetailMapper;

    @BeforeEach
    void setUp() {
        productOrderDetailMapper = new ProductOrderDetailMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProductOrderDetailSample1();
        var actual = productOrderDetailMapper.toEntity(productOrderDetailMapper.toDto(expected));
        assertProductOrderDetailAllPropertiesEquals(expected, actual);
    }
}
