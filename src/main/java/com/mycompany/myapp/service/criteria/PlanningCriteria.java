package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Planning} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.PlanningResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /plannings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlanningCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter orderCreationDate;

    private IntegerFilter status;

    private StringFilter code;

    private LongFilter quantificationId;

    private Boolean distinct;

    public PlanningCriteria() {}

    public PlanningCriteria(PlanningCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.orderCreationDate = other.optionalOrderCreationDate().map(InstantFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(IntegerFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.quantificationId = other.optionalQuantificationId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PlanningCriteria copy() {
        return new PlanningCriteria(this);
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

    public IntegerFilter getStatus() {
        return status;
    }

    public Optional<IntegerFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public IntegerFilter status() {
        if (status == null) {
            setStatus(new IntegerFilter());
        }
        return status;
    }

    public void setStatus(IntegerFilter status) {
        this.status = status;
    }

    public StringFilter getCode() {
        return code;
    }

    public Optional<StringFilter> optionalCode() {
        return Optional.ofNullable(code);
    }

    public StringFilter code() {
        if (code == null) {
            setCode(new StringFilter());
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public LongFilter getQuantificationId() {
        return quantificationId;
    }

    public Optional<LongFilter> optionalQuantificationId() {
        return Optional.ofNullable(quantificationId);
    }

    public LongFilter quantificationId() {
        if (quantificationId == null) {
            setQuantificationId(new LongFilter());
        }
        return quantificationId;
    }

    public void setQuantificationId(LongFilter quantificationId) {
        this.quantificationId = quantificationId;
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
        final PlanningCriteria that = (PlanningCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(orderCreationDate, that.orderCreationDate) &&
            Objects.equals(status, that.status) &&
            Objects.equals(code, that.code) &&
            Objects.equals(quantificationId, that.quantificationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderCreationDate, status, code, quantificationId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlanningCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalOrderCreationDate().map(f -> "orderCreationDate=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalQuantificationId().map(f -> "quantificationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
