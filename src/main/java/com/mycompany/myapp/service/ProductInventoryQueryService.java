package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.ProductInventory;
import com.mycompany.myapp.repository.ProductInventoryRepository;
import com.mycompany.myapp.service.criteria.ProductInventoryCriteria;
import com.mycompany.myapp.service.dto.ProductInventoryDTO;
import com.mycompany.myapp.service.mapper.ProductInventoryMapper;
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
 * Service for executing complex queries for {@link ProductInventory} entities in the database.
 * The main input is a {@link ProductInventoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProductInventoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductInventoryQueryService extends QueryService<ProductInventory> {

    private static final Logger log = LoggerFactory.getLogger(ProductInventoryQueryService.class);

    private final ProductInventoryRepository productInventoryRepository;

    private final ProductInventoryMapper productInventoryMapper;

    public ProductInventoryQueryService(
        ProductInventoryRepository productInventoryRepository,
        ProductInventoryMapper productInventoryMapper
    ) {
        this.productInventoryRepository = productInventoryRepository;
        this.productInventoryMapper = productInventoryMapper;
    }

    /**
     * Return a {@link Page} of {@link ProductInventoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductInventoryDTO> findByCriteria(ProductInventoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductInventory> specification = createSpecification(criteria);
        return productInventoryRepository.findAll(specification, page).map(productInventoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductInventoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProductInventory> specification = createSpecification(criteria);
        return productInventoryRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductInventoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductInventory> createSpecification(ProductInventoryCriteria criteria) {
        Specification<ProductInventory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProductInventory_.id));
            }
            if (criteria.getQuantityOnHand() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantityOnHand(), ProductInventory_.quantityOnHand));
            }
            if (criteria.getInventoryMonth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInventoryMonth(), ProductInventory_.inventoryMonth));
            }
            if (criteria.getInventoryYear() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInventoryYear(), ProductInventory_.inventoryYear));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getType(), ProductInventory_.type));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), ProductInventory_.price));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getProductId(),
                        root -> root.join(ProductInventory_.product, JoinType.LEFT).get(Product_.id)
                    )
                );
            }
        }
        return specification;
    }
}
