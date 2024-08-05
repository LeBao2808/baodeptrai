package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ConfigCriteriaTest {

    @Test
    void newConfigCriteriaHasAllFiltersNullTest() {
        var configCriteria = new ConfigCriteria();
        assertThat(configCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void configCriteriaFluentMethodsCreatesFiltersTest() {
        var configCriteria = new ConfigCriteria();

        setAllFilters(configCriteria);

        assertThat(configCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void configCriteriaCopyCreatesNullFilterTest() {
        var configCriteria = new ConfigCriteria();
        var copy = configCriteria.copy();

        assertThat(configCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(configCriteria)
        );
    }

    @Test
    void configCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var configCriteria = new ConfigCriteria();
        setAllFilters(configCriteria);

        var copy = configCriteria.copy();

        assertThat(configCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(configCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var configCriteria = new ConfigCriteria();

        assertThat(configCriteria).hasToString("ConfigCriteria{}");
    }

    private static void setAllFilters(ConfigCriteria configCriteria) {
        configCriteria.id();
        configCriteria.key();
        configCriteria.value();
        configCriteria.distinct();
    }

    private static Condition<ConfigCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getKey()) &&
                condition.apply(criteria.getValue()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ConfigCriteria> copyFiltersAre(ConfigCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getKey(), copy.getKey()) &&
                condition.apply(criteria.getValue(), copy.getValue()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
