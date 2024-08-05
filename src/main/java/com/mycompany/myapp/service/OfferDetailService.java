package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.OfferDetailDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.OfferDetail}.
 */
public interface OfferDetailService {
    /**
     * Save a offerDetail.
     *
     * @param offerDetailDTO the entity to save.
     * @return the persisted entity.
     */
    OfferDetailDTO save(OfferDetailDTO offerDetailDTO);

    /**
     * Updates a offerDetail.
     *
     * @param offerDetailDTO the entity to update.
     * @return the persisted entity.
     */
    OfferDetailDTO update(OfferDetailDTO offerDetailDTO);

    /**
     * Partially updates a offerDetail.
     *
     * @param offerDetailDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OfferDetailDTO> partialUpdate(OfferDetailDTO offerDetailDTO);

    /**
     * Get the "id" offerDetail.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OfferDetailDTO> findOne(Long id);

    /**
     * Delete the "id" offerDetail.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
