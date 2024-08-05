package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.MaterialExportDetailDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.MaterialExportDetail}.
 */
public interface MaterialExportDetailService {
    /**
     * Save a materialExportDetail.
     *
     * @param materialExportDetailDTO the entity to save.
     * @return the persisted entity.
     */
    MaterialExportDetailDTO save(MaterialExportDetailDTO materialExportDetailDTO);

    /**
     * Updates a materialExportDetail.
     *
     * @param materialExportDetailDTO the entity to update.
     * @return the persisted entity.
     */
    MaterialExportDetailDTO update(MaterialExportDetailDTO materialExportDetailDTO);

    /**
     * Partially updates a materialExportDetail.
     *
     * @param materialExportDetailDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MaterialExportDetailDTO> partialUpdate(MaterialExportDetailDTO materialExportDetailDTO);

    /**
     * Get the "id" materialExportDetail.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MaterialExportDetailDTO> findOne(Long id);

    /**
     * Delete the "id" materialExportDetail.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
