package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ProductReceipt;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductReceipt entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductReceiptRepository extends JpaRepository<ProductReceipt, Long>, JpaSpecificationExecutor<ProductReceipt> {
    @Query("select productReceipt from ProductReceipt productReceipt where productReceipt.created.login = ?#{authentication.name}")
    List<ProductReceipt> findByCreatedIsCurrentUser();
}
