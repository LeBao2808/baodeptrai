package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.ProductReceipt;
import com.mycompany.myapp.repository.ProductReceiptRepository;
import com.mycompany.myapp.service.ProductReceiptService;
import com.mycompany.myapp.service.dto.ProductReceiptDTO;
import com.mycompany.myapp.service.mapper.ProductReceiptMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.ProductReceipt}.
 */
@Service
@Transactional
public class ProductReceiptServiceImpl implements ProductReceiptService {

    private static final Logger log = LoggerFactory.getLogger(ProductReceiptServiceImpl.class);

    private final ProductReceiptRepository productReceiptRepository;

    private final ProductReceiptMapper productReceiptMapper;

    public ProductReceiptServiceImpl(ProductReceiptRepository productReceiptRepository, ProductReceiptMapper productReceiptMapper) {
        this.productReceiptRepository = productReceiptRepository;
        this.productReceiptMapper = productReceiptMapper;
    }

    @Override
    public ProductReceiptDTO save(ProductReceiptDTO productReceiptDTO) {
        log.debug("Request to save ProductReceipt : {}", productReceiptDTO);
        ProductReceipt productReceipt = productReceiptMapper.toEntity(productReceiptDTO);
        productReceipt = productReceiptRepository.save(productReceipt);
        return productReceiptMapper.toDto(productReceipt);
    }

    @Override
    public ProductReceiptDTO update(ProductReceiptDTO productReceiptDTO) {
        log.debug("Request to update ProductReceipt : {}", productReceiptDTO);
        ProductReceipt productReceipt = productReceiptMapper.toEntity(productReceiptDTO);
        productReceipt = productReceiptRepository.save(productReceipt);
        return productReceiptMapper.toDto(productReceipt);
    }

    @Override
    public Optional<ProductReceiptDTO> partialUpdate(ProductReceiptDTO productReceiptDTO) {
        log.debug("Request to partially update ProductReceipt : {}", productReceiptDTO);

        return productReceiptRepository
            .findById(productReceiptDTO.getId())
            .map(existingProductReceipt -> {
                productReceiptMapper.partialUpdate(existingProductReceipt, productReceiptDTO);

                return existingProductReceipt;
            })
            .map(productReceiptRepository::save)
            .map(productReceiptMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductReceiptDTO> findOne(Long id) {
        log.debug("Request to get ProductReceipt : {}", id);
        return productReceiptRepository.findById(id).map(productReceiptMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProductReceipt : {}", id);
        productReceiptRepository.deleteById(id);
    }
}
