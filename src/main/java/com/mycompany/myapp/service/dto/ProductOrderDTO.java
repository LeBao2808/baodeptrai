package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.ProductOrder} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductOrderDTO implements Serializable {

    private Long id;

    private String paymentMethod;

    private String note;

    private Integer status;

    private Instant orderDate;

    private Instant paymentDate;

    private Instant warehouseReleaseDate;

    private String code;

    private PlanningDTO quantificationOrder;

    private CustomerDTO customer;

    private UserDTO createdByUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Instant getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public Instant getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Instant paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Instant getWarehouseReleaseDate() {
        return warehouseReleaseDate;
    }

    public void setWarehouseReleaseDate(Instant warehouseReleaseDate) {
        this.warehouseReleaseDate = warehouseReleaseDate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public PlanningDTO getQuantificationOrder() {
        return quantificationOrder;
    }

    public void setQuantificationOrder(PlanningDTO quantificationOrder) {
        this.quantificationOrder = quantificationOrder;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public UserDTO getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(UserDTO createdByUser) {
        this.createdByUser = createdByUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductOrderDTO)) {
            return false;
        }

        ProductOrderDTO productOrderDTO = (ProductOrderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productOrderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductOrderDTO{" +
            "id=" + getId() +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", note='" + getNote() + "'" +
            ", status=" + getStatus() +
            ", orderDate='" + getOrderDate() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", warehouseReleaseDate='" + getWarehouseReleaseDate() + "'" +
            ", code='" + getCode() + "'" +
            ", quantificationOrder=" + getQuantificationOrder() +
            ", customer=" + getCustomer() +
            ", createdByUser=" + getCreatedByUser() +
            "}";
    }
}
