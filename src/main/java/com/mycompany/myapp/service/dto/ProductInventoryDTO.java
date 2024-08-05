package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.ProductInventory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductInventoryDTO implements Serializable {

    private Long id;

    private Float quantityOnHand;

    private Integer inventoryMonth;

    private Integer inventoryYear;

    private Integer type;

    private BigDecimal price;

    private ProductDTO product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getQuantityOnHand() {
        return quantityOnHand;
    }

    public void setQuantityOnHand(Float quantityOnHand) {
        this.quantityOnHand = quantityOnHand;
    }

    public Integer getInventoryMonth() {
        return inventoryMonth;
    }

    public void setInventoryMonth(Integer inventoryMonth) {
        this.inventoryMonth = inventoryMonth;
    }

    public Integer getInventoryYear() {
        return inventoryYear;
    }

    public void setInventoryYear(Integer inventoryYear) {
        this.inventoryYear = inventoryYear;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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
        if (!(o instanceof ProductInventoryDTO)) {
            return false;
        }

        ProductInventoryDTO productInventoryDTO = (ProductInventoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productInventoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductInventoryDTO{" +
            "id=" + getId() +
            ", quantityOnHand=" + getQuantityOnHand() +
            ", inventoryMonth=" + getInventoryMonth() +
            ", inventoryYear=" + getInventoryYear() +
            ", type=" + getType() +
            ", price=" + getPrice() +
            ", product=" + getProduct() +
            "}";
    }
}
