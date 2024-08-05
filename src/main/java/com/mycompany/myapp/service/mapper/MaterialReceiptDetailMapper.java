package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Material;
import com.mycompany.myapp.domain.MaterialReceipt;
import com.mycompany.myapp.domain.MaterialReceiptDetail;
import com.mycompany.myapp.service.dto.MaterialDTO;
import com.mycompany.myapp.service.dto.MaterialReceiptDTO;
import com.mycompany.myapp.service.dto.MaterialReceiptDetailDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MaterialReceiptDetail} and its DTO {@link MaterialReceiptDetailDTO}.
 */
@Mapper(componentModel = "spring")
public interface MaterialReceiptDetailMapper extends EntityMapper<MaterialReceiptDetailDTO, MaterialReceiptDetail> {
    @Mapping(target = "material", source = "material", qualifiedByName = "materialId")
    @Mapping(target = "receipt", source = "receipt", qualifiedByName = "materialReceiptId")
    MaterialReceiptDetailDTO toDto(MaterialReceiptDetail s);

    @Named("materialId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MaterialDTO toDtoMaterialId(Material material);

    @Named("materialReceiptId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MaterialReceiptDTO toDtoMaterialReceiptId(MaterialReceipt materialReceipt);
}
