package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MaterialReceiptDetailCriteriaTest {

    @Test
    void newMaterialReceiptDetailCriteriaHasAllFiltersNullTest() {
        var materialReceiptDetailCriteria = new MaterialReceiptDetailCriteria();
        assertThat(materialReceiptDetailCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void materialReceiptDetailCriteriaFluentMethodsCreatesFiltersTest() {
        var materialReceiptDetailCriteria = new MaterialReceiptDetailCriteria();

        setAllFilters(materialReceiptDetailCriteria);

        assertThat(materialReceiptDetailCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void materialReceiptDetailCriteriaCopyCreatesNullFilterTest() {
        var materialReceiptDetailCriteria = new MaterialReceiptDetailCriteria();
        var copy = materialReceiptDetailCriteria.copy();

        assertThat(materialReceiptDetailCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(materialReceiptDetailCriteria)
        );
    }

    @Test
    void materialReceiptDetailCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var materialReceiptDetailCriteria = new MaterialReceiptDetailCriteria();
        setAllFilters(materialReceiptDetailCriteria);

        var copy = materialReceiptDetailCriteria.copy();

        assertThat(materialReceiptDetailCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(materialReceiptDetailCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var materialReceiptDetailCriteria = new MaterialReceiptDetailCriteria();

        assertThat(materialReceiptDetailCriteria).hasToString("MaterialReceiptDetailCriteria{}");
    }

    private static void setAllFilters(MaterialReceiptDetailCriteria materialReceiptDetailCriteria) {
        materialReceiptDetailCriteria.id();
        materialReceiptDetailCriteria.note();
        materialReceiptDetailCriteria.importPrice();
        materialReceiptDetailCriteria.quantity();
        materialReceiptDetailCriteria.materialId();
        materialReceiptDetailCriteria.receiptId();
        materialReceiptDetailCriteria.distinct();
    }

    private static Condition<MaterialReceiptDetailCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNote()) &&
                condition.apply(criteria.getImportPrice()) &&
                condition.apply(criteria.getQuantity()) &&
                condition.apply(criteria.getMaterialId()) &&
                condition.apply(criteria.getReceiptId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MaterialReceiptDetailCriteria> copyFiltersAre(
        MaterialReceiptDetailCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNote(), copy.getNote()) &&
                condition.apply(criteria.getImportPrice(), copy.getImportPrice()) &&
                condition.apply(criteria.getQuantity(), copy.getQuantity()) &&
                condition.apply(criteria.getMaterialId(), copy.getMaterialId()) &&
                condition.apply(criteria.getReceiptId(), copy.getReceiptId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
