package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A ProductOrder.
 */
@Entity
@Table(name = "product_order")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "note")
    private String note;

    @Column(name = "status")
    private Integer status;

    @Column(name = "order_date")
    private Instant orderDate;

    @Column(name = "payment_date")
    private Instant paymentDate;

    @Column(name = "warehouse_release_date")
    private Instant warehouseReleaseDate;

    @Column(name = "code")
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "quantification" }, allowSetters = true)
    private Planning quantificationOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    private User createdByUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductOrder id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentMethod() {
        return this.paymentMethod;
    }

    public ProductOrder paymentMethod(String paymentMethod) {
        this.setPaymentMethod(paymentMethod);
        return this;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getNote() {
        return this.note;
    }

    public ProductOrder note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getStatus() {
        return this.status;
    }

    public ProductOrder status(Integer status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Instant getOrderDate() {
        return this.orderDate;
    }

    public ProductOrder orderDate(Instant orderDate) {
        this.setOrderDate(orderDate);
        return this;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public Instant getPaymentDate() {
        return this.paymentDate;
    }

    public ProductOrder paymentDate(Instant paymentDate) {
        this.setPaymentDate(paymentDate);
        return this;
    }

    public void setPaymentDate(Instant paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Instant getWarehouseReleaseDate() {
        return this.warehouseReleaseDate;
    }

    public ProductOrder warehouseReleaseDate(Instant warehouseReleaseDate) {
        this.setWarehouseReleaseDate(warehouseReleaseDate);
        return this;
    }

    public void setWarehouseReleaseDate(Instant warehouseReleaseDate) {
        this.warehouseReleaseDate = warehouseReleaseDate;
    }

    public String getCode() {
        return this.code;
    }

    public ProductOrder code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Planning getQuantificationOrder() {
        return this.quantificationOrder;
    }

    public void setQuantificationOrder(Planning planning) {
        this.quantificationOrder = planning;
    }

    public ProductOrder quantificationOrder(Planning planning) {
        this.setQuantificationOrder(planning);
        return this;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ProductOrder customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    public User getCreatedByUser() {
        return this.createdByUser;
    }

    public void setCreatedByUser(User user) {
        this.createdByUser = user;
    }

    public ProductOrder createdByUser(User user) {
        this.setCreatedByUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductOrder)) {
            return false;
        }
        return getId() != null && getId().equals(((ProductOrder) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductOrder{" +
            "id=" + getId() +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", note='" + getNote() + "'" +
            ", status=" + getStatus() +
            ", orderDate='" + getOrderDate() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", warehouseReleaseDate='" + getWarehouseReleaseDate() + "'" +
            ", code='" + getCode() + "'" +
            "}";
    }
}
