package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.MaterialReceiptDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.MaterialReceipt}.
 */
public interface MaterialReceiptService {
    /**
     * Save a materialReceipt.
     *
     * @param materialReceiptDTO the entity to save.
     * @return the persisted entity.
     */
    MaterialReceiptDTO save(MaterialReceiptDTO materialReceiptDTO);

    /**
     * Updates a materialReceipt.
     *
     * @param materialReceiptDTO the entity to update.
     * @return the persisted entity.
     */
    MaterialReceiptDTO update(MaterialReceiptDTO materialReceiptDTO);

    /**
     * Partially updates a materialReceipt.
     *
     * @param materialReceiptDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MaterialReceiptDTO> partialUpdate(MaterialReceiptDTO materialReceiptDTO);

    /**
     * Get the "id" materialReceipt.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MaterialReceiptDTO> findOne(Long id);

    /**
     * Delete the "id" materialReceipt.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
