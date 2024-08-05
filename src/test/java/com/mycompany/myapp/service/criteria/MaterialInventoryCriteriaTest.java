package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MaterialInventoryCriteriaTest {

    @Test
    void newMaterialInventoryCriteriaHasAllFiltersNullTest() {
        var materialInventoryCriteria = new MaterialInventoryCriteria();
        assertThat(materialInventoryCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void materialInventoryCriteriaFluentMethodsCreatesFiltersTest() {
        var materialInventoryCriteria = new MaterialInventoryCriteria();

        setAllFilters(materialInventoryCriteria);

        assertThat(materialInventoryCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void materialInventoryCriteriaCopyCreatesNullFilterTest() {
        var materialInventoryCriteria = new MaterialInventoryCriteria();
        var copy = materialInventoryCriteria.copy();

        assertThat(materialInventoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(materialInventoryCriteria)
        );
    }

    @Test
    void materialInventoryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var materialInventoryCriteria = new MaterialInventoryCriteria();
        setAllFilters(materialInventoryCriteria);

        var copy = materialInventoryCriteria.copy();

        assertThat(materialInventoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(materialInventoryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var materialInventoryCriteria = new MaterialInventoryCriteria();

        assertThat(materialInventoryCriteria).hasToString("MaterialInventoryCriteria{}");
    }

    private static void setAllFilters(MaterialInventoryCriteria materialInventoryCriteria) {
        materialInventoryCriteria.id();
        materialInventoryCriteria.quantityOnHand();
        materialInventoryCriteria.inventoryMonth();
        materialInventoryCriteria.inventoryYear();
        materialInventoryCriteria.type();
        materialInventoryCriteria.price();
        materialInventoryCriteria.materialId();
        materialInventoryCriteria.distinct();
    }

    private static Condition<MaterialInventoryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getQuantityOnHand()) &&
                condition.apply(criteria.getInventoryMonth()) &&
                condition.apply(criteria.getInventoryYear()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getPrice()) &&
                condition.apply(criteria.getMaterialId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MaterialInventoryCriteria> copyFiltersAre(
        MaterialInventoryCriteria copy,
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
                condition.apply(criteria.getMaterialId(), copy.getMaterialId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
