package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.MaterialExport;
import com.mycompany.myapp.repository.MaterialExportRepository;
import com.mycompany.myapp.service.criteria.MaterialExportCriteria;
import com.mycompany.myapp.service.dto.MaterialExportDTO;
import com.mycompany.myapp.service.mapper.MaterialExportMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link MaterialExport} entities in the database.
 * The main input is a {@link MaterialExportCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MaterialExportDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MaterialExportQueryService extends QueryService<MaterialExport> {

    private static final Logger log = LoggerFactory.getLogger(MaterialExportQueryService.class);

    private final MaterialExportRepository materialExportRepository;

    private final MaterialExportMapper materialExportMapper;

    public MaterialExportQueryService(MaterialExportRepository materialExportRepository, MaterialExportMapper materialExportMapper) {
        this.materialExportRepository = materialExportRepository;
        this.materialExportMapper = materialExportMapper;
    }

    /**
     * Return a {@link Page} of {@link MaterialExportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MaterialExportDTO> findByCriteria(MaterialExportCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MaterialExport> specification = createSpecification(criteria);
        return materialExportRepository.findAll(specification, page).map(materialExportMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MaterialExportCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MaterialExport> specification = createSpecification(criteria);
        return materialExportRepository.count(specification);
    }

    /**
     * Function to convert {@link MaterialExportCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MaterialExport> createSpecification(MaterialExportCriteria criteria) {
        Specification<MaterialExport> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MaterialExport_.id));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), MaterialExport_.creationDate));
            }
            if (criteria.getExportDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExportDate(), MaterialExport_.exportDate));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStatus(), MaterialExport_.status));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), MaterialExport_.code));
            }
            if (criteria.getCreatedByUserId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getCreatedByUserId(),
                        root -> root.join(MaterialExport_.createdByUser, JoinType.LEFT).get(User_.id)
                    )
                );
            }
            if (criteria.getProductionSiteId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getProductionSiteId(),
                        root -> root.join(MaterialExport_.productionSite, JoinType.LEFT).get(ProductionSite_.id)
                    )
                );
            }
        }
        return specification;
    }
}
