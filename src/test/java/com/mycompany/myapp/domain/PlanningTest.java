package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.PlanningTestSamples.*;
import static com.mycompany.myapp.domain.QuantificationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlanningTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Planning.class);
        Planning planning1 = getPlanningSample1();
        Planning planning2 = new Planning();
        assertThat(planning1).isNotEqualTo(planning2);

        planning2.setId(planning1.getId());
        assertThat(planning1).isEqualTo(planning2);

        planning2 = getPlanningSample2();
        assertThat(planning1).isNotEqualTo(planning2);
    }

    @Test
    void quantificationTest() {
        Planning planning = getPlanningRandomSampleGenerator();
        Quantification quantificationBack = getQuantificationRandomSampleGenerator();

        planning.setQuantification(quantificationBack);
        assertThat(planning.getQuantification()).isEqualTo(quantificationBack);

        planning.quantification(null);
        assertThat(planning.getQuantification()).isNull();
    }
}
