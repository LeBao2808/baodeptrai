package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.MaterialReceiptDetail;
import com.mycompany.myapp.repository.MaterialReceiptDetailRepository;
import com.mycompany.myapp.service.criteria.MaterialReceiptDetailCriteria;
import com.mycompany.myapp.service.dto.MaterialReceiptDetailDTO;
import com.mycompany.myapp.service.mapper.MaterialReceiptDetailMapper;
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
 * Service for executing complex queries for {@link MaterialReceiptDetail} entities in the database.
 * The main input is a {@link MaterialReceiptDetailCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MaterialReceiptDetailDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MaterialReceiptDetailQueryService extends QueryService<MaterialReceiptDetail> {

    private static final Logger log = LoggerFactory.getLogger(MaterialReceiptDetailQueryService.class);

    private final MaterialReceiptDetailRepository materialReceiptDetailRepository;

    private final MaterialReceiptDetailMapper materialReceiptDetailMapper;

    public MaterialReceiptDetailQueryService(
        MaterialReceiptDetailRepository materialReceiptDetailRepository,
        MaterialReceiptDetailMapper materialReceiptDetailMapper
    ) {
        this.materialReceiptDetailRepository = materialReceiptDetailRepository;
        this.materialReceiptDetailMapper = materialReceiptDetailMapper;
    }

    /**
     * Return a {@link Page} of {@link MaterialReceiptDetailDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MaterialReceiptDetailDTO> findByCriteria(MaterialReceiptDetailCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MaterialReceiptDetail> specification = createSpecification(criteria);
        return materialReceiptDetailRepository.findAll(specification, page).map(materialReceiptDetailMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MaterialReceiptDetailCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MaterialReceiptDetail> specification = createSpecification(criteria);
        return materialReceiptDetailRepository.count(specification);
    }

    /**
     * Function to convert {@link MaterialReceiptDetailCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MaterialReceiptDetail> createSpecification(MaterialReceiptDetailCriteria criteria) {
        Specification<MaterialReceiptDetail> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MaterialReceiptDetail_.id));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), MaterialReceiptDetail_.note));
            }
            if (criteria.getImportPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getImportPrice(), MaterialReceiptDetail_.importPrice));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), MaterialReceiptDetail_.quantity));
            }
            if (criteria.getMaterialId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getMaterialId(),
                        root -> root.join(MaterialReceiptDetail_.material, JoinType.LEFT).get(Material_.id)
                    )
                );
            }
            if (criteria.getReceiptId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getReceiptId(),
                        root -> root.join(MaterialReceiptDetail_.receipt, JoinType.LEFT).get(MaterialReceipt_.id)
                    )
                );
            }
        }
        return specification;
    }
}
