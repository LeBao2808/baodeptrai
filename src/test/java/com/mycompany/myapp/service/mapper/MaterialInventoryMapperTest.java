package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.MaterialInventoryAsserts.*;
import static com.mycompany.myapp.domain.MaterialInventoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MaterialInventoryMapperTest {

    private MaterialInventoryMapper materialInventoryMapper;

    @BeforeEach
    void setUp() {
        materialInventoryMapper = new MaterialInventoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMaterialInventorySample1();
        var actual = materialInventoryMapper.toEntity(materialInventoryMapper.toDto(expected));
        assertMaterialInventoryAllPropertiesEquals(expected, actual);
    }
}
