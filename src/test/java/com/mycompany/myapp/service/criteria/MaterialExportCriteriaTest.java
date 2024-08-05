package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MaterialExportCriteriaTest {

    @Test
    void newMaterialExportCriteriaHasAllFiltersNullTest() {
        var materialExportCriteria = new MaterialExportCriteria();
        assertThat(materialExportCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void materialExportCriteriaFluentMethodsCreatesFiltersTest() {
        var materialExportCriteria = new MaterialExportCriteria();

        setAllFilters(materialExportCriteria);

        assertThat(materialExportCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void materialExportCriteriaCopyCreatesNullFilterTest() {
        var materialExportCriteria = new MaterialExportCriteria();
        var copy = materialExportCriteria.copy();

        assertThat(materialExportCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(materialExportCriteria)
        );
    }

    @Test
    void materialExportCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var materialExportCriteria = new MaterialExportCriteria();
        setAllFilters(materialExportCriteria);

        var copy = materialExportCriteria.copy();

        assertThat(materialExportCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(materialExportCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var materialExportCriteria = new MaterialExportCriteria();

        assertThat(materialExportCriteria).hasToString("MaterialExportCriteria{}");
    }

    private static void setAllFilters(MaterialExportCriteria materialExportCriteria) {
        materialExportCriteria.id();
        materialExportCriteria.creationDate();
        materialExportCriteria.exportDate();
        materialExportCriteria.status();
        materialExportCriteria.code();
        materialExportCriteria.createdByUserId();
        materialExportCriteria.productionSiteId();
        materialExportCriteria.distinct();
    }

    private static Condition<MaterialExportCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCreationDate()) &&
                condition.apply(criteria.getExportDate()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getCreatedByUserId()) &&
                condition.apply(criteria.getProductionSiteId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MaterialExportCriteria> copyFiltersAre(
        MaterialExportCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCreationDate(), copy.getCreationDate()) &&
                condition.apply(criteria.getExportDate(), copy.getExportDate()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getCreatedByUserId(), copy.getCreatedByUserId()) &&
                condition.apply(criteria.getProductionSiteId(), copy.getProductionSiteId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
