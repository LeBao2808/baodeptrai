package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.MaterialReceiptDetail;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MaterialReceiptDetail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaterialReceiptDetailRepository
    extends JpaRepository<MaterialReceiptDetail, Long>, JpaSpecificationExecutor<MaterialReceiptDetail> {}
