package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.MaterialReceiptAsserts.*;
import static com.mycompany.myapp.domain.MaterialReceiptTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MaterialReceiptMapperTest {

    private MaterialReceiptMapper materialReceiptMapper;

    @BeforeEach
    void setUp() {
        materialReceiptMapper = new MaterialReceiptMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMaterialReceiptSample1();
        var actual = materialReceiptMapper.toEntity(materialReceiptMapper.toDto(expected));
        assertMaterialReceiptAllPropertiesEquals(expected, actual);
    }
}
