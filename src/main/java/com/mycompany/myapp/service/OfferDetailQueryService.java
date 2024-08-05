package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.OfferDetail;
import com.mycompany.myapp.repository.OfferDetailRepository;
import com.mycompany.myapp.service.criteria.OfferDetailCriteria;
import com.mycompany.myapp.service.dto.OfferDetailDTO;
import com.mycompany.myapp.service.mapper.OfferDetailMapper;
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
 * Service for executing complex queries for {@link OfferDetail} entities in the database.
 * The main input is a {@link OfferDetailCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link OfferDetailDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OfferDetailQueryService extends QueryService<OfferDetail> {

    private static final Logger log = LoggerFactory.getLogger(OfferDetailQueryService.class);

    private final OfferDetailRepository offerDetailRepository;

    private final OfferDetailMapper offerDetailMapper;

    public OfferDetailQueryService(OfferDetailRepository offerDetailRepository, OfferDetailMapper offerDetailMapper) {
        this.offerDetailRepository = offerDetailRepository;
        this.offerDetailMapper = offerDetailMapper;
    }

    /**
     * Return a {@link Page} of {@link OfferDetailDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OfferDetailDTO> findByCriteria(OfferDetailCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OfferDetail> specification = createSpecification(criteria);
        return offerDetailRepository.findAll(specification, page).map(offerDetailMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OfferDetailCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OfferDetail> specification = createSpecification(criteria);
        return offerDetailRepository.count(specification);
    }

    /**
     * Function to convert {@link OfferDetailCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OfferDetail> createSpecification(OfferDetailCriteria criteria) {
        Specification<OfferDetail> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OfferDetail_.id));
            }
            if (criteria.getFeedback() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFeedback(), OfferDetail_.feedback));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductId(), root -> root.join(OfferDetail_.product, JoinType.LEFT).get(Product_.id))
                );
            }
            if (criteria.getOfferId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOfferId(), root -> root.join(OfferDetail_.offer, JoinType.LEFT).get(Offer_.id))
                );
            }
        }
        return specification;
    }
}
