package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.ProductReceipt;
import com.mycompany.myapp.repository.ProductReceiptRepository;
import com.mycompany.myapp.service.criteria.ProductReceiptCriteria;
import com.mycompany.myapp.service.dto.ProductReceiptDTO;
import com.mycompany.myapp.service.mapper.ProductReceiptMapper;
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
 * Service for executing complex queries for {@link ProductReceipt} entities in the database.
 * The main input is a {@link ProductReceiptCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProductReceiptDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductReceiptQueryService extends QueryService<ProductReceipt> {

    private static final Logger log = LoggerFactory.getLogger(ProductReceiptQueryService.class);

    private final ProductReceiptRepository productReceiptRepository;

    private final ProductReceiptMapper productReceiptMapper;

    public ProductReceiptQueryService(ProductReceiptRepository productReceiptRepository, ProductReceiptMapper productReceiptMapper) {
        this.productReceiptRepository = productReceiptRepository;
        this.productReceiptMapper = productReceiptMapper;
    }

    /**
     * Return a {@link Page} of {@link ProductReceiptDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductReceiptDTO> findByCriteria(ProductReceiptCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductReceipt> specification = createSpecification(criteria);
        return productReceiptRepository.findAll(specification, page).map(productReceiptMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductReceiptCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProductReceipt> specification = createSpecification(criteria);
        return productReceiptRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductReceiptCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductReceipt> createSpecification(ProductReceiptCriteria criteria) {
        Specification<ProductReceipt> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProductReceipt_.id));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), ProductReceipt_.creationDate));
            }
            if (criteria.getPaymentDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentDate(), ProductReceipt_.paymentDate));
            }
            if (criteria.getReceiptDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReceiptDate(), ProductReceipt_.receiptDate));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStatus(), ProductReceipt_.status));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), ProductReceipt_.code));
            }
            if (criteria.getCreatedId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCreatedId(), root -> root.join(ProductReceipt_.created, JoinType.LEFT).get(User_.id))
                );
            }
        }
        return specification;
    }
}
