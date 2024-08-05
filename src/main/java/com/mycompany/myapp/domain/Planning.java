package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Planning.
 */
@Entity
@Table(name = "planning")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Planning implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "order_creation_date")
    private Instant orderCreationDate;

    @Column(name = "status")
    private Integer status;

    @Column(name = "code")
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "product", "material" }, allowSetters = true)
    private Quantification quantification;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Planning id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getOrderCreationDate() {
        return this.orderCreationDate;
    }

    public Planning orderCreationDate(Instant orderCreationDate) {
        this.setOrderCreationDate(orderCreationDate);
        return this;
    }

    public void setOrderCreationDate(Instant orderCreationDate) {
        this.orderCreationDate = orderCreationDate;
    }

    public Integer getStatus() {
        return this.status;
    }

    public Planning status(Integer status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCode() {
        return this.code;
    }

    public Planning code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Quantification getQuantification() {
        return this.quantification;
    }

    public void setQuantification(Quantification quantification) {
        this.quantification = quantification;
    }

    public Planning quantification(Quantification quantification) {
        this.setQuantification(quantification);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Planning)) {
            return false;
        }
        return getId() != null && getId().equals(((Planning) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Planning{" +
            "id=" + getId() +
            ", orderCreationDate='" + getOrderCreationDate() + "'" +
            ", status=" + getStatus() +
            ", code='" + getCode() + "'" +
            "}";
    }
}
