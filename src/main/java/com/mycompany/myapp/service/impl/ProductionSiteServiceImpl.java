package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.ProductionSite;
import com.mycompany.myapp.repository.ProductionSiteRepository;
import com.mycompany.myapp.service.ProductionSiteService;
import com.mycompany.myapp.service.dto.ProductionSiteDTO;
import com.mycompany.myapp.service.mapper.ProductionSiteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.ProductionSite}.
 */
@Service
@Transactional
public class ProductionSiteServiceImpl implements ProductionSiteService {

    private static final Logger log = LoggerFactory.getLogger(ProductionSiteServiceImpl.class);

    private final ProductionSiteRepository productionSiteRepository;

    private final ProductionSiteMapper productionSiteMapper;

    public ProductionSiteServiceImpl(ProductionSiteRepository productionSiteRepository, ProductionSiteMapper productionSiteMapper) {
        this.productionSiteRepository = productionSiteRepository;
        this.productionSiteMapper = productionSiteMapper;
    }

    @Override
    public ProductionSiteDTO save(ProductionSiteDTO productionSiteDTO) {
        log.debug("Request to save ProductionSite : {}", productionSiteDTO);
        ProductionSite productionSite = productionSiteMapper.toEntity(productionSiteDTO);
        productionSite = productionSiteRepository.save(productionSite);
        return productionSiteMapper.toDto(productionSite);
    }

    @Override
    public ProductionSiteDTO update(ProductionSiteDTO productionSiteDTO) {
        log.debug("Request to update ProductionSite : {}", productionSiteDTO);
        ProductionSite productionSite = productionSiteMapper.toEntity(productionSiteDTO);
        productionSite = productionSiteRepository.save(productionSite);
        return productionSiteMapper.toDto(productionSite);
    }

    @Override
    public Optional<ProductionSiteDTO> partialUpdate(ProductionSiteDTO productionSiteDTO) {
        log.debug("Request to partially update ProductionSite : {}", productionSiteDTO);

        return productionSiteRepository
            .findById(productionSiteDTO.getId())
            .map(existingProductionSite -> {
                productionSiteMapper.partialUpdate(existingProductionSite, productionSiteDTO);

                return existingProductionSite;
            })
            .map(productionSiteRepository::save)
            .map(productionSiteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductionSiteDTO> findOne(Long id) {
        log.debug("Request to get ProductionSite : {}", id);
        return productionSiteRepository.findById(id).map(productionSiteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProductionSite : {}", id);
        productionSiteRepository.deleteById(id);
    }
}
