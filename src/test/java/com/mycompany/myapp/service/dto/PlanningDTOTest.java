package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlanningDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlanningDTO.class);
        PlanningDTO planningDTO1 = new PlanningDTO();
        planningDTO1.setId(1L);
        PlanningDTO planningDTO2 = new PlanningDTO();
        assertThat(planningDTO1).isNotEqualTo(planningDTO2);
        planningDTO2.setId(planningDTO1.getId());
        assertThat(planningDTO1).isEqualTo(planningDTO2);
        planningDTO2.setId(2L);
        assertThat(planningDTO1).isNotEqualTo(planningDTO2);
        planningDTO1.setId(null);
        assertThat(planningDTO1).isNotEqualTo(planningDTO2);
    }
}
