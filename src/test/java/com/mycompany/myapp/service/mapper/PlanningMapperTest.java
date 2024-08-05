package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.PlanningAsserts.*;
import static com.mycompany.myapp.domain.PlanningTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlanningMapperTest {

    private PlanningMapper planningMapper;

    @BeforeEach
    void setUp() {
        planningMapper = new PlanningMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPlanningSample1();
        var actual = planningMapper.toEntity(planningMapper.toDto(expected));
        assertPlanningAllPropertiesEquals(expected, actual);
    }
}
