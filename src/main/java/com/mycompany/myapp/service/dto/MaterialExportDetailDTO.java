package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.MaterialExportDetail} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaterialExportDetailDTO implements Serializable {

    private Long id;

    private String note;

    private BigDecimal exportPrice;

    private Float quantity;

    private MaterialExportDTO materialExport;

    private MaterialDTO material;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BigDecimal getExportPrice() {
        return exportPrice;
    }

    public void setExportPrice(BigDecimal exportPrice) {
        this.exportPrice = exportPrice;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public MaterialExportDTO getMaterialExport() {
        return materialExport;
    }

    public void setMaterialExport(MaterialExportDTO materialExport) {
        this.materialExport = materialExport;
    }

    public MaterialDTO getMaterial() {
        return material;
    }

    public void setMaterial(MaterialDTO material) {
        this.material = material;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MaterialExportDetailDTO)) {
            return false;
        }

        MaterialExportDetailDTO materialExportDetailDTO = (MaterialExportDetailDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, materialExportDetailDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaterialExportDetailDTO{" +
            "id=" + getId() +
            ", note='" + getNote() + "'" +
            ", exportPrice=" + getExportPrice() +
            ", quantity=" + getQuantity() +
            ", materialExport=" + getMaterialExport() +
            ", material=" + getMaterial() +
            "}";
    }
}
