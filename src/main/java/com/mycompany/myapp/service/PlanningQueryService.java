package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Planning;
import com.mycompany.myapp.repository.PlanningRepository;
import com.mycompany.myapp.service.criteria.PlanningCriteria;
import com.mycompany.myapp.service.dto.PlanningDTO;
import com.mycompany.myapp.service.mapper.PlanningMapper;
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
 * Service for executing complex queries for {@link Planning} entities in the database.
 * The main input is a {@link PlanningCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PlanningDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PlanningQueryService extends QueryService<Planning> {

    private static final Logger log = LoggerFactory.getLogger(PlanningQueryService.class);

    private final PlanningRepository planningRepository;

    private final PlanningMapper planningMapper;

    public PlanningQueryService(PlanningRepository planningRepository, PlanningMapper planningMapper) {
        this.planningRepository = planningRepository;
        this.planningMapper = planningMapper;
    }

    /**
     * Return a {@link Page} of {@link PlanningDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PlanningDTO> findByCriteria(PlanningCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Planning> specification = createSpecification(criteria);
        return planningRepository.findAll(specification, page).map(planningMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PlanningCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Planning> specification = createSpecification(criteria);
        return planningRepository.count(specification);
    }

    /**
     * Function to convert {@link PlanningCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Planning> createSpecification(PlanningCriteria criteria) {
        Specification<Planning> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Planning_.id));
            }
            if (criteria.getOrderCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderCreationDate(), Planning_.orderCreationDate));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStatus(), Planning_.status));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Planning_.code));
            }
            if (criteria.getQuantificationId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getQuantificationId(),
                        root -> root.join(Planning_.quantification, JoinType.LEFT).get(Quantification_.id)
                    )
                );
            }
        }
        return specification;
    }
}
