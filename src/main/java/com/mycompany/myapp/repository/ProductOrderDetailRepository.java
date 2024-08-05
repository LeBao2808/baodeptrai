package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ProductOrderDetail;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductOrderDetail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductOrderDetailRepository
    extends JpaRepository<ProductOrderDetail, Long>, JpaSpecificationExecutor<ProductOrderDetail> {}
