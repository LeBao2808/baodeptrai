package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.MaterialExportAsserts.*;
import static com.mycompany.myapp.domain.MaterialExportTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MaterialExportMapperTest {

    private MaterialExportMapper materialExportMapper;

    @BeforeEach
    void setUp() {
        materialExportMapper = new MaterialExportMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMaterialExportSample1();
        var actual = materialExportMapper.toEntity(materialExportMapper.toDto(expected));
        assertMaterialExportAllPropertiesEquals(expected, actual);
    }
}
