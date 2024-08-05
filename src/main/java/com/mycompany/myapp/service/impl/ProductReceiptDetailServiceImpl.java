package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.ProductReceiptDetail;
import com.mycompany.myapp.repository.ProductReceiptDetailRepository;
import com.mycompany.myapp.service.ProductReceiptDetailService;
import com.mycompany.myapp.service.dto.ProductReceiptDetailDTO;
import com.mycompany.myapp.service.mapper.ProductReceiptDetailMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.ProductReceiptDetail}.
 */
@Service
@Transactional
public class ProductReceiptDetailServiceImpl implements ProductReceiptDetailService {

    private static final Logger log = LoggerFactory.getLogger(ProductReceiptDetailServiceImpl.class);

    private final ProductReceiptDetailRepository productReceiptDetailRepository;

    private final ProductReceiptDetailMapper productReceiptDetailMapper;

    public ProductReceiptDetailServiceImpl(
        ProductReceiptDetailRepository productReceiptDetailRepository,
        ProductReceiptDetailMapper productReceiptDetailMapper
    ) {
        this.productReceiptDetailRepository = productReceiptDetailRepository;
        this.productReceiptDetailMapper = productReceiptDetailMapper;
    }

    @Override
    public ProductReceiptDetailDTO save(ProductReceiptDetailDTO productReceiptDetailDTO) {
        log.debug("Request to save ProductReceiptDetail : {}", productReceiptDetailDTO);
        ProductReceiptDetail productReceiptDetail = productReceiptDetailMapper.toEntity(productReceiptDetailDTO);
        productReceiptDetail = productReceiptDetailRepository.save(productReceiptDetail);
        return productReceiptDetailMapper.toDto(productReceiptDetail);
    }

    @Override
    public ProductReceiptDetailDTO update(ProductReceiptDetailDTO productReceiptDetailDTO) {
        log.debug("Request to update ProductReceiptDetail : {}", productReceiptDetailDTO);
        ProductReceiptDetail productReceiptDetail = productReceiptDetailMapper.toEntity(productReceiptDetailDTO);
        productReceiptDetail = productReceiptDetailRepository.save(productReceiptDetail);
        return productReceiptDetailMapper.toDto(productReceiptDetail);
    }

    @Override
    public Optional<ProductReceiptDetailDTO> partialUpdate(ProductReceiptDetailDTO productReceiptDetailDTO) {
        log.debug("Request to partially update ProductReceiptDetail : {}", productReceiptDetailDTO);

        return productReceiptDetailRepository
            .findById(productReceiptDetailDTO.getId())
            .map(existingProductReceiptDetail -> {
                productReceiptDetailMapper.partialUpdate(existingProductReceiptDetail, productReceiptDetailDTO);

                return existingProductReceiptDetail;
            })
            .map(productReceiptDetailRepository::save)
            .map(productReceiptDetailMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductReceiptDetailDTO> findOne(Long id) {
        log.debug("Request to get ProductReceiptDetail : {}", id);
        return productReceiptDetailRepository.findById(id).map(productReceiptDetailMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProductReceiptDetail : {}", id);
        productReceiptDetailRepository.deleteById(id);
    }
}
