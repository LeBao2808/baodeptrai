package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.MaterialReceipt;
import com.mycompany.myapp.repository.MaterialReceiptRepository;
import com.mycompany.myapp.service.MaterialReceiptService;
import com.mycompany.myapp.service.dto.MaterialReceiptDTO;
import com.mycompany.myapp.service.mapper.MaterialReceiptMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.MaterialReceipt}.
 */
@Service
@Transactional
public class MaterialReceiptServiceImpl implements MaterialReceiptService {

    private static final Logger log = LoggerFactory.getLogger(MaterialReceiptServiceImpl.class);

    private final MaterialReceiptRepository materialReceiptRepository;

    private final MaterialReceiptMapper materialReceiptMapper;

    public MaterialReceiptServiceImpl(MaterialReceiptRepository materialReceiptRepository, MaterialReceiptMapper materialReceiptMapper) {
        this.materialReceiptRepository = materialReceiptRepository;
        this.materialReceiptMapper = materialReceiptMapper;
    }

    @Override
    public MaterialReceiptDTO save(MaterialReceiptDTO materialReceiptDTO) {
        log.debug("Request to save MaterialReceipt : {}", materialReceiptDTO);
        MaterialReceipt materialReceipt = materialReceiptMapper.toEntity(materialReceiptDTO);
        materialReceipt = materialReceiptRepository.save(materialReceipt);
        return materialReceiptMapper.toDto(materialReceipt);
    }

    @Override
    public MaterialReceiptDTO update(MaterialReceiptDTO materialReceiptDTO) {
        log.debug("Request to update MaterialReceipt : {}", materialReceiptDTO);
        MaterialReceipt materialReceipt = materialReceiptMapper.toEntity(materialReceiptDTO);
        materialReceipt = materialReceiptRepository.save(materialReceipt);
        return materialReceiptMapper.toDto(materialReceipt);
    }

    @Override
    public Optional<MaterialReceiptDTO> partialUpdate(MaterialReceiptDTO materialReceiptDTO) {
        log.debug("Request to partially update MaterialReceipt : {}", materialReceiptDTO);

        return materialReceiptRepository
            .findById(materialReceiptDTO.getId())
            .map(existingMaterialReceipt -> {
                materialReceiptMapper.partialUpdate(existingMaterialReceipt, materialReceiptDTO);

                return existingMaterialReceipt;
            })
            .map(materialReceiptRepository::save)
            .map(materialReceiptMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MaterialReceiptDTO> findOne(Long id) {
        log.debug("Request to get MaterialReceipt : {}", id);
        return materialReceiptRepository.findById(id).map(materialReceiptMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MaterialReceipt : {}", id);
        materialReceiptRepository.deleteById(id);
    }
}
