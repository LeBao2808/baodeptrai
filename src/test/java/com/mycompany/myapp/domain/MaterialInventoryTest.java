package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.MaterialInventoryTestSamples.*;
import static com.mycompany.myapp.domain.MaterialTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MaterialInventoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaterialInventory.class);
        MaterialInventory materialInventory1 = getMaterialInventorySample1();
        MaterialInventory materialInventory2 = new MaterialInventory();
        assertThat(materialInventory1).isNotEqualTo(materialInventory2);

        materialInventory2.setId(materialInventory1.getId());
        assertThat(materialInventory1).isEqualTo(materialInventory2);

        materialInventory2 = getMaterialInventorySample2();
        assertThat(materialInventory1).isNotEqualTo(materialInventory2);
    }

    @Test
    void materialTest() {
        MaterialInventory materialInventory = getMaterialInventoryRandomSampleGenerator();
        Material materialBack = getMaterialRandomSampleGenerator();

        materialInventory.setMaterial(materialBack);
        assertThat(materialInventory.getMaterial()).isEqualTo(materialBack);

        materialInventory.material(null);
        assertThat(materialInventory.getMaterial()).isNull();
    }
}
