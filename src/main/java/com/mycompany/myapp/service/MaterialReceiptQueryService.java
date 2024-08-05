package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.MaterialReceipt;
import com.mycompany.myapp.repository.MaterialReceiptRepository;
import com.mycompany.myapp.service.criteria.MaterialReceiptCriteria;
import com.mycompany.myapp.service.dto.MaterialReceiptDTO;
import com.mycompany.myapp.service.mapper.MaterialReceiptMapper;
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
 * Service for executing complex queries for {@link MaterialReceipt} entities in the database.
 * The main input is a {@link MaterialReceiptCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MaterialReceiptDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MaterialReceiptQueryService extends QueryService<MaterialReceipt> {

    private static final Logger log = LoggerFactory.getLogger(MaterialReceiptQueryService.class);

    private final MaterialReceiptRepository materialReceiptRepository;

    private final MaterialReceiptMapper materialReceiptMapper;

    public MaterialReceiptQueryService(MaterialReceiptRepository materialReceiptRepository, MaterialReceiptMapper materialReceiptMapper) {
        this.materialReceiptRepository = materialReceiptRepository;
        this.materialReceiptMapper = materialReceiptMapper;
    }

    /**
     * Return a {@link Page} of {@link MaterialReceiptDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MaterialReceiptDTO> findByCriteria(MaterialReceiptCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MaterialReceipt> specification = createSpecification(criteria);
        return materialReceiptRepository.findAll(specification, page).map(materialReceiptMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MaterialReceiptCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MaterialReceipt> specification = createSpecification(criteria);
        return materialReceiptRepository.count(specification);
    }

    /**
     * Function to convert {@link MaterialReceiptCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MaterialReceipt> createSpecification(MaterialReceiptCriteria criteria) {
        Specification<MaterialReceipt> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MaterialReceipt_.id));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), MaterialReceipt_.creationDate));
            }
            if (criteria.getPaymentDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentDate(), MaterialReceipt_.paymentDate));
            }
            if (criteria.getReceiptDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReceiptDate(), MaterialReceipt_.receiptDate));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStatus(), MaterialReceipt_.status));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), MaterialReceipt_.code));
            }
            if (criteria.getCreatedByUserId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getCreatedByUserId(),
                        root -> root.join(MaterialReceipt_.createdByUser, JoinType.LEFT).get(User_.id)
                    )
                );
            }
            if (criteria.getQuantificationOrderId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getQuantificationOrderId(),
                        root -> root.join(MaterialReceipt_.quantificationOrder, JoinType.LEFT).get(Planning_.id)
                    )
                );
            }
        }
        return specification;
    }
}
