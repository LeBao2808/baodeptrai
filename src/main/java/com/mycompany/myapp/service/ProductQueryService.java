package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.repository.ProductRepository;
import com.mycompany.myapp.service.criteria.ProductCriteria;
import com.mycompany.myapp.service.dto.ProductDTO;
import com.mycompany.myapp.service.mapper.ProductMapper;
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
 * Service for executing complex queries for {@link Product} entities in the database.
 * The main input is a {@link ProductCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProductDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductQueryService extends QueryService<Product> {

    private static final Logger log = LoggerFactory.getLogger(ProductQueryService.class);

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public ProductQueryService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    /**
     * Return a {@link Page} of {@link ProductDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductDTO> findByCriteria(ProductCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Product> specification = createSpecification(criteria);
        return productRepository.findAll(specification, page).map(productMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Product> specification = createSpecification(criteria);
        return productRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Product> createSpecification(ProductCriteria criteria) {
        Specification<Product> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Product_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Product_.name));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Product_.code));
            }
            if (criteria.getUnit() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUnit(), Product_.unit));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Product_.description));
            }
            if (criteria.getWeight() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWeight(), Product_.weight));
            }
            if (criteria.getHeight() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHeight(), Product_.height));
            }
            if (criteria.getWidth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWidth(), Product_.width));
            }
            if (criteria.getLength() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLength(), Product_.length));
            }
            if (criteria.getImageUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImageUrl(), Product_.imageUrl));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getType(), Product_.type));
            }
            if (criteria.getColor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getColor(), Product_.color));
            }
            if (criteria.getCbm() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCbm(), Product_.cbm));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Product_.price));
            }
            if (criteria.getConstruction() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getConstruction(), Product_.construction));
            }
            if (criteria.getMasterPackingQty() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMasterPackingQty(), Product_.masterPackingQty));
            }
            if (criteria.getMasterNestCode() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMasterNestCode(), Product_.masterNestCode));
            }
            if (criteria.getInnerQty() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInnerQty(), Product_.innerQty));
            }
            if (criteria.getPackSize() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPackSize(), Product_.packSize));
            }
            if (criteria.getNestCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNestCode(), Product_.nestCode));
            }
            if (criteria.getCountryOfOrigin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountryOfOrigin(), Product_.countryOfOrigin));
            }
            if (criteria.getVendorName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVendorName(), Product_.vendorName));
            }
            if (criteria.getNumberOfSet() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumberOfSet(), Product_.numberOfSet));
            }
            if (criteria.getPriceFOB() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPriceFOB(), Product_.priceFOB));
            }
            if (criteria.getQty40HC() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQty40HC(), Product_.qty40HC));
            }
            if (criteria.getd57Qty() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getd57Qty(), Product_.d57Qty));
            }
            if (criteria.getCategory() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCategory(), Product_.category));
            }
            if (criteria.getLabels() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLabels(), Product_.labels));
            }
            if (criteria.getPlanningNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPlanningNotes(), Product_.planningNotes));
            }
            if (criteria.getFactTag() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFactTag(), Product_.factTag));
            }
            if (criteria.getPackagingLength() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPackagingLength(), Product_.packagingLength));
            }
            if (criteria.getPackagingHeight() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPackagingHeight(), Product_.packagingHeight));
            }
            if (criteria.getPackagingWidth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPackagingWidth(), Product_.packagingWidth));
            }
            if (criteria.getSetIdProductId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getSetIdProductId(),
                        root -> root.join(Product_.setIdProduct, JoinType.LEFT).get(Product_.id)
                    )
                );
            }
            if (criteria.getParentProductId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getParentProductId(),
                        root -> root.join(Product_.parentProduct, JoinType.LEFT).get(Product_.id)
                    )
                );
            }
            if (criteria.getMaterialId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getMaterialId(), root -> root.join(Product_.material, JoinType.LEFT).get(Material_.id))
                );
            }
        }
        return specification;
    }
}
