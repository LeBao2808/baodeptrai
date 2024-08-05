package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.MaterialReceiptDetail} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaterialReceiptDetailDTO implements Serializable {

    private Long id;

    private String note;

    private BigDecimal importPrice;

    private Float quantity;

    private MaterialDTO material;

    private MaterialReceiptDTO receipt;

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

    public BigDecimal getImportPrice() {
        return importPrice;
    }

    public void setImportPrice(BigDecimal importPrice) {
        this.importPrice = importPrice;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public MaterialDTO getMaterial() {
        return material;
    }

    public void setMaterial(MaterialDTO material) {
        this.material = material;
    }

    public MaterialReceiptDTO getReceipt() {
        return receipt;
    }

    public void setReceipt(MaterialReceiptDTO receipt) {
        this.receipt = receipt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MaterialReceiptDetailDTO)) {
            return false;
        }

        MaterialReceiptDetailDTO materialReceiptDetailDTO = (MaterialReceiptDetailDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, materialReceiptDetailDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaterialReceiptDetailDTO{" +
            "id=" + getId() +
            ", note='" + getNote() + "'" +
            ", importPrice=" + getImportPrice() +
            ", quantity=" + getQuantity() +
            ", material=" + getMaterial() +
            ", receipt=" + getReceipt() +
            "}";
    }
}
