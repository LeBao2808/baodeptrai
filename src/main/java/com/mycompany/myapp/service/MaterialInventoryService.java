package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.MaterialInventoryDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.MaterialInventory}.
 */
public interface MaterialInventoryService {
    /**
     * Save a materialInventory.
     *
     * @param materialInventoryDTO the entity to save.
     * @return the persisted entity.
     */
    MaterialInventoryDTO save(MaterialInventoryDTO materialInventoryDTO);

    /**
     * Updates a materialInventory.
     *
     * @param materialInventoryDTO the entity to update.
     * @return the persisted entity.
     */
    MaterialInventoryDTO update(MaterialInventoryDTO materialInventoryDTO);

    /**
     * Partially updates a materialInventory.
     *
     * @param materialInventoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MaterialInventoryDTO> partialUpdate(MaterialInventoryDTO materialInventoryDTO);

    /**
     * Get the "id" materialInventory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MaterialInventoryDTO> findOne(Long id);

    /**
     * Delete the "id" materialInventory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
