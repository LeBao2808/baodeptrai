package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AssertUtils.bigDecimalCompareTo;
import static org.assertj.core.api.Assertions.assertThat;

public class MaterialExportDetailAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMaterialExportDetailAllPropertiesEquals(MaterialExportDetail expected, MaterialExportDetail actual) {
        assertMaterialExportDetailAutoGeneratedPropertiesEquals(expected, actual);
        assertMaterialExportDetailAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMaterialExportDetailAllUpdatablePropertiesEquals(MaterialExportDetail expected, MaterialExportDetail actual) {
        assertMaterialExportDetailUpdatableFieldsEquals(expected, actual);
        assertMaterialExportDetailUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMaterialExportDetailAutoGeneratedPropertiesEquals(MaterialExportDetail expected, MaterialExportDetail actual) {
        assertThat(expected)
            .as("Verify MaterialExportDetail auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMaterialExportDetailUpdatableFieldsEquals(MaterialExportDetail expected, MaterialExportDetail actual) {
        assertThat(expected)
            .as("Verify MaterialExportDetail relevant properties")
            .satisfies(e -> assertThat(e.getNote()).as("check note").isEqualTo(actual.getNote()))
            .satisfies(
                e ->
                    assertThat(e.getExportPrice())
                        .as("check exportPrice")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.getExportPrice())
            )
            .satisfies(e -> assertThat(e.getQuantity()).as("check quantity").isEqualTo(actual.getQuantity()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMaterialExportDetailUpdatableRelationshipsEquals(MaterialExportDetail expected, MaterialExportDetail actual) {
        assertThat(expected)
            .as("Verify MaterialExportDetail relationships")
            .satisfies(e -> assertThat(e.getMaterialExport()).as("check materialExport").isEqualTo(actual.getMaterialExport()))
            .satisfies(e -> assertThat(e.getMaterial()).as("check material").isEqualTo(actual.getMaterial()));
    }
}
