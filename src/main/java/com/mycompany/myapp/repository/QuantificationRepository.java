package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Quantification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Quantification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuantificationRepository extends JpaRepository<Quantification, Long>, JpaSpecificationExecutor<Quantification> {}
