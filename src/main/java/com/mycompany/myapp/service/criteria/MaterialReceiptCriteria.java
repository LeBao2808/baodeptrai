package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.MaterialReceipt} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.MaterialReceiptResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /material-receipts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaterialReceiptCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter creationDate;

    private InstantFilter paymentDate;

    private InstantFilter receiptDate;

    private IntegerFilter status;

    private StringFilter code;

    private LongFilter createdByUserId;

    private LongFilter quantificationOrderId;

    private Boolean distinct;

    public MaterialReceiptCriteria() {}

    public MaterialReceiptCriteria(MaterialReceiptCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.creationDate = other.optionalCreationDate().map(InstantFilter::copy).orElse(null);
        this.paymentDate = other.optionalPaymentDate().map(InstantFilter::copy).orElse(null);
        this.receiptDate = other.optionalReceiptDate().map(InstantFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(IntegerFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.createdByUserId = other.optionalCreatedByUserId().map(LongFilter::copy).orElse(null);
        this.quantificationOrderId = other.optionalQuantificationOrderId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MaterialReceiptCriteria copy() {
        return new MaterialReceiptCriteria(this);
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

    public InstantFilter getCreationDate() {
        return creationDate;
    }

    public Optional<InstantFilter> optionalCreationDate() {
        return Optional.ofNullable(creationDate);
    }

    public InstantFilter creationDate() {
        if (creationDate == null) {
            setCreationDate(new InstantFilter());
        }
        return creationDate;
    }

    public void setCreationDate(InstantFilter creationDate) {
        this.creationDate = creationDate;
    }

    public InstantFilter getPaymentDate() {
        return paymentDate;
    }

    public Optional<InstantFilter> optionalPaymentDate() {
        return Optional.ofNullable(paymentDate);
    }

    public InstantFilter paymentDate() {
        if (paymentDate == null) {
            setPaymentDate(new InstantFilter());
        }
        return paymentDate;
    }

    public void setPaymentDate(InstantFilter paymentDate) {
        this.paymentDate = paymentDate;
    }

    public InstantFilter getReceiptDate() {
        return receiptDate;
    }

    public Optional<InstantFilter> optionalReceiptDate() {
        return Optional.ofNullable(receiptDate);
    }

    public InstantFilter receiptDate() {
        if (receiptDate == null) {
            setReceiptDate(new InstantFilter());
        }
        return receiptDate;
    }

    public void setReceiptDate(InstantFilter receiptDate) {
        this.receiptDate = receiptDate;
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

    public LongFilter getCreatedByUserId() {
        return createdByUserId;
    }

    public Optional<LongFilter> optionalCreatedByUserId() {
        return Optional.ofNullable(createdByUserId);
    }

    public LongFilter createdByUserId() {
        if (createdByUserId == null) {
            setCreatedByUserId(new LongFilter());
        }
        return createdByUserId;
    }

    public void setCreatedByUserId(LongFilter createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public LongFilter getQuantificationOrderId() {
        return quantificationOrderId;
    }

    public Optional<LongFilter> optionalQuantificationOrderId() {
        return Optional.ofNullable(quantificationOrderId);
    }

    public LongFilter quantificationOrderId() {
        if (quantificationOrderId == null) {
            setQuantificationOrderId(new LongFilter());
        }
        return quantificationOrderId;
    }

    public void setQuantificationOrderId(LongFilter quantificationOrderId) {
        this.quantificationOrderId = quantificationOrderId;
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
        final MaterialReceiptCriteria that = (MaterialReceiptCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(paymentDate, that.paymentDate) &&
            Objects.equals(receiptDate, that.receiptDate) &&
            Objects.equals(status, that.status) &&
            Objects.equals(code, that.code) &&
            Objects.equals(createdByUserId, that.createdByUserId) &&
            Objects.equals(quantificationOrderId, that.quantificationOrderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDate, paymentDate, receiptDate, status, code, createdByUserId, quantificationOrderId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaterialReceiptCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCreationDate().map(f -> "creationDate=" + f + ", ").orElse("") +
            optionalPaymentDate().map(f -> "paymentDate=" + f + ", ").orElse("") +
            optionalReceiptDate().map(f -> "receiptDate=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalCreatedByUserId().map(f -> "createdByUserId=" + f + ", ").orElse("") +
            optionalQuantificationOrderId().map(f -> "quantificationOrderId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
