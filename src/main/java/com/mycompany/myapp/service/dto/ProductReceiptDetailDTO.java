package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.ProductReceiptDetail} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductReceiptDetailDTO implements Serializable {

    private Long id;

    private String note;

    private Integer quantity;

    private ProductDTO product;

    private ProductReceiptDTO receipt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public ProductReceiptDTO getReceipt() {
        return receipt;
    }

    public void setReceipt(ProductReceiptDTO receipt) {
        this.receipt = receipt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductReceiptDetailDTO)) {
            return false;
        }

        ProductReceiptDetailDTO productReceiptDetailDTO = (ProductReceiptDetailDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productReceiptDetailDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductReceiptDetailDTO{" +
            "id=" + getId() +
            ", note='" + getNote() + "'" +
            ", quantity=" + getQuantity() +
            ", product=" + getProduct() +
            ", receipt=" + getReceipt() +
            "}";
    }
}
