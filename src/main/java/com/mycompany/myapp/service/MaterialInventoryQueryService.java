package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.MaterialInventory;
import com.mycompany.myapp.repository.MaterialInventoryRepository;
import com.mycompany.myapp.service.criteria.MaterialInventoryCriteria;
import com.mycompany.myapp.service.dto.MaterialInventoryDTO;
import com.mycompany.myapp.service.mapper.MaterialInventoryMapper;
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
 * Service for executing complex queries for {@link MaterialInventory} entities in the database.
 * The main input is a {@link MaterialInventoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MaterialInventoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MaterialInventoryQueryService extends QueryService<MaterialInventory> {

    private static final Logger log = LoggerFactory.getLogger(MaterialInventoryQueryService.class);

    private final MaterialInventoryRepository materialInventoryRepository;

    private final MaterialInventoryMapper materialInventoryMapper;

    public MaterialInventoryQueryService(
        MaterialInventoryRepository materialInventoryRepository,
        MaterialInventoryMapper materialInventoryMapper
    ) {
        this.materialInventoryRepository = materialInventoryRepository;
        this.materialInventoryMapper = materialInventoryMapper;
    }

    /**
     * Return a {@link Page} of {@link MaterialInventoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MaterialInventoryDTO> findByCriteria(MaterialInventoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MaterialInventory> specification = createSpecification(criteria);
        return materialInventoryRepository.findAll(specification, page).map(materialInventoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MaterialInventoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MaterialInventory> specification = createSpecification(criteria);
        return materialInventoryRepository.count(specification);
    }

    /**
     * Function to convert {@link MaterialInventoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MaterialInventory> createSpecification(MaterialInventoryCriteria criteria) {
        Specification<MaterialInventory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MaterialInventory_.id));
            }
            if (criteria.getQuantityOnHand() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantityOnHand(), MaterialInventory_.quantityOnHand));
            }
            if (criteria.getInventoryMonth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInventoryMonth(), MaterialInventory_.inventoryMonth));
            }
            if (criteria.getInventoryYear() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInventoryYear(), MaterialInventory_.inventoryYear));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getType(), MaterialInventory_.type));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), MaterialInventory_.price));
            }
            if (criteria.getMaterialId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getMaterialId(),
                        root -> root.join(MaterialInventory_.material, JoinType.LEFT).get(Material_.id)
                    )
                );
            }
        }
        return specification;
    }
}
