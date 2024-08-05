package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.ProductReceiptDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.ProductReceipt}.
 */
public interface ProductReceiptService {
    /**
     * Save a productReceipt.
     *
     * @param productReceiptDTO the entity to save.
     * @return the persisted entity.
     */
    ProductReceiptDTO save(ProductReceiptDTO productReceiptDTO);

    /**
     * Updates a productReceipt.
     *
     * @param productReceiptDTO the entity to update.
     * @return the persisted entity.
     */
    ProductReceiptDTO update(ProductReceiptDTO productReceiptDTO);

    /**
     * Partially updates a productReceipt.
     *
     * @param productReceiptDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductReceiptDTO> partialUpdate(ProductReceiptDTO productReceiptDTO);

    /**
     * Get the "id" productReceipt.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductReceiptDTO> findOne(Long id);

    /**
     * Delete the "id" productReceipt.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
