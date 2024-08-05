package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.ProductReceiptDetail;
import com.mycompany.myapp.repository.ProductReceiptDetailRepository;
import com.mycompany.myapp.service.criteria.ProductReceiptDetailCriteria;
import com.mycompany.myapp.service.dto.ProductReceiptDetailDTO;
import com.mycompany.myapp.service.mapper.ProductReceiptDetailMapper;
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
 * Service for executing complex queries for {@link ProductReceiptDetail} entities in the database.
 * The main input is a {@link ProductReceiptDetailCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProductReceiptDetailDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductReceiptDetailQueryService extends QueryService<ProductReceiptDetail> {

    private static final Logger log = LoggerFactory.getLogger(ProductReceiptDetailQueryService.class);

    private final ProductReceiptDetailRepository productReceiptDetailRepository;

    private final ProductReceiptDetailMapper productReceiptDetailMapper;

    public ProductReceiptDetailQueryService(
        ProductReceiptDetailRepository productReceiptDetailRepository,
        ProductReceiptDetailMapper productReceiptDetailMapper
    ) {
        this.productReceiptDetailRepository = productReceiptDetailRepository;
        this.productReceiptDetailMapper = productReceiptDetailMapper;
    }

    /**
     * Return a {@link Page} of {@link ProductReceiptDetailDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductReceiptDetailDTO> findByCriteria(ProductReceiptDetailCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductReceiptDetail> specification = createSpecification(criteria);
        return productReceiptDetailRepository.findAll(specification, page).map(productReceiptDetailMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductReceiptDetailCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProductReceiptDetail> specification = createSpecification(criteria);
        return productReceiptDetailRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductReceiptDetailCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductReceiptDetail> createSpecification(ProductReceiptDetailCriteria criteria) {
        Specification<ProductReceiptDetail> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProductReceiptDetail_.id));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), ProductReceiptDetail_.note));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), ProductReceiptDetail_.quantity));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getProductId(),
                        root -> root.join(ProductReceiptDetail_.product, JoinType.LEFT).get(Product_.id)
                    )
                );
            }
            if (criteria.getReceiptId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getReceiptId(),
                        root -> root.join(ProductReceiptDetail_.receipt, JoinType.LEFT).get(ProductReceipt_.id)
                    )
                );
            }
        }
        return specification;
    }
}
