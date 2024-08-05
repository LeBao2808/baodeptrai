package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Planning} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlanningDTO implements Serializable {

    private Long id;

    private Instant orderCreationDate;

    private Integer status;

    private String code;

    private QuantificationDTO quantification;

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

    public QuantificationDTO getQuantification() {
        return quantification;
    }

    public void setQuantification(QuantificationDTO quantification) {
        this.quantification = quantification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlanningDTO)) {
            return false;
        }

        PlanningDTO planningDTO = (PlanningDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, planningDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlanningDTO{" +
            "id=" + getId() +
            ", orderCreationDate='" + getOrderCreationDate() + "'" +
            ", status=" + getStatus() +
            ", code='" + getCode() + "'" +
            ", quantification=" + getQuantification() +
            "}";
    }
}
