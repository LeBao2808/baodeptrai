package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.ProductInventoryAsserts.*;
import static com.mycompany.myapp.domain.ProductInventoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductInventoryMapperTest {

    private ProductInventoryMapper productInventoryMapper;

    @BeforeEach
    void setUp() {
        productInventoryMapper = new ProductInventoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProductInventorySample1();
        var actual = productInventoryMapper.toEntity(productInventoryMapper.toDto(expected));
        assertProductInventoryAllPropertiesEquals(expected, actual);
    }
}
