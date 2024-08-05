package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Material;
import com.mycompany.myapp.domain.Supplier;
import com.mycompany.myapp.service.dto.MaterialDTO;
import com.mycompany.myapp.service.dto.SupplierDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Supplier} and its DTO {@link SupplierDTO}.
 */
@Mapper(componentModel = "spring")
public interface SupplierMapper extends EntityMapper<SupplierDTO, Supplier> {
    @Mapping(target = "materialId", source = "materialId", qualifiedByName = "materialId")
    SupplierDTO toDto(Supplier s);

    @Named("materialId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MaterialDTO toDtoMaterialId(Material material);
}
