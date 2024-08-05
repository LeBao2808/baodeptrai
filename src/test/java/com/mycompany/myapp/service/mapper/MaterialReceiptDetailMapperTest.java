package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.MaterialReceiptDetailAsserts.*;
import static com.mycompany.myapp.domain.MaterialReceiptDetailTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MaterialReceiptDetailMapperTest {

    private MaterialReceiptDetailMapper materialReceiptDetailMapper;

    @BeforeEach
    void setUp() {
        materialReceiptDetailMapper = new MaterialReceiptDetailMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMaterialReceiptDetailSample1();
        var actual = materialReceiptDetailMapper.toEntity(materialReceiptDetailMapper.toDto(expected));
        assertMaterialReceiptDetailAllPropertiesEquals(expected, actual);
    }
}
