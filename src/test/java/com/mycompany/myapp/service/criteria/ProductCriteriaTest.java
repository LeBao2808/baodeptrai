package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductCriteriaTest {

    @Test
    void newProductCriteriaHasAllFiltersNullTest() {
        var productCriteria = new ProductCriteria();
        assertThat(productCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void productCriteriaFluentMethodsCreatesFiltersTest() {
        var productCriteria = new ProductCriteria();

        setAllFilters(productCriteria);

        assertThat(productCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void productCriteriaCopyCreatesNullFilterTest() {
        var productCriteria = new ProductCriteria();
        var copy = productCriteria.copy();

        assertThat(productCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(productCriteria)
        );
    }

    @Test
    void productCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productCriteria = new ProductCriteria();
        setAllFilters(productCriteria);

        var copy = productCriteria.copy();

        assertThat(productCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(productCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productCriteria = new ProductCriteria();

        assertThat(productCriteria).hasToString("ProductCriteria{}");
    }

    private static void setAllFilters(ProductCriteria productCriteria) {
        productCriteria.id();
        productCriteria.name();
        productCriteria.code();
        productCriteria.unit();
        productCriteria.description();
        productCriteria.weight();
        productCriteria.height();
        productCriteria.width();
        productCriteria.length();
        productCriteria.imageUrl();
        productCriteria.type();
        productCriteria.color();
        productCriteria.cbm();
        productCriteria.price();
        productCriteria.construction();
        productCriteria.masterPackingQty();
        productCriteria.masterNestCode();
        productCriteria.innerQty();
        productCriteria.packSize();
        productCriteria.nestCode();
        productCriteria.countryOfOrigin();
        productCriteria.vendorName();
        productCriteria.numberOfSet();
        productCriteria.priceFOB();
        productCriteria.qty40HC();
        productCriteria.d57Qty();
        productCriteria.category();
        productCriteria.labels();
        productCriteria.planningNotes();
        productCriteria.factTag();
        productCriteria.packagingLength();
        productCriteria.packagingHeight();
        productCriteria.packagingWidth();
        productCriteria.setIdProductId();
        productCriteria.parentProductId();
        productCriteria.materialId();
        productCriteria.distinct();
    }

    private static Condition<ProductCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getUnit()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getWeight()) &&
                condition.apply(criteria.getHeight()) &&
                condition.apply(criteria.getWidth()) &&
                condition.apply(criteria.getLength()) &&
                condition.apply(criteria.getImageUrl()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getColor()) &&
                condition.apply(criteria.getCbm()) &&
                condition.apply(criteria.getPrice()) &&
                condition.apply(criteria.getConstruction()) &&
                condition.apply(criteria.getMasterPackingQty()) &&
                condition.apply(criteria.getMasterNestCode()) &&
                condition.apply(criteria.getInnerQty()) &&
                condition.apply(criteria.getPackSize()) &&
                condition.apply(criteria.getNestCode()) &&
                condition.apply(criteria.getCountryOfOrigin()) &&
                condition.apply(criteria.getVendorName()) &&
                condition.apply(criteria.getNumberOfSet()) &&
                condition.apply(criteria.getPriceFOB()) &&
                condition.apply(criteria.getQty40HC()) &&
                condition.apply(criteria.getd57Qty()) &&
                condition.apply(criteria.getCategory()) &&
                condition.apply(criteria.getLabels()) &&
                condition.apply(criteria.getPlanningNotes()) &&
                condition.apply(criteria.getFactTag()) &&
                condition.apply(criteria.getPackagingLength()) &&
                condition.apply(criteria.getPackagingHeight()) &&
                condition.apply(criteria.getPackagingWidth()) &&
                condition.apply(criteria.getSetIdProductId()) &&
                condition.apply(criteria.getParentProductId()) &&
                condition.apply(criteria.getMaterialId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProductCriteria> copyFiltersAre(ProductCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getUnit(), copy.getUnit()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getWeight(), copy.getWeight()) &&
                condition.apply(criteria.getHeight(), copy.getHeight()) &&
                condition.apply(criteria.getWidth(), copy.getWidth()) &&
                condition.apply(criteria.getLength(), copy.getLength()) &&
                condition.apply(criteria.getImageUrl(), copy.getImageUrl()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getColor(), copy.getColor()) &&
                condition.apply(criteria.getCbm(), copy.getCbm()) &&
                condition.apply(criteria.getPrice(), copy.getPrice()) &&
                condition.apply(criteria.getConstruction(), copy.getConstruction()) &&
                condition.apply(criteria.getMasterPackingQty(), copy.getMasterPackingQty()) &&
                condition.apply(criteria.getMasterNestCode(), copy.getMasterNestCode()) &&
                condition.apply(criteria.getInnerQty(), copy.getInnerQty()) &&
                condition.apply(criteria.getPackSize(), copy.getPackSize()) &&
                condition.apply(criteria.getNestCode(), copy.getNestCode()) &&
                condition.apply(criteria.getCountryOfOrigin(), copy.getCountryOfOrigin()) &&
                condition.apply(criteria.getVendorName(), copy.getVendorName()) &&
                condition.apply(criteria.getNumberOfSet(), copy.getNumberOfSet()) &&
                condition.apply(criteria.getPriceFOB(), copy.getPriceFOB()) &&
                condition.apply(criteria.getQty40HC(), copy.getQty40HC()) &&
                condition.apply(criteria.getd57Qty(), copy.getd57Qty()) &&
                condition.apply(criteria.getCategory(), copy.getCategory()) &&
                condition.apply(criteria.getLabels(), copy.getLabels()) &&
                condition.apply(criteria.getPlanningNotes(), copy.getPlanningNotes()) &&
                condition.apply(criteria.getFactTag(), copy.getFactTag()) &&
                condition.apply(criteria.getPackagingLength(), copy.getPackagingLength()) &&
                condition.apply(criteria.getPackagingHeight(), copy.getPackagingHeight()) &&
                condition.apply(criteria.getPackagingWidth(), copy.getPackagingWidth()) &&
                condition.apply(criteria.getSetIdProductId(), copy.getSetIdProductId()) &&
                condition.apply(criteria.getParentProductId(), copy.getParentProductId()) &&
                condition.apply(criteria.getMaterialId(), copy.getMaterialId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
