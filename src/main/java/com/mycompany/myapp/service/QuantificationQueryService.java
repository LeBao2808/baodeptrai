package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Quantification;
import com.mycompany.myapp.repository.QuantificationRepository;
import com.mycompany.myapp.service.criteria.QuantificationCriteria;
import com.mycompany.myapp.service.dto.QuantificationDTO;
import com.mycompany.myapp.service.mapper.QuantificationMapper;
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
 * Service for executing complex queries for {@link Quantification} entities in the database.
 * The main input is a {@link QuantificationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link QuantificationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QuantificationQueryService extends QueryService<Quantification> {

    private static final Logger log = LoggerFactory.getLogger(QuantificationQueryService.class);

    private final QuantificationRepository quantificationRepository;

    private final QuantificationMapper quantificationMapper;

    public QuantificationQueryService(QuantificationRepository quantificationRepository, QuantificationMapper quantificationMapper) {
        this.quantificationRepository = quantificationRepository;
        this.quantificationMapper = quantificationMapper;
    }

    /**
     * Return a {@link Page} of {@link QuantificationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QuantificationDTO> findByCriteria(QuantificationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Quantification> specification = createSpecification(criteria);
        return quantificationRepository.findAll(specification, page).map(quantificationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(QuantificationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Quantification> specification = createSpecification(criteria);
        return quantificationRepository.count(specification);
    }

    /**
     * Function to convert {@link QuantificationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Quantification> createSpecification(QuantificationCriteria criteria) {
        Specification<Quantification> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Quantification_.id));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), Quantification_.quantity));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductId(), root -> root.join(Quantification_.product, JoinType.LEFT).get(Product_.id))
                );
            }
            if (criteria.getMaterialId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getMaterialId(),
                        root -> root.join(Quantification_.material, JoinType.LEFT).get(Material_.id)
                    )
                );
            }
        }
        return specification;
    }
}
