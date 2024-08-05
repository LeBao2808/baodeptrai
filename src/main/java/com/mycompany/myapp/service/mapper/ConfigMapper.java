package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Config;
import com.mycompany.myapp.service.dto.ConfigDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Config} and its DTO {@link ConfigDTO}.
 */
@Mapper(componentModel = "spring")
public interface ConfigMapper extends EntityMapper<ConfigDTO, Config> {}
