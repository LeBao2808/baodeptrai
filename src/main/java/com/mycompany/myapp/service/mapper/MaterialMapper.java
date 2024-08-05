package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Material;
import com.mycompany.myapp.service.dto.MaterialDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Material} and its DTO {@link MaterialDTO}.
 */
@Mapper(componentModel = "spring")
public interface MaterialMapper extends EntityMapper<MaterialDTO, Material> {}
