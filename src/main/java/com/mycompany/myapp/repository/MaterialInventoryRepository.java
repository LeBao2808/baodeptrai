package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.MaterialInventory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MaterialInventory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaterialInventoryRepository extends JpaRepository<MaterialInventory, Long>, JpaSpecificationExecutor<MaterialInventory> {}
