package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.MaterialReceiptDetailDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.MaterialReceiptDetail}.
 */
public interface MaterialReceiptDetailService {
    /**
     * Save a materialReceiptDetail.
     *
     * @param materialReceiptDetailDTO the entity to save.
     * @return the persisted entity.
     */
    MaterialReceiptDetailDTO save(MaterialReceiptDetailDTO materialReceiptDetailDTO);

    /**
     * Updates a materialReceiptDetail.
     *
     * @param materialReceiptDetailDTO the entity to update.
     * @return the persisted entity.
     */
    MaterialReceiptDetailDTO update(MaterialReceiptDetailDTO materialReceiptDetailDTO);

    /**
     * Partially updates a materialReceiptDetail.
     *
     * @param materialReceiptDetailDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MaterialReceiptDetailDTO> partialUpdate(MaterialReceiptDetailDTO materialReceiptDetailDTO);

    /**
     * Get the "id" materialReceiptDetail.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MaterialReceiptDetailDTO> findOne(Long id);

    /**
     * Delete the "id" materialReceiptDetail.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
