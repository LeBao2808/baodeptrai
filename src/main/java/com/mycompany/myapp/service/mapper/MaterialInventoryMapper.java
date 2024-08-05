package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Material;
import com.mycompany.myapp.domain.MaterialInventory;
import com.mycompany.myapp.service.dto.MaterialDTO;
import com.mycompany.myapp.service.dto.MaterialInventoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MaterialInventory} and its DTO {@link MaterialInventoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface MaterialInventoryMapper extends EntityMapper<MaterialInventoryDTO, MaterialInventory> {
    @Mapping(target = "material", source = "material", qualifiedByName = "materialId")
    MaterialInventoryDTO toDto(MaterialInventory s);

    @Named("materialId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MaterialDTO toDtoMaterialId(Material material);
}
