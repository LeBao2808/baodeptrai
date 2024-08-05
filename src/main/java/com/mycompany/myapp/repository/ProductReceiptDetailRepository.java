package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ProductReceiptDetail;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductReceiptDetail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductReceiptDetailRepository
    extends JpaRepository<ProductReceiptDetail, Long>, JpaSpecificationExecutor<ProductReceiptDetail> {}
