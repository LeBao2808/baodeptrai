package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.ProductOrderDetail} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductOrderDetailDTO implements Serializable {

    private Long id;

    private Instant orderCreationDate;

    private Integer quantity;

    private Float unitPrice;

    private ProductOrderDTO order;

    private ProductDTO product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getOrderCreationDate() {
        return orderCreationDate;
    }

    public void setOrderCreationDate(Instant orderCreationDate) {
        this.orderCreationDate = orderCreationDate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public ProductOrderDTO getOrder() {
        return order;
    }

    public void setOrder(ProductOrderDTO order) {
        this.order = order;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductOrderDetailDTO)) {
            return false;
        }

        ProductOrderDetailDTO productOrderDetailDTO = (ProductOrderDetailDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productOrderDetailDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductOrderDetailDTO{" +
            "id=" + getId() +
            ", orderCreationDate='" + getOrderCreationDate() + "'" +
            ", quantity=" + getQuantity() +
            ", unitPrice=" + getUnitPrice() +
            ", order=" + getOrder() +
            ", product=" + getProduct() +
            "}";
    }
}
