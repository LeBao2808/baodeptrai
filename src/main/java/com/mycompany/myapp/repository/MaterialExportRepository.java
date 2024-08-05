package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.MaterialExport;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MaterialExport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaterialExportRepository extends JpaRepository<MaterialExport, Long>, JpaSpecificationExecutor<MaterialExport> {
    @Query("select materialExport from MaterialExport materialExport where materialExport.createdByUser.login = ?#{authentication.name}")
    List<MaterialExport> findByCreatedByUserIsCurrentUser();
}
