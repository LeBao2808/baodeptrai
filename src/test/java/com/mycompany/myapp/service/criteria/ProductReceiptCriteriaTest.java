package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductReceiptCriteriaTest {

    @Test
    void newProductReceiptCriteriaHasAllFiltersNullTest() {
        var productReceiptCriteria = new ProductReceiptCriteria();
        assertThat(productReceiptCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void productReceiptCriteriaFluentMethodsCreatesFiltersTest() {
        var productReceiptCriteria = new ProductReceiptCriteria();

        setAllFilters(productReceiptCriteria);

        assertThat(productReceiptCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void productReceiptCriteriaCopyCreatesNullFilterTest() {
        var productReceiptCriteria = new ProductReceiptCriteria();
        var copy = productReceiptCriteria.copy();

        assertThat(productReceiptCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(productReceiptCriteria)
        );
    }

    @Test
    void productReceiptCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productReceiptCriteria = new ProductReceiptCriteria();
        setAllFilters(productReceiptCriteria);

        var copy = productReceiptCriteria.copy();

        assertThat(productReceiptCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(productReceiptCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productReceiptCriteria = new ProductReceiptCriteria();

        assertThat(productReceiptCriteria).hasToString("ProductReceiptCriteria{}");
    }

    private static void setAllFilters(ProductReceiptCriteria productReceiptCriteria) {
        productReceiptCriteria.id();
        productReceiptCriteria.creationDate();
        productReceiptCriteria.paymentDate();
        productReceiptCriteria.receiptDate();
        productReceiptCriteria.status();
        productReceiptCriteria.code();
        productReceiptCriteria.createdId();
        productReceiptCriteria.distinct();
    }

    private static Condition<ProductReceiptCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCreationDate()) &&
                condition.apply(criteria.getPaymentDate()) &&
                condition.apply(criteria.getReceiptDate()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getCreatedId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProductReceiptCriteria> copyFiltersAre(
        ProductReceiptCriteria copy,
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
                condition.apply(criteria.getCreatedId(), copy.getCreatedId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
