package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.PlanningDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Planning}.
 */
public interface PlanningService {
    /**
     * Save a planning.
     *
     * @param planningDTO the entity to save.
     * @return the persisted entity.
     */
    PlanningDTO save(PlanningDTO planningDTO);

    /**
     * Updates a planning.
     *
     * @param planningDTO the entity to update.
     * @return the persisted entity.
     */
    PlanningDTO update(PlanningDTO planningDTO);

    /**
     * Partially updates a planning.
     *
     * @param planningDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PlanningDTO> partialUpdate(PlanningDTO planningDTO);

    /**
     * Get the "id" planning.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PlanningDTO> findOne(Long id);

    /**
     * Delete the "id" planning.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
