package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A MaterialReceiptDetail.
 */
@Entity
@Table(name = "material_receipt_detail")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaterialReceiptDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "note")
    private String note;

    @Column(name = "import_price", precision = 21, scale = 2)
    private BigDecimal importPrice;

    @Column(name = "quantity")
    private Float quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    private Material material;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "createdByUser", "quantificationOrder" }, allowSetters = true)
    private MaterialReceipt receipt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MaterialReceiptDetail id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNote() {
        return this.note;
    }

    public MaterialReceiptDetail note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BigDecimal getImportPrice() {
        return this.importPrice;
    }

    public MaterialReceiptDetail importPrice(BigDecimal importPrice) {
        this.setImportPrice(importPrice);
        return this;
    }

    public void setImportPrice(BigDecimal importPrice) {
        this.importPrice = importPrice;
    }

    public Float getQuantity() {
        return this.quantity;
    }

    public MaterialReceiptDetail quantity(Float quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public Material getMaterial() {
        return this.material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public MaterialReceiptDetail material(Material material) {
        this.setMaterial(material);
        return this;
    }

    public MaterialReceipt getReceipt() {
        return this.receipt;
    }

    public void setReceipt(MaterialReceipt materialReceipt) {
        this.receipt = materialReceipt;
    }

    public MaterialReceiptDetail receipt(MaterialReceipt materialReceipt) {
        this.setReceipt(materialReceipt);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MaterialReceiptDetail)) {
            return false;
        }
        return getId() != null && getId().equals(((MaterialReceiptDetail) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaterialReceiptDetail{" +
            "id=" + getId() +
            ", note='" + getNote() + "'" +
            ", importPrice=" + getImportPrice() +
            ", quantity=" + getQuantity() +
            "}";
    }
}
