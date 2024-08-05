package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.MaterialExport} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaterialExportDTO implements Serializable {

    private Long id;

    private Instant creationDate;

    private Instant exportDate;

    private Integer status;

    private String code;

    private UserDTO createdByUser;

    private ProductionSiteDTO productionSite;

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

    public Instant getExportDate() {
        return exportDate;
    }

    public void setExportDate(Instant exportDate) {
        this.exportDate = exportDate;
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

    public UserDTO getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(UserDTO createdByUser) {
        this.createdByUser = createdByUser;
    }

    public ProductionSiteDTO getProductionSite() {
        return productionSite;
    }

    public void setProductionSite(ProductionSiteDTO productionSite) {
        this.productionSite = productionSite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MaterialExportDTO)) {
            return false;
        }

        MaterialExportDTO materialExportDTO = (MaterialExportDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, materialExportDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaterialExportDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", exportDate='" + getExportDate() + "'" +
            ", status=" + getStatus() +
            ", code='" + getCode() + "'" +
            ", createdByUser=" + getCreatedByUser() +
            ", productionSite=" + getProductionSite() +
            "}";
    }
}
