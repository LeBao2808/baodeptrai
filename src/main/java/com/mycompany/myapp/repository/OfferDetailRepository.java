package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.OfferDetail;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OfferDetail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OfferDetailRepository extends JpaRepository<OfferDetail, Long>, JpaSpecificationExecutor<OfferDetail> {}
