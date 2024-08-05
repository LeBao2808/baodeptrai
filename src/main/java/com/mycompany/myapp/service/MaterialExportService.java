package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.MaterialExportDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.MaterialExport}.
 */
public interface MaterialExportService {
    /**
     * Save a materialExport.
     *
     * @param materialExportDTO the entity to save.
     * @return the persisted entity.
     */
    MaterialExportDTO save(MaterialExportDTO materialExportDTO);

    /**
     * Updates a materialExport.
     *
     * @param materialExportDTO the entity to update.
     * @return the persisted entity.
     */
    MaterialExportDTO update(MaterialExportDTO materialExportDTO);

    /**
     * Partially updates a materialExport.
     *
     * @param materialExportDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MaterialExportDTO> partialUpdate(MaterialExportDTO materialExportDTO);

    /**
     * Get the "id" materialExport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MaterialExportDTO> findOne(Long id);

    /**
     * Delete the "id" materialExport.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
