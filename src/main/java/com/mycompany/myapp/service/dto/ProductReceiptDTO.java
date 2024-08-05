package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.ProductReceipt} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductReceiptDTO implements Serializable {

    private Long id;

    private Instant creationDate;

    private Instant paymentDate;

    private Instant receiptDate;

    private Integer status;

    private String code;

    private UserDTO created;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Instant paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Instant getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Instant receiptDate) {
        this.receiptDate = receiptDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public UserDTO getCreated() {
        return created;
    }

    public void setCreated(UserDTO created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductReceiptDTO)) {
            return false;
        }

        ProductReceiptDTO productReceiptDTO = (ProductReceiptDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productReceiptDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductReceiptDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", receiptDate='" + getReceiptDate() + "'" +
            ", status=" + getStatus() +
            ", code='" + getCode() + "'" +
            ", created=" + getCreated() +
            "}";
    }
}
