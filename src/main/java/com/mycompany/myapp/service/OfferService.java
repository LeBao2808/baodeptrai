package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.OfferDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Offer}.
 */
public interface OfferService {
    /**
     * Save a offer.
     *
     * @param offerDTO the entity to save.
     * @return the persisted entity.
     */
    OfferDTO save(OfferDTO offerDTO);

    /**
     * Updates a offer.
     *
     * @param offerDTO the entity to update.
     * @return the persisted entity.
     */
    OfferDTO update(OfferDTO offerDTO);

    /**
     * Partially updates a offer.
     *
     * @param offerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OfferDTO> partialUpdate(OfferDTO offerDTO);

    /**
     * Get the "id" offer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OfferDTO> findOne(Long id);

    /**
     * Delete the "id" offer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
