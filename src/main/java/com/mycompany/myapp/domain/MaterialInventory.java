package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A MaterialInventory.
 */
@Entity
@Table(name = "material_inventory")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaterialInventory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "quantity_on_hand")
    private Float quantityOnHand;

    @Column(name = "inventory_month")
    private Integer inventoryMonth;

    @Column(name = "inventory_year")
    private Integer inventoryYear;

    @Column(name = "type")
    private Integer type;

    @Column(name = "price", precision = 21, scale = 2)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    private Material material;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MaterialInventory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getQuantityOnHand() {
        return this.quantityOnHand;
    }

    public MaterialInventory quantityOnHand(Float quantityOnHand) {
        this.setQuantityOnHand(quantityOnHand);
        return this;
    }

    public void setQuantityOnHand(Float quantityOnHand) {
        this.quantityOnHand = quantityOnHand;
    }

    public Integer getInventoryMonth() {
        return this.inventoryMonth;
    }

    public MaterialInventory inventoryMonth(Integer inventoryMonth) {
        this.setInventoryMonth(inventoryMonth);
        return this;
    }

    public void setInventoryMonth(Integer inventoryMonth) {
        this.inventoryMonth = inventoryMonth;
    }

    public Integer getInventoryYear() {
        return this.inventoryYear;
    }

    public MaterialInventory inventoryYear(Integer inventoryYear) {
        this.setInventoryYear(inventoryYear);
        return this;
    }

    public void setInventoryYear(Integer inventoryYear) {
        this.inventoryYear = inventoryYear;
    }

    public Integer getType() {
        return this.type;
    }

    public MaterialInventory type(Integer type) {
        this.setType(type);
        return this;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public MaterialInventory price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Material getMaterial() {
        return this.material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public MaterialInventory material(Material material) {
        this.setMaterial(material);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MaterialInventory)) {
            return false;
        }
        return getId() != null && getId().equals(((MaterialInventory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaterialInventory{" +
            "id=" + getId() +
            ", quantityOnHand=" + getQuantityOnHand() +
            ", inventoryMonth=" + getInventoryMonth() +
            ", inventoryYear=" + getInventoryYear() +
            ", type=" + getType() +
            ", price=" + getPrice() +
            "}";
    }
}
