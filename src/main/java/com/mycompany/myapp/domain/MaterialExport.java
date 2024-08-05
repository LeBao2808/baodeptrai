package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A MaterialExport.
 */
@Entity
@Table(name = "material_export")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaterialExport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "export_date")
    private Instant exportDate;

    @Column(name = "status")
    private Integer status;

    @Column(name = "code")
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    private User createdByUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "productId" }, allowSetters = true)
    private ProductionSite productionSite;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MaterialExport id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public MaterialExport creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getExportDate() {
        return this.exportDate;
    }

    public MaterialExport exportDate(Instant exportDate) {
        this.setExportDate(exportDate);
        return this;
    }

    public void setExportDate(Instant exportDate) {
        this.exportDate = exportDate;
    }

    public Integer getStatus() {
        return this.status;
    }

    public MaterialExport status(Integer status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCode() {
        return this.code;
    }

    public MaterialExport code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public User getCreatedByUser() {
        return this.createdByUser;
    }

    public void setCreatedByUser(User user) {
        this.createdByUser = user;
    }

    public MaterialExport createdByUser(User user) {
        this.setCreatedByUser(user);
        return this;
    }

    public ProductionSite getProductionSite() {
        return this.productionSite;
    }

    public void setProductionSite(ProductionSite productionSite) {
        this.productionSite = productionSite;
    }

    public MaterialExport productionSite(ProductionSite productionSite) {
        this.setProductionSite(productionSite);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MaterialExport)) {
            return false;
        }
        return getId() != null && getId().equals(((MaterialExport) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaterialExport{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", exportDate='" + getExportDate() + "'" +
            ", status=" + getStatus() +
            ", code='" + getCode() + "'" +
            "}";
    }
}
