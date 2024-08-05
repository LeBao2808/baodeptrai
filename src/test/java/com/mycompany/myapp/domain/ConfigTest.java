package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ConfigTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConfigTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Config.class);
        Config config1 = getConfigSample1();
        Config config2 = new Config();
        assertThat(config1).isNotEqualTo(config2);

        config2.setId(config1.getId());
        assertThat(config1).isEqualTo(config2);

        config2 = getConfigSample2();
        assertThat(config1).isNotEqualTo(config2);
    }
}
