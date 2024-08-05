package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ProductOrder;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long>, JpaSpecificationExecutor<ProductOrder> {
    @Query("select productOrder from ProductOrder productOrder where productOrder.createdByUser.login = ?#{authentication.name}")
    List<ProductOrder> findByCreatedByUserIsCurrentUser();
}
