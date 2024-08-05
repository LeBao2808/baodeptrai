package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.MaterialInventory;
import com.mycompany.myapp.repository.MaterialInventoryRepository;
import com.mycompany.myapp.service.MaterialInventoryService;
import com.mycompany.myapp.service.dto.MaterialInventoryDTO;
import com.mycompany.myapp.service.mapper.MaterialInventoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.MaterialInventory}.
 */
@Service
@Transactional
public class MaterialInventoryServiceImpl implements MaterialInventoryService {

    private static final Logger log = LoggerFactory.getLogger(MaterialInventoryServiceImpl.class);

    private final MaterialInventoryRepository materialInventoryRepository;

    private final MaterialInventoryMapper materialInventoryMapper;

    public MaterialInventoryServiceImpl(
        MaterialInventoryRepository materialInventoryRepository,
        MaterialInventoryMapper materialInventoryMapper
    ) {
        this.materialInventoryRepository = materialInventoryRepository;
        this.materialInventoryMapper = materialInventoryMapper;
    }

    @Override
    public MaterialInventoryDTO save(MaterialInventoryDTO materialInventoryDTO) {
        log.debug("Request to save MaterialInventory : {}", materialInventoryDTO);
        MaterialInventory materialInventory = materialInventoryMapper.toEntity(materialInventoryDTO);
        materialInventory = materialInventoryRepository.save(materialInventory);
        return materialInventoryMapper.toDto(materialInventory);
    }

    @Override
    public MaterialInventoryDTO update(MaterialInventoryDTO materialInventoryDTO) {
        log.debug("Request to update MaterialInventory : {}", materialInventoryDTO);
        MaterialInventory materialInventory = materialInventoryMapper.toEntity(materialInventoryDTO);
        materialInventory = materialInventoryRepository.save(materialInventory);
        return materialInventoryMapper.toDto(materialInventory);
    }

    @Override
    public Optional<MaterialInventoryDTO> partialUpdate(MaterialInventoryDTO materialInventoryDTO) {
        log.debug("Request to partially update MaterialInventory : {}", materialInventoryDTO);

        return materialInventoryRepository
            .findById(materialInventoryDTO.getId())
            .map(existingMaterialInventory -> {
                materialInventoryMapper.partialUpdate(existingMaterialInventory, materialInventoryDTO);

                return existingMaterialInventory;
            })
            .map(materialInventoryRepository::save)
            .map(materialInventoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MaterialInventoryDTO> findOne(Long id) {
        log.debug("Request to get MaterialInventory : {}", id);
        return materialInventoryRepository.findById(id).map(materialInventoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MaterialInventory : {}", id);
        materialInventoryRepository.deleteById(id);
    }
}
