package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.ProductionSite} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductionSiteDTO implements Serializable {

    private Long id;

    private String name;

    private String address;

    private String phone;

    private ProductDTO productId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ProductDTO getProductId() {
        return productId;
    }

    public void setProductId(ProductDTO productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductionSiteDTO)) {
            return false;
        }

        ProductionSiteDTO productionSiteDTO = (ProductionSiteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productionSiteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductionSiteDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            ", phone='" + getPhone() + "'" +
            ", productId=" + getProductId() +
            "}";
    }
}
