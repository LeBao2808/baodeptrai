package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.ProductReceipt;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.service.dto.ProductReceiptDTO;
import com.mycompany.myapp.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductReceipt} and its DTO {@link ProductReceiptDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductReceiptMapper extends EntityMapper<ProductReceiptDTO, ProductReceipt> {
    @Mapping(target = "created", source = "created", qualifiedByName = "userId")
    ProductReceiptDTO toDto(ProductReceipt s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
