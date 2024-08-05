package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.ProductOrderDetailDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.ProductOrderDetail}.
 */
public interface ProductOrderDetailService {
    /**
     * Save a productOrderDetail.
     *
     * @param productOrderDetailDTO the entity to save.
     * @return the persisted entity.
     */
    ProductOrderDetailDTO save(ProductOrderDetailDTO productOrderDetailDTO);

    /**
     * Updates a productOrderDetail.
     *
     * @param productOrderDetailDTO the entity to update.
     * @return the persisted entity.
     */
    ProductOrderDetailDTO update(ProductOrderDetailDTO productOrderDetailDTO);

    /**
     * Partially updates a productOrderDetail.
     *
     * @param productOrderDetailDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductOrderDetailDTO> partialUpdate(ProductOrderDetailDTO productOrderDetailDTO);

    /**
     * Get the "id" productOrderDetail.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductOrderDetailDTO> findOne(Long id);

    /**
     * Delete the "id" productOrderDetail.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
