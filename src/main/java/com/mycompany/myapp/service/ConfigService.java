package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.ConfigDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Config}.
 */
public interface ConfigService {
    /**
     * Save a config.
     *
     * @param configDTO the entity to save.
     * @return the persisted entity.
     */
    ConfigDTO save(ConfigDTO configDTO);

    /**
     * Updates a config.
     *
     * @param configDTO the entity to update.
     * @return the persisted entity.
     */
    ConfigDTO update(ConfigDTO configDTO);

    /**
     * Partially updates a config.
     *
     * @param configDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ConfigDTO> partialUpdate(ConfigDTO configDTO);

    /**
     * Get the "id" config.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConfigDTO> findOne(Long id);

    /**
     * Delete the "id" config.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
