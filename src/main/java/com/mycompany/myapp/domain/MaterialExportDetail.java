package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A MaterialExportDetail.
 */
@Entity
@Table(name = "material_export_detail")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaterialExportDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "note")
    private String note;

    @Column(name = "export_price", precision = 21, scale = 2)
    private BigDecimal exportPrice;

    @Column(name = "quantity")
    private Float quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "createdByUser", "productionSite" }, allowSetters = true)
    private MaterialExport materialExport;

    @ManyToOne(fetch = FetchType.LAZY)
    private Material material;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MaterialExportDetail id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNote() {
        return this.note;
    }

    public MaterialExportDetail note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BigDecimal getExportPrice() {
        return this.exportPrice;
    }

    public MaterialExportDetail exportPrice(BigDecimal exportPrice) {
        this.setExportPrice(exportPrice);
        return this;
    }

    public void setExportPrice(BigDecimal exportPrice) {
        this.exportPrice = exportPrice;
    }

    public Float getQuantity() {
        return this.quantity;
    }

    public MaterialExportDetail quantity(Float quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public MaterialExport getMaterialExport() {
        return this.materialExport;
    }

    public void setMaterialExport(MaterialExport materialExport) {
        this.materialExport = materialExport;
    }

    public MaterialExportDetail materialExport(MaterialExport materialExport) {
        this.setMaterialExport(materialExport);
        return this;
    }

    public Material getMaterial() {
        return this.material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public MaterialExportDetail material(Material material) {
        this.setMaterial(material);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MaterialExportDetail)) {
            return false;
        }
        return getId() != null && getId().equals(((MaterialExportDetail) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaterialExportDetail{" +
            "id=" + getId() +
            ", note='" + getNote() + "'" +
            ", exportPrice=" + getExportPrice() +
            ", quantity=" + getQuantity() +
            "}";
    }
}
