package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OfferDetailCriteriaTest {

    @Test
    void newOfferDetailCriteriaHasAllFiltersNullTest() {
        var offerDetailCriteria = new OfferDetailCriteria();
        assertThat(offerDetailCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void offerDetailCriteriaFluentMethodsCreatesFiltersTest() {
        var offerDetailCriteria = new OfferDetailCriteria();

        setAllFilters(offerDetailCriteria);

        assertThat(offerDetailCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void offerDetailCriteriaCopyCreatesNullFilterTest() {
        var offerDetailCriteria = new OfferDetailCriteria();
        var copy = offerDetailCriteria.copy();

        assertThat(offerDetailCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(offerDetailCriteria)
        );
    }

    @Test
    void offerDetailCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var offerDetailCriteria = new OfferDetailCriteria();
        setAllFilters(offerDetailCriteria);

        var copy = offerDetailCriteria.copy();

        assertThat(offerDetailCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(offerDetailCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var offerDetailCriteria = new OfferDetailCriteria();

        assertThat(offerDetailCriteria).hasToString("OfferDetailCriteria{}");
    }

    private static void setAllFilters(OfferDetailCriteria offerDetailCriteria) {
        offerDetailCriteria.id();
        offerDetailCriteria.feedback();
        offerDetailCriteria.productId();
        offerDetailCriteria.offerId();
        offerDetailCriteria.distinct();
    }

    private static Condition<OfferDetailCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFeedback()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getOfferId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OfferDetailCriteria> copyFiltersAre(OfferDetailCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFeedback(), copy.getFeedback()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getOfferId(), copy.getOfferId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
