package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.QuantificationDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Quantification}.
 */
public interface QuantificationService {
    /**
     * Save a quantification.
     *
     * @param quantificationDTO the entity to save.
     * @return the persisted entity.
     */
    QuantificationDTO save(QuantificationDTO quantificationDTO);

    /**
     * Updates a quantification.
     *
     * @param quantificationDTO the entity to update.
     * @return the persisted entity.
     */
    QuantificationDTO update(QuantificationDTO quantificationDTO);

    /**
     * Partially updates a quantification.
     *
     * @param quantificationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<QuantificationDTO> partialUpdate(QuantificationDTO quantificationDTO);

    /**
     * Get the "id" quantification.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<QuantificationDTO> findOne(Long id);

    /**
     * Delete the "id" quantification.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
