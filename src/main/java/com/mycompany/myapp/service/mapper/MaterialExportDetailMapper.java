package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Material;
import com.mycompany.myapp.domain.MaterialExport;
import com.mycompany.myapp.domain.MaterialExportDetail;
import com.mycompany.myapp.service.dto.MaterialDTO;
import com.mycompany.myapp.service.dto.MaterialExportDTO;
import com.mycompany.myapp.service.dto.MaterialExportDetailDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MaterialExportDetail} and its DTO {@link MaterialExportDetailDTO}.
 */
@Mapper(componentModel = "spring")
public interface MaterialExportDetailMapper extends EntityMapper<MaterialExportDetailDTO, MaterialExportDetail> {
    @Mapping(target = "materialExport", source = "materialExport", qualifiedByName = "materialExportId")
    @Mapping(target = "material", source = "material", qualifiedByName = "materialId")
    MaterialExportDetailDTO toDto(MaterialExportDetail s);

    @Named("materialExportId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MaterialExportDTO toDtoMaterialExportId(MaterialExport materialExport);

    @Named("materialId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MaterialDTO toDtoMaterialId(Material material);
}
