package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OfferCriteriaTest {

    @Test
    void newOfferCriteriaHasAllFiltersNullTest() {
        var offerCriteria = new OfferCriteria();
        assertThat(offerCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void offerCriteriaFluentMethodsCreatesFiltersTest() {
        var offerCriteria = new OfferCriteria();

        setAllFilters(offerCriteria);

        assertThat(offerCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void offerCriteriaCopyCreatesNullFilterTest() {
        var offerCriteria = new OfferCriteria();
        var copy = offerCriteria.copy();

        assertThat(offerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(offerCriteria)
        );
    }

    @Test
    void offerCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var offerCriteria = new OfferCriteria();
        setAllFilters(offerCriteria);

        var copy = offerCriteria.copy();

        assertThat(offerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(offerCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var offerCriteria = new OfferCriteria();

        assertThat(offerCriteria).hasToString("OfferCriteria{}");
    }

    private static void setAllFilters(OfferCriteria offerCriteria) {
        offerCriteria.id();
        offerCriteria.date();
        offerCriteria.status();
        offerCriteria.code();
        offerCriteria.customerId();
        offerCriteria.userId();
        offerCriteria.distinct();
    }

    private static Condition<OfferCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDate()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getCustomerId()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OfferCriteria> copyFiltersAre(OfferCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDate(), copy.getDate()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getCustomerId(), copy.getCustomerId()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
