package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.ProductOrderDetail;
import com.mycompany.myapp.repository.ProductOrderDetailRepository;
import com.mycompany.myapp.service.criteria.ProductOrderDetailCriteria;
import com.mycompany.myapp.service.dto.ProductOrderDetailDTO;
import com.mycompany.myapp.service.mapper.ProductOrderDetailMapper;
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
 * Service for executing complex queries for {@link ProductOrderDetail} entities in the database.
 * The main input is a {@link ProductOrderDetailCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProductOrderDetailDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductOrderDetailQueryService extends QueryService<ProductOrderDetail> {

    private static final Logger log = LoggerFactory.getLogger(ProductOrderDetailQueryService.class);

    private final ProductOrderDetailRepository productOrderDetailRepository;

    private final ProductOrderDetailMapper productOrderDetailMapper;

    public ProductOrderDetailQueryService(
        ProductOrderDetailRepository productOrderDetailRepository,
        ProductOrderDetailMapper productOrderDetailMapper
    ) {
        this.productOrderDetailRepository = productOrderDetailRepository;
        this.productOrderDetailMapper = productOrderDetailMapper;
    }

    /**
     * Return a {@link Page} of {@link ProductOrderDetailDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductOrderDetailDTO> findByCriteria(ProductOrderDetailCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductOrderDetail> specification = createSpecification(criteria);
        return productOrderDetailRepository.findAll(specification, page).map(productOrderDetailMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductOrderDetailCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProductOrderDetail> specification = createSpecification(criteria);
        return productOrderDetailRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductOrderDetailCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductOrderDetail> createSpecification(ProductOrderDetailCriteria criteria) {
        Specification<ProductOrderDetail> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProductOrderDetail_.id));
            }
            if (criteria.getOrderCreationDate() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getOrderCreationDate(), ProductOrderDetail_.orderCreationDate)
                );
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), ProductOrderDetail_.quantity));
            }
            if (criteria.getUnitPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUnitPrice(), ProductOrderDetail_.unitPrice));
            }
            if (criteria.getOrderId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getOrderId(),
                        root -> root.join(ProductOrderDetail_.order, JoinType.LEFT).get(ProductOrder_.id)
                    )
                );
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getProductId(),
                        root -> root.join(ProductOrderDetail_.product, JoinType.LEFT).get(Product_.id)
                    )
                );
            }
        }
        return specification;
    }
}
