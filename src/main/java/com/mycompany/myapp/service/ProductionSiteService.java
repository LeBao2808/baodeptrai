package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.ProductionSiteDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.ProductionSite}.
 */
public interface ProductionSiteService {
    /**
     * Save a productionSite.
     *
     * @param productionSiteDTO the entity to save.
     * @return the persisted entity.
     */
    ProductionSiteDTO save(ProductionSiteDTO productionSiteDTO);

    /**
     * Updates a productionSite.
     *
     * @param productionSiteDTO the entity to update.
     * @return the persisted entity.
     */
    ProductionSiteDTO update(ProductionSiteDTO productionSiteDTO);

    /**
     * Partially updates a productionSite.
     *
     * @param productionSiteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductionSiteDTO> partialUpdate(ProductionSiteDTO productionSiteDTO);

    /**
     * Get the "id" productionSite.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductionSiteDTO> findOne(Long id);

    /**
     * Delete the "id" productionSite.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
