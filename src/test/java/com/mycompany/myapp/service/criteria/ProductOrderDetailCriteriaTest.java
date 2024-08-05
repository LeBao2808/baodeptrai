package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductOrderDetailCriteriaTest {

    @Test
    void newProductOrderDetailCriteriaHasAllFiltersNullTest() {
        var productOrderDetailCriteria = new ProductOrderDetailCriteria();
        assertThat(productOrderDetailCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void productOrderDetailCriteriaFluentMethodsCreatesFiltersTest() {
        var productOrderDetailCriteria = new ProductOrderDetailCriteria();

        setAllFilters(productOrderDetailCriteria);

        assertThat(productOrderDetailCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void productOrderDetailCriteriaCopyCreatesNullFilterTest() {
        var productOrderDetailCriteria = new ProductOrderDetailCriteria();
        var copy = productOrderDetailCriteria.copy();

        assertThat(productOrderDetailCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(productOrderDetailCriteria)
        );
    }

    @Test
    void productOrderDetailCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productOrderDetailCriteria = new ProductOrderDetailCriteria();
        setAllFilters(productOrderDetailCriteria);

        var copy = productOrderDetailCriteria.copy();

        assertThat(productOrderDetailCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(productOrderDetailCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productOrderDetailCriteria = new ProductOrderDetailCriteria();

        assertThat(productOrderDetailCriteria).hasToString("ProductOrderDetailCriteria{}");
    }

    private static void setAllFilters(ProductOrderDetailCriteria productOrderDetailCriteria) {
        productOrderDetailCriteria.id();
        productOrderDetailCriteria.orderCreationDate();
        productOrderDetailCriteria.quantity();
        productOrderDetailCriteria.unitPrice();
        productOrderDetailCriteria.orderId();
        productOrderDetailCriteria.productId();
        productOrderDetailCriteria.distinct();
    }

    private static Condition<ProductOrderDetailCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getOrderCreationDate()) &&
                condition.apply(criteria.getQuantity()) &&
                condition.apply(criteria.getUnitPrice()) &&
                condition.apply(criteria.getOrderId()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProductOrderDetailCriteria> copyFiltersAre(
        ProductOrderDetailCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getOrderCreationDate(), copy.getOrderCreationDate()) &&
                condition.apply(criteria.getQuantity(), copy.getQuantity()) &&
                condition.apply(criteria.getUnitPrice(), copy.getUnitPrice()) &&
                condition.apply(criteria.getOrderId(), copy.getOrderId()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
