package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.ProductReceiptAsserts.*;
import static com.mycompany.myapp.domain.ProductReceiptTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductReceiptMapperTest {

    private ProductReceiptMapper productReceiptMapper;

    @BeforeEach
    void setUp() {
        productReceiptMapper = new ProductReceiptMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProductReceiptSample1();
        var actual = productReceiptMapper.toEntity(productReceiptMapper.toDto(expected));
        assertProductReceiptAllPropertiesEquals(expected, actual);
    }
}
