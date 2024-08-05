package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.MaterialExport;
import com.mycompany.myapp.repository.MaterialExportRepository;
import com.mycompany.myapp.service.MaterialExportService;
import com.mycompany.myapp.service.dto.MaterialExportDTO;
import com.mycompany.myapp.service.mapper.MaterialExportMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.MaterialExport}.
 */
@Service
@Transactional
public class MaterialExportServiceImpl implements MaterialExportService {

    private static final Logger log = LoggerFactory.getLogger(MaterialExportServiceImpl.class);

    private final MaterialExportRepository materialExportRepository;

    private final MaterialExportMapper materialExportMapper;

    public MaterialExportServiceImpl(MaterialExportRepository materialExportRepository, MaterialExportMapper materialExportMapper) {
        this.materialExportRepository = materialExportRepository;
        this.materialExportMapper = materialExportMapper;
    }

    @Override
    public MaterialExportDTO save(MaterialExportDTO materialExportDTO) {
        log.debug("Request to save MaterialExport : {}", materialExportDTO);
        MaterialExport materialExport = materialExportMapper.toEntity(materialExportDTO);
        materialExport = materialExportRepository.save(materialExport);
        return materialExportMapper.toDto(materialExport);
    }

    @Override
    public MaterialExportDTO update(MaterialExportDTO materialExportDTO) {
        log.debug("Request to update MaterialExport : {}", materialExportDTO);
        MaterialExport materialExport = materialExportMapper.toEntity(materialExportDTO);
        materialExport = materialExportRepository.save(materialExport);
        return materialExportMapper.toDto(materialExport);
    }

    @Override
    public Optional<MaterialExportDTO> partialUpdate(MaterialExportDTO materialExportDTO) {
        log.debug("Request to partially update MaterialExport : {}", materialExportDTO);

        return materialExportRepository
            .findById(materialExportDTO.getId())
            .map(existingMaterialExport -> {
                materialExportMapper.partialUpdate(existingMaterialExport, materialExportDTO);

                return existingMaterialExport;
            })
            .map(materialExportRepository::save)
            .map(materialExportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MaterialExportDTO> findOne(Long id) {
        log.debug("Request to get MaterialExport : {}", id);
        return materialExportRepository.findById(id).map(materialExportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MaterialExport : {}", id);
        materialExportRepository.deleteById(id);
    }
}
