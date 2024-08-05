package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.domain.ProductionSite;
import com.mycompany.myapp.service.dto.ProductDTO;
import com.mycompany.myapp.service.dto.ProductionSiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductionSite} and its DTO {@link ProductionSiteDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductionSiteMapper extends EntityMapper<ProductionSiteDTO, ProductionSite> {
    @Mapping(target = "productId", source = "productId", qualifiedByName = "productId")
    ProductionSiteDTO toDto(ProductionSite s);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);
}
