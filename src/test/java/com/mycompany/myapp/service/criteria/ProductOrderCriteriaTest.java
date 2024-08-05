package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductOrderCriteriaTest {

    @Test
    void newProductOrderCriteriaHasAllFiltersNullTest() {
        var productOrderCriteria = new ProductOrderCriteria();
        assertThat(productOrderCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void productOrderCriteriaFluentMethodsCreatesFiltersTest() {
        var productOrderCriteria = new ProductOrderCriteria();

        setAllFilters(productOrderCriteria);

        assertThat(productOrderCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void productOrderCriteriaCopyCreatesNullFilterTest() {
        var productOrderCriteria = new ProductOrderCriteria();
        var copy = productOrderCriteria.copy();

        assertThat(productOrderCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(productOrderCriteria)
        );
    }

    @Test
    void productOrderCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productOrderCriteria = new ProductOrderCriteria();
        setAllFilters(productOrderCriteria);

        var copy = productOrderCriteria.copy();

        assertThat(productOrderCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(productOrderCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productOrderCriteria = new ProductOrderCriteria();

        assertThat(productOrderCriteria).hasToString("ProductOrderCriteria{}");
    }

    private static void setAllFilters(ProductOrderCriteria productOrderCriteria) {
        productOrderCriteria.id();
        productOrderCriteria.paymentMethod();
        productOrderCriteria.note();
        productOrderCriteria.status();
        productOrderCriteria.orderDate();
        productOrderCriteria.paymentDate();
        productOrderCriteria.warehouseReleaseDate();
        productOrderCriteria.code();
        productOrderCriteria.quantificationOrderId();
        productOrderCriteria.customerId();
        productOrderCriteria.createdByUserId();
        productOrderCriteria.distinct();
    }

    private static Condition<ProductOrderCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPaymentMethod()) &&
                condition.apply(criteria.getNote()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getOrderDate()) &&
                condition.apply(criteria.getPaymentDate()) &&
                condition.apply(criteria.getWarehouseReleaseDate()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getQuantificationOrderId()) &&
                condition.apply(criteria.getCustomerId()) &&
                condition.apply(criteria.getCreatedByUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProductOrderCriteria> copyFiltersAre(
        ProductOrderCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPaymentMethod(), copy.getPaymentMethod()) &&
                condition.apply(criteria.getNote(), copy.getNote()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getOrderDate(), copy.getOrderDate()) &&
                condition.apply(criteria.getPaymentDate(), copy.getPaymentDate()) &&
                condition.apply(criteria.getWarehouseReleaseDate(), copy.getWarehouseReleaseDate()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getQuantificationOrderId(), copy.getQuantificationOrderId()) &&
                condition.apply(criteria.getCustomerId(), copy.getCustomerId()) &&
                condition.apply(criteria.getCreatedByUserId(), copy.getCreatedByUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
