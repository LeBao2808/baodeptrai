package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.MaterialReceiptDetail} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.MaterialReceiptDetailResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /material-receipt-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaterialReceiptDetailCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter note;

    private BigDecimalFilter importPrice;

    private FloatFilter quantity;

    private LongFilter materialId;

    private LongFilter receiptId;

    private Boolean distinct;

    public MaterialReceiptDetailCriteria() {}

    public MaterialReceiptDetailCriteria(MaterialReceiptDetailCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.note = other.optionalNote().map(StringFilter::copy).orElse(null);
        this.importPrice = other.optionalImportPrice().map(BigDecimalFilter::copy).orElse(null);
        this.quantity = other.optionalQuantity().map(FloatFilter::copy).orElse(null);
        this.materialId = other.optionalMaterialId().map(LongFilter::copy).orElse(null);
        this.receiptId = other.optionalReceiptId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MaterialReceiptDetailCriteria copy() {
        return new MaterialReceiptDetailCriteria(this);
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

    public StringFilter getNote() {
        return note;
    }

    public Optional<StringFilter> optionalNote() {
        return Optional.ofNullable(note);
    }

    public StringFilter note() {
        if (note == null) {
            setNote(new StringFilter());
        }
        return note;
    }

    public void setNote(StringFilter note) {
        this.note = note;
    }

    public BigDecimalFilter getImportPrice() {
        return importPrice;
    }

    public Optional<BigDecimalFilter> optionalImportPrice() {
        return Optional.ofNullable(importPrice);
    }

    public BigDecimalFilter importPrice() {
        if (importPrice == null) {
            setImportPrice(new BigDecimalFilter());
        }
        return importPrice;
    }

    public void setImportPrice(BigDecimalFilter importPrice) {
        this.importPrice = importPrice;
    }

    public FloatFilter getQuantity() {
        return quantity;
    }

    public Optional<FloatFilter> optionalQuantity() {
        return Optional.ofNullable(quantity);
    }

    public FloatFilter quantity() {
        if (quantity == null) {
            setQuantity(new FloatFilter());
        }
        return quantity;
    }

    public void setQuantity(FloatFilter quantity) {
        this.quantity = quantity;
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

    public LongFilter getReceiptId() {
        return receiptId;
    }

    public Optional<LongFilter> optionalReceiptId() {
        return Optional.ofNullable(receiptId);
    }

    public LongFilter receiptId() {
        if (receiptId == null) {
            setReceiptId(new LongFilter());
        }
        return receiptId;
    }

    public void setReceiptId(LongFilter receiptId) {
        this.receiptId = receiptId;
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
        final MaterialReceiptDetailCriteria that = (MaterialReceiptDetailCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(note, that.note) &&
            Objects.equals(importPrice, that.importPrice) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(materialId, that.materialId) &&
            Objects.equals(receiptId, that.receiptId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, note, importPrice, quantity, materialId, receiptId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaterialReceiptDetailCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNote().map(f -> "note=" + f + ", ").orElse("") +
            optionalImportPrice().map(f -> "importPrice=" + f + ", ").orElse("") +
            optionalQuantity().map(f -> "quantity=" + f + ", ").orElse("") +
            optionalMaterialId().map(f -> "materialId=" + f + ", ").orElse("") +
            optionalReceiptId().map(f -> "receiptId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
