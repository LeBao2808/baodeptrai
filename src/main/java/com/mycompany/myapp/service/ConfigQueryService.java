package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Config;
import com.mycompany.myapp.repository.ConfigRepository;
import com.mycompany.myapp.service.criteria.ConfigCriteria;
import com.mycompany.myapp.service.dto.ConfigDTO;
import com.mycompany.myapp.service.mapper.ConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Config} entities in the database.
 * The main input is a {@link ConfigCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ConfigDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ConfigQueryService extends QueryService<Config> {

    private static final Logger log = LoggerFactory.getLogger(ConfigQueryService.class);

    private final ConfigRepository configRepository;

    private final ConfigMapper configMapper;

    public ConfigQueryService(ConfigRepository configRepository, ConfigMapper configMapper) {
        this.configRepository = configRepository;
        this.configMapper = configMapper;
    }

    /**
     * Return a {@link Page} of {@link ConfigDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ConfigDTO> findByCriteria(ConfigCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Config> specification = createSpecification(criteria);
        return configRepository.findAll(specification, page).map(configMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ConfigCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Config> specification = createSpecification(criteria);
        return configRepository.count(specification);
    }

    /**
     * Function to convert {@link ConfigCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Config> createSpecification(ConfigCriteria criteria) {
        Specification<Config> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Config_.id));
            }
            if (criteria.getKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKey(), Config_.key));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValue(), Config_.value));
            }
        }
        return specification;
    }
}
