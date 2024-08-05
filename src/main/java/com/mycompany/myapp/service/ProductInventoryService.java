package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.ProductInventoryDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.ProductInventory}.
 */
public interface ProductInventoryService {
    /**
     * Save a productInventory.
     *
     * @param productInventoryDTO the entity to save.
     * @return the persisted entity.
     */
    ProductInventoryDTO save(ProductInventoryDTO productInventoryDTO);

    /**
     * Updates a productInventory.
     *
     * @param productInventoryDTO the entity to update.
     * @return the persisted entity.
     */
    ProductInventoryDTO update(ProductInventoryDTO productInventoryDTO);

    /**
     * Partially updates a productInventory.
     *
     * @param productInventoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductInventoryDTO> partialUpdate(ProductInventoryDTO productInventoryDTO);

    /**
     * Get the "id" productInventory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductInventoryDTO> findOne(Long id);

    /**
     * Delete the "id" productInventory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
