package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.MaterialReceipt;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MaterialReceipt entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaterialReceiptRepository extends JpaRepository<MaterialReceipt, Long>, JpaSpecificationExecutor<MaterialReceipt> {
    @Query(
        "select materialReceipt from MaterialReceipt materialReceipt where materialReceipt.createdByUser.login = ?#{authentication.name}"
    )
    List<MaterialReceipt> findByCreatedByUserIsCurrentUser();
}
