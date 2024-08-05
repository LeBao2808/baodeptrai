package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.domain.ProductReceipt;
import com.mycompany.myapp.domain.ProductReceiptDetail;
import com.mycompany.myapp.service.dto.ProductDTO;
import com.mycompany.myapp.service.dto.ProductReceiptDTO;
import com.mycompany.myapp.service.dto.ProductReceiptDetailDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductReceiptDetail} and its DTO {@link ProductReceiptDetailDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductReceiptDetailMapper extends EntityMapper<ProductReceiptDetailDTO, ProductReceiptDetail> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productId")
    @Mapping(target = "receipt", source = "receipt", qualifiedByName = "productReceiptId")
    ProductReceiptDetailDTO toDto(ProductReceiptDetail s);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);

    @Named("productReceiptId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductReceiptDTO toDtoProductReceiptId(ProductReceipt productReceipt);
}
