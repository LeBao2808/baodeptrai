package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MaterialReceiptCriteriaTest {

    @Test
    void newMaterialReceiptCriteriaHasAllFiltersNullTest() {
        var materialReceiptCriteria = new MaterialReceiptCriteria();
        assertThat(materialReceiptCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void materialReceiptCriteriaFluentMethodsCreatesFiltersTest() {
        var materialReceiptCriteria = new MaterialReceiptCriteria();

        setAllFilters(materialReceiptCriteria);

        assertThat(materialReceiptCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void materialReceiptCriteriaCopyCreatesNullFilterTest() {
        var materialReceiptCriteria = new MaterialReceiptCriteria();
        var copy = materialReceiptCriteria.copy();

        assertThat(materialReceiptCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(materialReceiptCriteria)
        );
    }

    @Test
    void materialReceiptCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var materialReceiptCriteria = new MaterialReceiptCriteria();
        setAllFilters(materialReceiptCriteria);

        var copy = materialReceiptCriteria.copy();

        assertThat(materialReceiptCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(materialReceiptCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var materialReceiptCriteria = new MaterialReceiptCriteria();

        assertThat(materialReceiptCriteria).hasToString("MaterialReceiptCriteria{}");
    }

    private static void setAllFilters(MaterialReceiptCriteria materialReceiptCriteria) {
        materialReceiptCriteria.id();
        materialReceiptCriteria.creationDate();
        materialReceiptCriteria.paymentDate();
        materialReceiptCriteria.receiptDate();
        materialReceiptCriteria.status();
        materialReceiptCriteria.code();
        materialReceiptCriteria.createdByUserId();
        materialReceiptCriteria.quantificationOrderId();
        materialReceiptCriteria.distinct();
    }

    private static Condition<MaterialReceiptCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCreationDate()) &&
                condition.apply(criteria.getPaymentDate()) &&
                condition.apply(criteria.getReceiptDate()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getCreatedByUserId()) &&
                condition.apply(criteria.getQuantificationOrderId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MaterialReceiptCriteria> copyFiltersAre(
        MaterialReceiptCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCreationDate(), copy.getCreationDate()) &&
                condition.apply(criteria.getPaymentDate(), copy.getPaymentDate()) &&
                condition.apply(criteria.getReceiptDate(), copy.getReceiptDate()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getCreatedByUserId(), copy.getCreatedByUserId()) &&
                condition.apply(criteria.getQuantificationOrderId(), copy.getQuantificationOrderId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
