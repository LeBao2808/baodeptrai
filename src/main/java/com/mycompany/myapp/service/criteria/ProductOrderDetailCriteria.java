package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.ProductOrderDetail} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ProductOrderDetailResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /product-order-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductOrderDetailCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter orderCreationDate;

    private IntegerFilter quantity;

    private FloatFilter unitPrice;

    private LongFilter orderId;

    private LongFilter productId;

    private Boolean distinct;

    public ProductOrderDetailCriteria() {}

    public ProductOrderDetailCriteria(ProductOrderDetailCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.orderCreationDate = other.optionalOrderCreationDate().map(InstantFilter::copy).orElse(null);
        this.quantity = other.optionalQuantity().map(IntegerFilter::copy).orElse(null);
        this.unitPrice = other.optionalUnitPrice().map(FloatFilter::copy).orElse(null);
        this.orderId = other.optionalOrderId().map(LongFilter::copy).orElse(null);
        this.productId = other.optionalProductId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ProductOrderDetailCriteria copy() {
        return new ProductOrderDetailCriteria(this);
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

    public InstantFilter getOrderCreationDate() {
        return orderCreationDate;
    }

    public Optional<InstantFilter> optionalOrderCreationDate() {
        return Optional.ofNullable(orderCreationDate);
    }

    public InstantFilter orderCreationDate() {
        if (orderCreationDate == null) {
            setOrderCreationDate(new InstantFilter());
        }
        return orderCreationDate;
    }

    public void setOrderCreationDate(InstantFilter orderCreationDate) {
        this.orderCreationDate = orderCreationDate;
    }

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public Optional<IntegerFilter> optionalQuantity() {
        return Optional.ofNullable(quantity);
    }

    public IntegerFilter quantity() {
        if (quantity == null) {
            setQuantity(new IntegerFilter());
        }
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
    }

    public FloatFilter getUnitPrice() {
        return unitPrice;
    }

    public Optional<FloatFilter> optionalUnitPrice() {
        return Optional.ofNullable(unitPrice);
    }

    public FloatFilter unitPrice() {
        if (unitPrice == null) {
            setUnitPrice(new FloatFilter());
        }
        return unitPrice;
    }

    public void setUnitPrice(FloatFilter unitPrice) {
        this.unitPrice = unitPrice;
    }

    public LongFilter getOrderId() {
        return orderId;
    }

    public Optional<LongFilter> optionalOrderId() {
        return Optional.ofNullable(orderId);
    }

    public LongFilter orderId() {
        if (orderId == null) {
            setOrderId(new LongFilter());
        }
        return orderId;
    }

    public void setOrderId(LongFilter orderId) {
        this.orderId = orderId;
    }

    public LongFilter getProductId() {
        return productId;
    }

    public Optional<LongFilter> optionalProductId() {
        return Optional.ofNullable(productId);
    }

    public LongFilter productId() {
        if (productId == null) {
            setProductId(new LongFilter());
        }
        return productId;
    }

    public void setProductId(LongFilter productId) {
        this.productId = productId;
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
        final ProductOrderDetailCriteria that = (ProductOrderDetailCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(orderCreationDate, that.orderCreationDate) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(unitPrice, that.unitPrice) &&
            Objects.equals(orderId, that.orderId) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderCreationDate, quantity, unitPrice, orderId, productId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductOrderDetailCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalOrderCreationDate().map(f -> "orderCreationDate=" + f + ", ").orElse("") +
            optionalQuantity().map(f -> "quantity=" + f + ", ").orElse("") +
            optionalUnitPrice().map(f -> "unitPrice=" + f + ", ").orElse("") +
            optionalOrderId().map(f -> "orderId=" + f + ", ").orElse("") +
            optionalProductId().map(f -> "productId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
