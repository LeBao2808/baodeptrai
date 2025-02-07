package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class QuantificationAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertQuantificationAllPropertiesEquals(Quantification expected, Quantification actual) {
        assertQuantificationAutoGeneratedPropertiesEquals(expected, actual);
        assertQuantificationAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertQuantificationAllUpdatablePropertiesEquals(Quantification expected, Quantification actual) {
        assertQuantificationUpdatableFieldsEquals(expected, actual);
        assertQuantificationUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertQuantificationAutoGeneratedPropertiesEquals(Quantification expected, Quantification actual) {
        assertThat(expected)
            .as("Verify Quantification auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertQuantificationUpdatableFieldsEquals(Quantification expected, Quantification actual) {
        assertThat(expected)
            .as("Verify Quantification relevant properties")
            .satisfies(e -> assertThat(e.getQuantity()).as("check quantity").isEqualTo(actual.getQuantity()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertQuantificationUpdatableRelationshipsEquals(Quantification expected, Quantification actual) {
        assertThat(expected)
            .as("Verify Quantification relationships")
            .satisfies(e -> assertThat(e.getProduct()).as("check product").isEqualTo(actual.getProduct()))
            .satisfies(e -> assertThat(e.getMaterial()).as("check material").isEqualTo(actual.getMaterial()));
    }
}
