package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.MaterialInventory} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.MaterialInventoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /material-inventories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaterialInventoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private FloatFilter quantityOnHand;

    private IntegerFilter inventoryMonth;

    private IntegerFilter inventoryYear;

    private IntegerFilter type;

    private BigDecimalFilter price;

    private LongFilter materialId;

    private Boolean distinct;

    public MaterialInventoryCriteria() {}

    public MaterialInventoryCriteria(MaterialInventoryCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.quantityOnHand = other.optionalQuantityOnHand().map(FloatFilter::copy).orElse(null);
        this.inventoryMonth = other.optionalInventoryMonth().map(IntegerFilter::copy).orElse(null);
        this.inventoryYear = other.optionalInventoryYear().map(IntegerFilter::copy).orElse(null);
        this.type = other.optionalType().map(IntegerFilter::copy).orElse(null);
        this.price = other.optionalPrice().map(BigDecimalFilter::copy).orElse(null);
        this.materialId = other.optionalMaterialId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MaterialInventoryCriteria copy() {
        return new MaterialInventoryCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public FloatFilter getQuantityOnHand() {
        return quantityOnHand;
    }

    public Optional<FloatFilter> optionalQuantityOnHand() {
        return Optional.ofNullable(quantityOnHand);
    }

    public FloatFilter quantityOnHand() {
        if (quantityOnHand == null) {
            setQuantityOnHand(new FloatFilter());
        }
        return quantityOnHand;
    }

    public void setQuantityOnHand(FloatFilter quantityOnHand) {
        this.quantityOnHand = quantityOnHand;
    }

    public IntegerFilter getInventoryMonth() {
        return inventoryMonth;
    }

    public Optional<IntegerFilter> optionalInventoryMonth() {
        return Optional.ofNullable(inventoryMonth);
    }

    public IntegerFilter inventoryMonth() {
        if (inventoryMonth == null) {
            setInventoryMonth(new IntegerFilter());
        }
        return inventoryMonth;
    }

    public void setInventoryMonth(IntegerFilter inventoryMonth) {
        this.inventoryMonth = inventoryMonth;
    }

    public IntegerFilter getInventoryYear() {
        return inventoryYear;
    }

    public Optional<IntegerFilter> optionalInventoryYear() {
        return Optional.ofNullable(inventoryYear);
    }

    public IntegerFilter inventoryYear() {
        if (inventoryYear == null) {
            setInventoryYear(new IntegerFilter());
        }
        return inventoryYear;
    }

    public void setInventoryYear(IntegerFilter inventoryYear) {
        this.inventoryYear = inventoryYear;
    }

    public IntegerFilter getType() {
        return type;
    }

    public Optional<IntegerFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public IntegerFilter type() {
        if (type == null) {
            setType(new IntegerFilter());
        }
        return type;
    }

    public void setType(IntegerFilter type) {
        this.type = type;
    }

    public BigDecimalFilter getPrice() {
        return price;
    }

    public Optional<BigDecimalFilter> optionalPrice() {
        return Optional.ofNullable(price);
    }

    public BigDecimalFilter price() {
        if (price == null) {
            setPrice(new BigDecimalFilter());
        }
        return price;
    }

    public void setPrice(BigDecimalFilter price) {
        this.price = price;
    }

    public LongFilter getMaterialId() {
        return materialId;
    }

    public Optional<LongFilter> optionalMaterialId() {
        return Optional.ofNullable(materialId);
    }

    public LongFilter materialId() {
        if (materialId == null) {
            setMaterialId(new LongFilter());
        }
        return materialId;
    }

    public void setMaterialId(LongFilter materialId) {
        this.materialId = materialId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MaterialInventoryCriteria that = (MaterialInventoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(quantityOnHand, that.quantityOnHand) &&
            Objects.equals(inventoryMonth, that.inventoryMonth) &&
            Objects.equals(inventoryYear, that.inventoryYear) &&
            Objects.equals(type, that.type) &&
            Objects.equals(price, that.price) &&
            Objects.equals(materialId, that.materialId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quantityOnHand, inventoryMonth, inventoryYear, type, price, materialId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaterialInventoryCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalQuantityOnHand().map(f -> "quantityOnHand=" + f + ", ").orElse("") +
            optionalInventoryMonth().map(f -> "inventoryMonth=" + f + ", ").orElse("") +
            optionalInventoryYear().map(f -> "inventoryYear=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalPrice().map(f -> "price=" + f + ", ").orElse("") +
            optionalMaterialId().map(f -> "materialId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
