package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Material;
import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.domain.Quantification;
import com.mycompany.myapp.service.dto.MaterialDTO;
import com.mycompany.myapp.service.dto.ProductDTO;
import com.mycompany.myapp.service.dto.QuantificationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Quantification} and its DTO {@link QuantificationDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuantificationMapper extends EntityMapper<QuantificationDTO, Quantification> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productId")
    @Mapping(target = "material", source = "material", qualifiedByName = "materialId")
    QuantificationDTO toDto(Quantification s);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);

    @Named("materialId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MaterialDTO toDtoMaterialId(Material material);
}
