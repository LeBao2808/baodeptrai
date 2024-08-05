package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.domain.ProductOrder;
import com.mycompany.myapp.domain.ProductOrderDetail;
import com.mycompany.myapp.service.dto.ProductDTO;
import com.mycompany.myapp.service.dto.ProductOrderDTO;
import com.mycompany.myapp.service.dto.ProductOrderDetailDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductOrderDetail} and its DTO {@link ProductOrderDetailDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductOrderDetailMapper extends EntityMapper<ProductOrderDetailDTO, ProductOrderDetail> {
    @Mapping(target = "order", source = "order", qualifiedByName = "productOrderId")
    @Mapping(target = "product", source = "product", qualifiedByName = "productId")
    ProductOrderDetailDTO toDto(ProductOrderDetail s);

    @Named("productOrderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductOrderDTO toDtoProductOrderId(ProductOrder productOrder);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);
}
