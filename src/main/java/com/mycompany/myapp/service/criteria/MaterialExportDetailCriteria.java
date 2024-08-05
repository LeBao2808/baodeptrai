package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.MaterialExportDetail} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.MaterialExportDetailResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /material-export-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaterialExportDetailCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter note;

    private BigDecimalFilter exportPrice;

    private FloatFilter quantity;

    private LongFilter materialExportId;

    private LongFilter materialId;

    private Boolean distinct;

    public MaterialExportDetailCriteria() {}

    public MaterialExportDetailCriteria(MaterialExportDetailCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.note = other.optionalNote().map(StringFilter::copy).orElse(null);
        this.exportPrice = other.optionalExportPrice().map(BigDecimalFilter::copy).orElse(null);
        this.quantity = other.optionalQuantity().map(FloatFilter::copy).orElse(null);
        this.materialExportId = other.optionalMaterialExportId().map(LongFilter::copy).orElse(null);
        this.materialId = other.optionalMaterialId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MaterialExportDetailCriteria copy() {
        return new MaterialExportDetailCriteria(this);
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

    public BigDecimalFilter getExportPrice() {
        return exportPrice;
    }

    public Optional<BigDecimalFilter> optionalExportPrice() {
        return Optional.ofNullable(exportPrice);
    }

    public BigDecimalFilter exportPrice() {
        if (exportPrice == null) {
            setExportPrice(new BigDecimalFilter());
        }
        return exportPrice;
    }

    public void setExportPrice(BigDecimalFilter exportPrice) {
        this.exportPrice = exportPrice;
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

    public LongFilter getMaterialExportId() {
        return materialExportId;
    }

    public Optional<LongFilter> optionalMaterialExportId() {
        return Optional.ofNullable(materialExportId);
    }

    public LongFilter materialExportId() {
        if (materialExportId == null) {
            setMaterialExportId(new LongFilter());
        }
        return materialExportId;
    }

    public void setMaterialExportId(LongFilter materialExportId) {
        this.materialExportId = materialExportId;
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
        final MaterialExportDetailCriteria that = (MaterialExportDetailCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(note, that.note) &&
            Objects.equals(exportPrice, that.exportPrice) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(materialExportId, that.materialExportId) &&
            Objects.equals(materialId, that.materialId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, note, exportPrice, quantity, materialExportId, materialId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaterialExportDetailCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNote().map(f -> "note=" + f + ", ").orElse("") +
            optionalExportPrice().map(f -> "exportPrice=" + f + ", ").orElse("") +
            optionalQuantity().map(f -> "quantity=" + f + ", ").orElse("") +
            optionalMaterialExportId().map(f -> "materialExportId=" + f + ", ").orElse("") +
            optionalMaterialId().map(f -> "materialId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
