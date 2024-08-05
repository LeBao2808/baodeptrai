package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.OfferDetail} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OfferDetailDTO implements Serializable {

    private Long id;

    private String feedback;

    private ProductDTO product;

    private OfferDTO offer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public OfferDTO getOffer() {
        return offer;
    }

    public void setOffer(OfferDTO offer) {
        this.offer = offer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OfferDetailDTO)) {
            return false;
        }

        OfferDetailDTO offerDetailDTO = (OfferDetailDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, offerDetailDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OfferDetailDTO{" +
            "id=" + getId() +
            ", feedback='" + getFeedback() + "'" +
            ", product=" + getProduct() +
            ", offer=" + getOffer() +
            "}";
    }
}
