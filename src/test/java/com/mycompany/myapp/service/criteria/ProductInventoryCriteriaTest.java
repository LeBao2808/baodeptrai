package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductInventoryCriteriaTest {

    @Test
    void newProductInventoryCriteriaHasAllFiltersNullTest() {
        var productInventoryCriteria = new ProductInventoryCriteria();
        assertThat(productInventoryCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void productInventoryCriteriaFluentMethodsCreatesFiltersTest() {
        var productInventoryCriteria = new ProductInventoryCriteria();

        setAllFilters(productInventoryCriteria);

        assertThat(productInventoryCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void productInventoryCriteriaCopyCreatesNullFilterTest() {
        var productInventoryCriteria = new ProductInventoryCriteria();
        var copy = productInventoryCriteria.copy();

        assertThat(productInventoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(productInventoryCriteria)
        );
    }

    @Test
    void productInventoryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productInventoryCriteria = new ProductInventoryCriteria();
        setAllFilters(productInventoryCriteria);

        var copy = productInventoryCriteria.copy();

        assertThat(productInventoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(productInventoryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productInventoryCriteria = new ProductInventoryCriteria();

        assertThat(productInventoryCriteria).hasToString("ProductInventoryCriteria{}");
    }

    private static void setAllFilters(ProductInventoryCriteria productInventoryCriteria) {
        productInventoryCriteria.id();
        productInventoryCriteria.quantityOnHand();
        productInventoryCriteria.inventoryMonth();
        productInventoryCriteria.inventoryYear();
        productInventoryCriteria.type();
        productInventoryCriteria.price();
        productInventoryCriteria.productId();
        productInventoryCriteria.distinct();
    }

    private static Condition<ProductInventoryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getQuantityOnHand()) &&
                condition.apply(criteria.getInventoryMonth()) &&
                condition.apply(criteria.getInventoryYear()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getPrice()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProductInventoryCriteria> copyFiltersAre(
        ProductInventoryCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getQuantityOnHand(), copy.getQuantityOnHand()) &&
                condition.apply(criteria.getInventoryMonth(), copy.getInventoryMonth()) &&
                condition.apply(criteria.getInventoryYear(), copy.getInventoryYear()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getPrice(), copy.getPrice()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
