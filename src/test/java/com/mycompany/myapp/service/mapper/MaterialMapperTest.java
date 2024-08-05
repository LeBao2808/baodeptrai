package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.MaterialAsserts.*;
import static com.mycompany.myapp.domain.MaterialTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MaterialMapperTest {

    private MaterialMapper materialMapper;

    @BeforeEach
    void setUp() {
        materialMapper = new MaterialMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMaterialSample1();
        var actual = materialMapper.toEntity(materialMapper.toDto(expected));
        assertMaterialAllPropertiesEquals(expected, actual);
    }
}
