package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PlanningCriteriaTest {

    @Test
    void newPlanningCriteriaHasAllFiltersNullTest() {
        var planningCriteria = new PlanningCriteria();
        assertThat(planningCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void planningCriteriaFluentMethodsCreatesFiltersTest() {
        var planningCriteria = new PlanningCriteria();

        setAllFilters(planningCriteria);

        assertThat(planningCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void planningCriteriaCopyCreatesNullFilterTest() {
        var planningCriteria = new PlanningCriteria();
        var copy = planningCriteria.copy();

        assertThat(planningCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(planningCriteria)
        );
    }

    @Test
    void planningCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var planningCriteria = new PlanningCriteria();
        setAllFilters(planningCriteria);

        var copy = planningCriteria.copy();

        assertThat(planningCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(planningCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var planningCriteria = new PlanningCriteria();

        assertThat(planningCriteria).hasToString("PlanningCriteria{}");
    }

    private static void setAllFilters(PlanningCriteria planningCriteria) {
        planningCriteria.id();
        planningCriteria.orderCreationDate();
        planningCriteria.status();
        planningCriteria.code();
        planningCriteria.quantificationId();
        planningCriteria.distinct();
    }

    private static Condition<PlanningCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getOrderCreationDate()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getQuantificationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PlanningCriteria> copyFiltersAre(PlanningCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getOrderCreationDate(), copy.getOrderCreationDate()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getQuantificationId(), copy.getQuantificationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
