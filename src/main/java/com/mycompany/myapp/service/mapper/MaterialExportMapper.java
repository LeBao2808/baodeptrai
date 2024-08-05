package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.MaterialExport;
import com.mycompany.myapp.domain.ProductionSite;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.service.dto.MaterialExportDTO;
import com.mycompany.myapp.service.dto.ProductionSiteDTO;
import com.mycompany.myapp.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MaterialExport} and its DTO {@link MaterialExportDTO}.
 */
@Mapper(componentModel = "spring")
public interface MaterialExportMapper extends EntityMapper<MaterialExportDTO, MaterialExport> {
    @Mapping(target = "createdByUser", source = "createdByUser", qualifiedByName = "userId")
    @Mapping(target = "productionSite", source = "productionSite", qualifiedByName = "productionSiteId")
    MaterialExportDTO toDto(MaterialExport s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("productionSiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductionSiteDTO toDtoProductionSiteId(ProductionSite productionSite);
}
