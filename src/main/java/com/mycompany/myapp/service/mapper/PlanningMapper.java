package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Planning;
import com.mycompany.myapp.domain.Quantification;
import com.mycompany.myapp.service.dto.PlanningDTO;
import com.mycompany.myapp.service.dto.QuantificationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Planning} and its DTO {@link PlanningDTO}.
 */
@Mapper(componentModel = "spring")
public interface PlanningMapper extends EntityMapper<PlanningDTO, Planning> {
    @Mapping(target = "quantification", source = "quantification", qualifiedByName = "quantificationId")
    PlanningDTO toDto(Planning s);

    @Named("quantificationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuantificationDTO toDtoQuantificationId(Quantification quantification);
}
