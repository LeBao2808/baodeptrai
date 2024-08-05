package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.MaterialExportDetail;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MaterialExportDetail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaterialExportDetailRepository
    extends JpaRepository<MaterialExportDetail, Long>, JpaSpecificationExecutor<MaterialExportDetail> {}
