package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ProductionSite;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductionSite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductionSiteRepository extends JpaRepository<ProductionSite, Long>, JpaSpecificationExecutor<ProductionSite> {}
