package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A ProductReceipt.
 */
@Entity
@Table(name = "product_receipt")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductReceipt implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "payment_date")
    private Instant paymentDate;

    @Column(name = "receipt_date")
    private Instant receiptDate;

    @Column(name = "status")
    private Integer status;

    @Column(name = "code")
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    private User created;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductReceipt id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public ProductReceipt creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getPaymentDate() {
        return this.paymentDate;
    }

    public ProductReceipt paymentDate(Instant paymentDate) {
        this.setPaymentDate(paymentDate);
        return this;
    }

    public void setPaymentDate(Instant paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Instant getReceiptDate() {
        return this.receiptDate;
    }

    public ProductReceipt receiptDate(Instant receiptDate) {
        this.setReceiptDate(receiptDate);
        return this;
    }

    public void setReceiptDate(Instant receiptDate) {
        this.receiptDate = receiptDate;
    }

    public Integer getStatus() {
        return this.status;
    }

    public ProductReceipt status(Integer status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCode() {
        return this.code;
    }

    public ProductReceipt code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public User getCreated() {
        return this.created;
    }

    public void setCreated(User user) {
        this.created = user;
    }

    public ProductReceipt created(User user) {
        this.setCreated(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductReceipt)) {
            return false;
        }
        return getId() != null && getId().equals(((ProductReceipt) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductReceipt{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", receiptDate='" + getReceiptDate() + "'" +
            ", status=" + getStatus() +
            ", code='" + getCode() + "'" +
            "}";
    }
}
