package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.MaterialReceipt;
import com.mycompany.myapp.domain.Planning;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.service.dto.MaterialReceiptDTO;
import com.mycompany.myapp.service.dto.PlanningDTO;
import com.mycompany.myapp.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MaterialReceipt} and its DTO {@link MaterialReceiptDTO}.
 */
@Mapper(componentModel = "spring")
public interface MaterialReceiptMapper extends EntityMapper<MaterialReceiptDTO, MaterialReceipt> {
    @Mapping(target = "createdByUser", source = "createdByUser", qualifiedByName = "userId")
    @Mapping(target = "quantificationOrder", source = "quantificationOrder", qualifiedByName = "planningId")
    MaterialReceiptDTO toDto(MaterialReceipt s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("planningId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PlanningDTO toDtoPlanningId(Planning planning);
}
