package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.MaterialExportDetail;
import com.mycompany.myapp.repository.MaterialExportDetailRepository;
import com.mycompany.myapp.service.criteria.MaterialExportDetailCriteria;
import com.mycompany.myapp.service.dto.MaterialExportDetailDTO;
import com.mycompany.myapp.service.mapper.MaterialExportDetailMapper;
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
 * Service for executing complex queries for {@link MaterialExportDetail} entities in the database.
 * The main input is a {@link MaterialExportDetailCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MaterialExportDetailDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MaterialExportDetailQueryService extends QueryService<MaterialExportDetail> {

    private static final Logger log = LoggerFactory.getLogger(MaterialExportDetailQueryService.class);

    private final MaterialExportDetailRepository materialExportDetailRepository;

    private final MaterialExportDetailMapper materialExportDetailMapper;

    public MaterialExportDetailQueryService(
        MaterialExportDetailRepository materialExportDetailRepository,
        MaterialExportDetailMapper materialExportDetailMapper
    ) {
        this.materialExportDetailRepository = materialExportDetailRepository;
        this.materialExportDetailMapper = materialExportDetailMapper;
    }

    /**
     * Return a {@link Page} of {@link MaterialExportDetailDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MaterialExportDetailDTO> findByCriteria(MaterialExportDetailCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MaterialExportDetail> specification = createSpecification(criteria);
        return materialExportDetailRepository.findAll(specification, page).map(materialExportDetailMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MaterialExportDetailCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MaterialExportDetail> specification = createSpecification(criteria);
        return materialExportDetailRepository.count(specification);
    }

    /**
     * Function to convert {@link MaterialExportDetailCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MaterialExportDetail> createSpecification(MaterialExportDetailCriteria criteria) {
        Specification<MaterialExportDetail> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MaterialExportDetail_.id));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), MaterialExportDetail_.note));
            }
            if (criteria.getExportPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExportPrice(), MaterialExportDetail_.exportPrice));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), MaterialExportDetail_.quantity));
            }
            if (criteria.getMaterialExportId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getMaterialExportId(),
                        root -> root.join(MaterialExportDetail_.materialExport, JoinType.LEFT).get(MaterialExport_.id)
                    )
                );
            }
            if (criteria.getMaterialId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getMaterialId(),
                        root -> root.join(MaterialExportDetail_.material, JoinType.LEFT).get(Material_.id)
                    )
                );
            }
        }
        return specification;
    }
}
