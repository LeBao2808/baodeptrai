package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.ProductReceiptDetailDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.ProductReceiptDetail}.
 */
public interface ProductReceiptDetailService {
    /**
     * Save a productReceiptDetail.
     *
     * @param productReceiptDetailDTO the entity to save.
     * @return the persisted entity.
     */
    ProductReceiptDetailDTO save(ProductReceiptDetailDTO productReceiptDetailDTO);

    /**
     * Updates a productReceiptDetail.
     *
     * @param productReceiptDetailDTO the entity to update.
     * @return the persisted entity.
     */
    ProductReceiptDetailDTO update(ProductReceiptDetailDTO productReceiptDetailDTO);

    /**
     * Partially updates a productReceiptDetail.
     *
     * @param productReceiptDetailDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductReceiptDetailDTO> partialUpdate(ProductReceiptDetailDTO productReceiptDetailDTO);

    /**
     * Get the "id" productReceiptDetail.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductReceiptDetailDTO> findOne(Long id);

    /**
     * Delete the "id" productReceiptDetail.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
