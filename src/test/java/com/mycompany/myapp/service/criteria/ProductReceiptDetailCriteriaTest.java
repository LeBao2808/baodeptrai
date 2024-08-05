package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductReceiptDetailCriteriaTest {

    @Test
    void newProductReceiptDetailCriteriaHasAllFiltersNullTest() {
        var productReceiptDetailCriteria = new ProductReceiptDetailCriteria();
        assertThat(productReceiptDetailCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void productReceiptDetailCriteriaFluentMethodsCreatesFiltersTest() {
        var productReceiptDetailCriteria = new ProductReceiptDetailCriteria();

        setAllFilters(productReceiptDetailCriteria);

        assertThat(productReceiptDetailCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void productReceiptDetailCriteriaCopyCreatesNullFilterTest() {
        var productReceiptDetailCriteria = new ProductReceiptDetailCriteria();
        var copy = productReceiptDetailCriteria.copy();

        assertThat(productReceiptDetailCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(productReceiptDetailCriteria)
        );
    }

    @Test
    void productReceiptDetailCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productReceiptDetailCriteria = new ProductReceiptDetailCriteria();
        setAllFilters(productReceiptDetailCriteria);

        var copy = productReceiptDetailCriteria.copy();

        assertThat(productReceiptDetailCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(productReceiptDetailCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productReceiptDetailCriteria = new ProductReceiptDetailCriteria();

        assertThat(productReceiptDetailCriteria).hasToString("ProductReceiptDetailCriteria{}");
    }

    private static void setAllFilters(ProductReceiptDetailCriteria productReceiptDetailCriteria) {
        productReceiptDetailCriteria.id();
        productReceiptDetailCriteria.note();
        productReceiptDetailCriteria.quantity();
        productReceiptDetailCriteria.productId();
        productReceiptDetailCriteria.receiptId();
        productReceiptDetailCriteria.distinct();
    }

    private static Condition<ProductReceiptDetailCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNote()) &&
                condition.apply(criteria.getQuantity()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getReceiptId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProductReceiptDetailCriteria> copyFiltersAre(
        ProductReceiptDetailCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNote(), copy.getNote()) &&
                condition.apply(criteria.getQuantity(), copy.getQuantity()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getReceiptId(), copy.getReceiptId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
