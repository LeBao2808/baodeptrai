package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.MaterialExportDetail;
import com.mycompany.myapp.repository.MaterialExportDetailRepository;
import com.mycompany.myapp.service.MaterialExportDetailService;
import com.mycompany.myapp.service.dto.MaterialExportDetailDTO;
import com.mycompany.myapp.service.mapper.MaterialExportDetailMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.MaterialExportDetail}.
 */
@Service
@Transactional
public class MaterialExportDetailServiceImpl implements MaterialExportDetailService {

    private static final Logger log = LoggerFactory.getLogger(MaterialExportDetailServiceImpl.class);

    private final MaterialExportDetailRepository materialExportDetailRepository;

    private final MaterialExportDetailMapper materialExportDetailMapper;

    public MaterialExportDetailServiceImpl(
        MaterialExportDetailRepository materialExportDetailRepository,
        MaterialExportDetailMapper materialExportDetailMapper
    ) {
        this.materialExportDetailRepository = materialExportDetailRepository;
        this.materialExportDetailMapper = materialExportDetailMapper;
    }

    @Override
    public MaterialExportDetailDTO save(MaterialExportDetailDTO materialExportDetailDTO) {
        log.debug("Request to save MaterialExportDetail : {}", materialExportDetailDTO);
        MaterialExportDetail materialExportDetail = materialExportDetailMapper.toEntity(materialExportDetailDTO);
        materialExportDetail = materialExportDetailRepository.save(materialExportDetail);
        return materialExportDetailMapper.toDto(materialExportDetail);
    }

    @Override
    public MaterialExportDetailDTO update(MaterialExportDetailDTO materialExportDetailDTO) {
        log.debug("Request to update MaterialExportDetail : {}", materialExportDetailDTO);
        MaterialExportDetail materialExportDetail = materialExportDetailMapper.toEntity(materialExportDetailDTO);
        materialExportDetail = materialExportDetailRepository.save(materialExportDetail);
        return materialExportDetailMapper.toDto(materialExportDetail);
    }

    @Override
    public Optional<MaterialExportDetailDTO> partialUpdate(MaterialExportDetailDTO materialExportDetailDTO) {
        log.debug("Request to partially update MaterialExportDetail : {}", materialExportDetailDTO);

        return materialExportDetailRepository
            .findById(materialExportDetailDTO.getId())
            .map(existingMaterialExportDetail -> {
                materialExportDetailMapper.partialUpdate(existingMaterialExportDetail, materialExportDetailDTO);

                return existingMaterialExportDetail;
            })
            .map(materialExportDetailRepository::save)
            .map(materialExportDetailMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MaterialExportDetailDTO> findOne(Long id) {
        log.debug("Request to get MaterialExportDetail : {}", id);
        return materialExportDetailRepository.findById(id).map(materialExportDetailMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MaterialExportDetail : {}", id);
        materialExportDetailRepository.deleteById(id);
    }
}
