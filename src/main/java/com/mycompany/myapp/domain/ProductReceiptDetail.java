package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A ProductReceiptDetail.
 */
@Entity
@Table(name = "product_receipt_detail")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductReceiptDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "note")
    private String note;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "setIdProduct", "parentProduct", "material" }, allowSetters = true)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "created" }, allowSetters = true)
    private ProductReceipt receipt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductReceiptDetail id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNote() {
        return this.note;
    }

    public ProductReceiptDetail note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public ProductReceiptDetail quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductReceiptDetail product(Product product) {
        this.setProduct(product);
        return this;
    }

    public ProductReceipt getReceipt() {
        return this.receipt;
    }

    public void setReceipt(ProductReceipt productReceipt) {
        this.receipt = productReceipt;
    }

    public ProductReceiptDetail receipt(ProductReceipt productReceipt) {
        this.setReceipt(productReceipt);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductReceiptDetail)) {
            return false;
        }
        return getId() != null && getId().equals(((ProductReceiptDetail) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductReceiptDetail{" +
            "id=" + getId() +
            ", note='" + getNote() + "'" +
            ", quantity=" + getQuantity() +
            "}";
    }
}
