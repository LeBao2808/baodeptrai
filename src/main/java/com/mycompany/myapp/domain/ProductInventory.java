package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A ProductInventory.
 */
@Entity
@Table(name = "product_inventory")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductInventory implements Serializable {

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
    @JsonIgnoreProperties(value = { "setIdProduct", "parentProduct", "material" }, allowSetters = true)
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductInventory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getQuantityOnHand() {
        return this.quantityOnHand;
    }

    public ProductInventory quantityOnHand(Float quantityOnHand) {
        this.setQuantityOnHand(quantityOnHand);
        return this;
    }

    public void setQuantityOnHand(Float quantityOnHand) {
        this.quantityOnHand = quantityOnHand;
    }

    public Integer getInventoryMonth() {
        return this.inventoryMonth;
    }

    public ProductInventory inventoryMonth(Integer inventoryMonth) {
        this.setInventoryMonth(inventoryMonth);
        return this;
    }

    public void setInventoryMonth(Integer inventoryMonth) {
        this.inventoryMonth = inventoryMonth;
    }

    public Integer getInventoryYear() {
        return this.inventoryYear;
    }

    public ProductInventory inventoryYear(Integer inventoryYear) {
        this.setInventoryYear(inventoryYear);
        return this;
    }

    public void setInventoryYear(Integer inventoryYear) {
        this.inventoryYear = inventoryYear;
    }

    public Integer getType() {
        return this.type;
    }

    public ProductInventory type(Integer type) {
        this.setType(type);
        return this;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public ProductInventory price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductInventory product(Product product) {
        this.setProduct(product);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductInventory)) {
            return false;
        }
        return getId() != null && getId().equals(((ProductInventory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductInventory{" +
            "id=" + getId() +
            ", quantityOnHand=" + getQuantityOnHand() +
            ", inventoryMonth=" + getInventoryMonth() +
            ", inventoryYear=" + getInventoryYear() +
            ", type=" + getType() +
            ", price=" + getPrice() +
            "}";
    }
}
