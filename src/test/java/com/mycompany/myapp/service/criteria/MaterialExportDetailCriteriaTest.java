package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MaterialExportDetailCriteriaTest {

    @Test
    void newMaterialExportDetailCriteriaHasAllFiltersNullTest() {
        var materialExportDetailCriteria = new MaterialExportDetailCriteria();
        assertThat(materialExportDetailCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void materialExportDetailCriteriaFluentMethodsCreatesFiltersTest() {
        var materialExportDetailCriteria = new MaterialExportDetailCriteria();

        setAllFilters(materialExportDetailCriteria);

        assertThat(materialExportDetailCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void materialExportDetailCriteriaCopyCreatesNullFilterTest() {
        var materialExportDetailCriteria = new MaterialExportDetailCriteria();
        var copy = materialExportDetailCriteria.copy();

        assertThat(materialExportDetailCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(materialExportDetailCriteria)
        );
    }

    @Test
    void materialExportDetailCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var materialExportDetailCriteria = new MaterialExportDetailCriteria();
        setAllFilters(materialExportDetailCriteria);

        var copy = materialExportDetailCriteria.copy();

        assertThat(materialExportDetailCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(materialExportDetailCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var materialExportDetailCriteria = new MaterialExportDetailCriteria();

        assertThat(materialExportDetailCriteria).hasToString("MaterialExportDetailCriteria{}");
    }

    private static void setAllFilters(MaterialExportDetailCriteria materialExportDetailCriteria) {
        materialExportDetailCriteria.id();
        materialExportDetailCriteria.note();
        materialExportDetailCriteria.exportPrice();
        materialExportDetailCriteria.quantity();
        materialExportDetailCriteria.materialExportId();
        materialExportDetailCriteria.materialId();
        materialExportDetailCriteria.distinct();
    }

    private static Condition<MaterialExportDetailCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNote()) &&
                condition.apply(criteria.getExportPrice()) &&
                condition.apply(criteria.getQuantity()) &&
                condition.apply(criteria.getMaterialExportId()) &&
                condition.apply(criteria.getMaterialId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MaterialExportDetailCriteria> copyFiltersAre(
        MaterialExportDetailCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNote(), copy.getNote()) &&
                condition.apply(criteria.getExportPrice(), copy.getExportPrice()) &&
                condition.apply(criteria.getQuantity(), copy.getQuantity()) &&
                condition.apply(criteria.getMaterialExportId(), copy.getMaterialExportId()) &&
                condition.apply(criteria.getMaterialId(), copy.getMaterialId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
