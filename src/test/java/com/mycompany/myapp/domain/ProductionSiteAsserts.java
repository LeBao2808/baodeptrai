package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductionSiteAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProductionSiteAllPropertiesEquals(ProductionSite expected, ProductionSite actual) {
        assertProductionSiteAutoGeneratedPropertiesEquals(expected, actual);
        assertProductionSiteAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProductionSiteAllUpdatablePropertiesEquals(ProductionSite expected, ProductionSite actual) {
        assertProductionSiteUpdatableFieldsEquals(expected, actual);
        assertProductionSiteUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProductionSiteAutoGeneratedPropertiesEquals(ProductionSite expected, ProductionSite actual) {
        assertThat(expected)
            .as("Verify ProductionSite auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProductionSiteUpdatableFieldsEquals(ProductionSite expected, ProductionSite actual) {
        assertThat(expected)
            .as("Verify ProductionSite relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getAddress()).as("check address").isEqualTo(actual.getAddress()))
            .satisfies(e -> assertThat(e.getPhone()).as("check phone").isEqualTo(actual.getPhone()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProductionSiteUpdatableRelationshipsEquals(ProductionSite expected, ProductionSite actual) {
        assertThat(expected)
            .as("Verify ProductionSite relationships")
            .satisfies(e -> assertThat(e.getProductId()).as("check productId").isEqualTo(actual.getProductId()));
    }
}
