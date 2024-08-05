package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.ProductInventory;
import com.mycompany.myapp.repository.ProductInventoryRepository;
import com.mycompany.myapp.service.ProductInventoryService;
import com.mycompany.myapp.service.dto.ProductInventoryDTO;
import com.mycompany.myapp.service.mapper.ProductInventoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.ProductInventory}.
 */
@Service
@Transactional
public class ProductInventoryServiceImpl implements ProductInventoryService {

    private static final Logger log = LoggerFactory.getLogger(ProductInventoryServiceImpl.class);

    private final ProductInventoryRepository productInventoryRepository;

    private final ProductInventoryMapper productInventoryMapper;

    public ProductInventoryServiceImpl(
        ProductInventoryRepository productInventoryRepository,
        ProductInventoryMapper productInventoryMapper
    ) {
        this.productInventoryRepository = productInventoryRepository;
        this.productInventoryMapper = productInventoryMapper;
    }

    @Override
    public ProductInventoryDTO save(ProductInventoryDTO productInventoryDTO) {
        log.debug("Request to save ProductInventory : {}", productInventoryDTO);
        ProductInventory productInventory = productInventoryMapper.toEntity(productInventoryDTO);
        productInventory = productInventoryRepository.save(productInventory);
        return productInventoryMapper.toDto(productInventory);
    }

    @Override
    public ProductInventoryDTO update(ProductInventoryDTO productInventoryDTO) {
        log.debug("Request to update ProductInventory : {}", productInventoryDTO);
        ProductInventory productInventory = productInventoryMapper.toEntity(productInventoryDTO);
        productInventory = productInventoryRepository.save(productInventory);
        return productInventoryMapper.toDto(productInventory);
    }

    @Override
    public Optional<ProductInventoryDTO> partialUpdate(ProductInventoryDTO productInventoryDTO) {
        log.debug("Request to partially update ProductInventory : {}", productInventoryDTO);

        return productInventoryRepository
            .findById(productInventoryDTO.getId())
            .map(existingProductInventory -> {
                productInventoryMapper.partialUpdate(existingProductInventory, productInventoryDTO);

                return existingProductInventory;
            })
            .map(productInventoryRepository::save)
            .map(productInventoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductInventoryDTO> findOne(Long id) {
        log.debug("Request to get ProductInventory : {}", id);
        return productInventoryRepository.findById(id).map(productInventoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProductInventory : {}", id);
        productInventoryRepository.deleteById(id);
    }
}
