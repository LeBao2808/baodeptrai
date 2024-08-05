package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.ProductReceiptDetailAsserts.*;
import static com.mycompany.myapp.domain.ProductReceiptDetailTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductReceiptDetailMapperTest {

    private ProductReceiptDetailMapper productReceiptDetailMapper;

    @BeforeEach
    void setUp() {
        productReceiptDetailMapper = new ProductReceiptDetailMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProductReceiptDetailSample1();
        var actual = productReceiptDetailMapper.toEntity(productReceiptDetailMapper.toDto(expected));
        assertProductReceiptDetailAllPropertiesEquals(expected, actual);
    }
}
