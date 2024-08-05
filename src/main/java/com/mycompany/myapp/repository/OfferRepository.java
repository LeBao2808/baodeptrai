package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Offer;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Offer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OfferRepository extends JpaRepository<Offer, Long>, JpaSpecificationExecutor<Offer> {
    @Query("select offer from Offer offer where offer.user.login = ?#{authentication.name}")
    List<Offer> findByUserIsCurrentUser();
}
