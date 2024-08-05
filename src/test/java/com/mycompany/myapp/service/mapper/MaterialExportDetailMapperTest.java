package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.MaterialExportDetailAsserts.*;
import static com.mycompany.myapp.domain.MaterialExportDetailTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MaterialExportDetailMapperTest {

    private MaterialExportDetailMapper materialExportDetailMapper;

    @BeforeEach
    void setUp() {
        materialExportDetailMapper = new MaterialExportDetailMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMaterialExportDetailSample1();
        var actual = materialExportDetailMapper.toEntity(materialExportDetailMapper.toDto(expected));
        assertMaterialExportDetailAllPropertiesEquals(expected, actual);
    }
}
