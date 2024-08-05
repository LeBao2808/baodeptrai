package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.ProductOrderDetail;
import com.mycompany.myapp.repository.ProductOrderDetailRepository;
import com.mycompany.myapp.service.ProductOrderDetailService;
import com.mycompany.myapp.service.dto.ProductOrderDetailDTO;
import com.mycompany.myapp.service.mapper.ProductOrderDetailMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.ProductOrderDetail}.
 */
@Service
@Transactional
public class ProductOrderDetailServiceImpl implements ProductOrderDetailService {

    private static final Logger log = LoggerFactory.getLogger(ProductOrderDetailServiceImpl.class);

    private final ProductOrderDetailRepository productOrderDetailRepository;

    private final ProductOrderDetailMapper productOrderDetailMapper;

    public ProductOrderDetailServiceImpl(
        ProductOrderDetailRepository productOrderDetailRepository,
        ProductOrderDetailMapper productOrderDetailMapper
    ) {
        this.productOrderDetailRepository = productOrderDetailRepository;
        this.productOrderDetailMapper = productOrderDetailMapper;
    }

    @Override
    public ProductOrderDetailDTO save(ProductOrderDetailDTO productOrderDetailDTO) {
        log.debug("Request to save ProductOrderDetail : {}", productOrderDetailDTO);
        ProductOrderDetail productOrderDetail = productOrderDetailMapper.toEntity(productOrderDetailDTO);
        productOrderDetail = productOrderDetailRepository.save(productOrderDetail);
        return productOrderDetailMapper.toDto(productOrderDetail);
    }

    @Override
    public ProductOrderDetailDTO update(ProductOrderDetailDTO productOrderDetailDTO) {
        log.debug("Request to update ProductOrderDetail : {}", productOrderDetailDTO);
        ProductOrderDetail productOrderDetail = productOrderDetailMapper.toEntity(productOrderDetailDTO);
        productOrderDetail = productOrderDetailRepository.save(productOrderDetail);
        return productOrderDetailMapper.toDto(productOrderDetail);
    }

    @Override
    public Optional<ProductOrderDetailDTO> partialUpdate(ProductOrderDetailDTO productOrderDetailDTO) {
        log.debug("Request to partially update ProductOrderDetail : {}", productOrderDetailDTO);

        return productOrderDetailRepository
            .findById(productOrderDetailDTO.getId())
            .map(existingProductOrderDetail -> {
                productOrderDetailMapper.partialUpdate(existingProductOrderDetail, productOrderDetailDTO);

                return existingProductOrderDetail;
            })
            .map(productOrderDetailRepository::save)
            .map(productOrderDetailMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductOrderDetailDTO> findOne(Long id) {
        log.debug("Request to get ProductOrderDetail : {}", id);
        return productOrderDetailRepository.findById(id).map(productOrderDetailMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProductOrderDetail : {}", id);
        productOrderDetailRepository.deleteById(id);
    }
}
