package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertConfigAllPropertiesEquals(Config expected, Config actual) {
        assertConfigAutoGeneratedPropertiesEquals(expected, actual);
        assertConfigAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertConfigAllUpdatablePropertiesEquals(Config expected, Config actual) {
        assertConfigUpdatableFieldsEquals(expected, actual);
        assertConfigUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertConfigAutoGeneratedPropertiesEquals(Config expected, Config actual) {
        assertThat(expected)
            .as("Verify Config auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertConfigUpdatableFieldsEquals(Config expected, Config actual) {
        assertThat(expected)
            .as("Verify Config relevant properties")
            .satisfies(e -> assertThat(e.getKey()).as("check key").isEqualTo(actual.getKey()))
            .satisfies(e -> assertThat(e.getValue()).as("check value").isEqualTo(actual.getValue()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertConfigUpdatableRelationshipsEquals(Config expected, Config actual) {}
}
