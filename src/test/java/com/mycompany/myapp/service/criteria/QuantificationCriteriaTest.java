package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class QuantificationCriteriaTest {

    @Test
    void newQuantificationCriteriaHasAllFiltersNullTest() {
        var quantificationCriteria = new QuantificationCriteria();
        assertThat(quantificationCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void quantificationCriteriaFluentMethodsCreatesFiltersTest() {
        var quantificationCriteria = new QuantificationCriteria();

        setAllFilters(quantificationCriteria);

        assertThat(quantificationCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void quantificationCriteriaCopyCreatesNullFilterTest() {
        var quantificationCriteria = new QuantificationCriteria();
        var copy = quantificationCriteria.copy();

        assertThat(quantificationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(quantificationCriteria)
        );
    }

    @Test
    void quantificationCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var quantificationCriteria = new QuantificationCriteria();
        setAllFilters(quantificationCriteria);

        var copy = quantificationCriteria.copy();

        assertThat(quantificationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(quantificationCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var quantificationCriteria = new QuantificationCriteria();

        assertThat(quantificationCriteria).hasToString("QuantificationCriteria{}");
    }

    private static void setAllFilters(QuantificationCriteria quantificationCriteria) {
        quantificationCriteria.id();
        quantificationCriteria.quantity();
        quantificationCriteria.productId();
        quantificationCriteria.materialId();
        quantificationCriteria.distinct();
    }

    private static Condition<QuantificationCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getQuantity()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getMaterialId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<QuantificationCriteria> copyFiltersAre(
        QuantificationCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getQuantity(), copy.getQuantity()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getMaterialId(), copy.getMaterialId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
