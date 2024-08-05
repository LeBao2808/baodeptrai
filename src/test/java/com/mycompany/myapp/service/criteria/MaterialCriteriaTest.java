package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MaterialCriteriaTest {

    @Test
    void newMaterialCriteriaHasAllFiltersNullTest() {
        var materialCriteria = new MaterialCriteria();
        assertThat(materialCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void materialCriteriaFluentMethodsCreatesFiltersTest() {
        var materialCriteria = new MaterialCriteria();

        setAllFilters(materialCriteria);

        assertThat(materialCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void materialCriteriaCopyCreatesNullFilterTest() {
        var materialCriteria = new MaterialCriteria();
        var copy = materialCriteria.copy();

        assertThat(materialCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(materialCriteria)
        );
    }

    @Test
    void materialCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var materialCriteria = new MaterialCriteria();
        setAllFilters(materialCriteria);

        var copy = materialCriteria.copy();

        assertThat(materialCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(materialCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var materialCriteria = new MaterialCriteria();

        assertThat(materialCriteria).hasToString("MaterialCriteria{}");
    }

    private static void setAllFilters(MaterialCriteria materialCriteria) {
        materialCriteria.id();
        materialCriteria.name();
        materialCriteria.unit();
        materialCriteria.code();
        materialCriteria.description();
        materialCriteria.imgUrl();
        materialCriteria.distinct();
    }

    private static Condition<MaterialCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getUnit()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getImgUrl()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MaterialCriteria> copyFiltersAre(MaterialCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getUnit(), copy.getUnit()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getImgUrl(), copy.getImgUrl()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
