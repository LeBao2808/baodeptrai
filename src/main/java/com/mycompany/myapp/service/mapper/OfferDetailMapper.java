package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Offer;
import com.mycompany.myapp.domain.OfferDetail;
import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.service.dto.OfferDTO;
import com.mycompany.myapp.service.dto.OfferDetailDTO;
import com.mycompany.myapp.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OfferDetail} and its DTO {@link OfferDetailDTO}.
 */
@Mapper(componentModel = "spring")
public interface OfferDetailMapper extends EntityMapper<OfferDetailDTO, OfferDetail> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productId")
    @Mapping(target = "offer", source = "offer", qualifiedByName = "offerId")
    OfferDetailDTO toDto(OfferDetail s);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);

    @Named("offerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OfferDTO toDtoOfferId(Offer offer);
}
