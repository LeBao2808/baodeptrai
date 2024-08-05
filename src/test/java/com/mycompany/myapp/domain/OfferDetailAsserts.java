package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class OfferDetailAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOfferDetailAllPropertiesEquals(OfferDetail expected, OfferDetail actual) {
        assertOfferDetailAutoGeneratedPropertiesEquals(expected, actual);
        assertOfferDetailAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOfferDetailAllUpdatablePropertiesEquals(OfferDetail expected, OfferDetail actual) {
        assertOfferDetailUpdatableFieldsEquals(expected, actual);
        assertOfferDetailUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOfferDetailAutoGeneratedPropertiesEquals(OfferDetail expected, OfferDetail actual) {
        assertThat(expected)
            .as("Verify OfferDetail auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOfferDetailUpdatableFieldsEquals(OfferDetail expected, OfferDetail actual) {
        assertThat(expected)
            .as("Verify OfferDetail relevant properties")
            .satisfies(e -> assertThat(e.getFeedback()).as("check feedback").isEqualTo(actual.getFeedback()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOfferDetailUpdatableRelationshipsEquals(OfferDetail expected, OfferDetail actual) {
        assertThat(expected)
            .as("Verify OfferDetail relationships")
            .satisfies(e -> assertThat(e.getProduct()).as("check product").isEqualTo(actual.getProduct()))
            .satisfies(e -> assertThat(e.getOffer()).as("check offer").isEqualTo(actual.getOffer()));
    }
}
