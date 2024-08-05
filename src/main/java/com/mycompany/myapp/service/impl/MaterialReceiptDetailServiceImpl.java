package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.MaterialReceiptDetail;
import com.mycompany.myapp.repository.MaterialReceiptDetailRepository;
import com.mycompany.myapp.service.MaterialReceiptDetailService;
import com.mycompany.myapp.service.dto.MaterialReceiptDetailDTO;
import com.mycompany.myapp.service.mapper.MaterialReceiptDetailMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.MaterialReceiptDetail}.
 */
@Service
@Transactional
public class MaterialReceiptDetailServiceImpl implements MaterialReceiptDetailService {

    private static final Logger log = LoggerFactory.getLogger(MaterialReceiptDetailServiceImpl.class);

    private final MaterialReceiptDetailRepository materialReceiptDetailRepository;

    private final MaterialReceiptDetailMapper materialReceiptDetailMapper;

    public MaterialReceiptDetailServiceImpl(
        MaterialReceiptDetailRepository materialReceiptDetailRepository,
        MaterialReceiptDetailMapper materialReceiptDetailMapper
    ) {
        this.materialReceiptDetailRepository = materialReceiptDetailRepository;
        this.materialReceiptDetailMapper = materialReceiptDetailMapper;
    }

    @Override
    public MaterialReceiptDetailDTO save(MaterialReceiptDetailDTO materialReceiptDetailDTO) {
        log.debug("Request to save MaterialReceiptDetail : {}", materialReceiptDetailDTO);
        MaterialReceiptDetail materialReceiptDetail = materialReceiptDetailMapper.toEntity(materialReceiptDetailDTO);
        materialReceiptDetail = materialReceiptDetailRepository.save(materialReceiptDetail);
        return materialReceiptDetailMapper.toDto(materialReceiptDetail);
    }

    @Override
    public MaterialReceiptDetailDTO update(MaterialReceiptDetailDTO materialReceiptDetailDTO) {
        log.debug("Request to update MaterialReceiptDetail : {}", materialReceiptDetailDTO);
        MaterialReceiptDetail materialReceiptDetail = materialReceiptDetailMapper.toEntity(materialReceiptDetailDTO);
        materialReceiptDetail = materialReceiptDetailRepository.save(materialReceiptDetail);
        return materialReceiptDetailMapper.toDto(materialReceiptDetail);
    }

    @Override
    public Optional<MaterialReceiptDetailDTO> partialUpdate(MaterialReceiptDetailDTO materialReceiptDetailDTO) {
        log.debug("Request to partially update MaterialReceiptDetail : {}", materialReceiptDetailDTO);

        return materialReceiptDetailRepository
            .findById(materialReceiptDetailDTO.getId())
            .map(existingMaterialReceiptDetail -> {
                materialReceiptDetailMapper.partialUpdate(existingMaterialReceiptDetail, materialReceiptDetailDTO);

                return existingMaterialReceiptDetail;
            })
            .map(materialReceiptDetailRepository::save)
            .map(materialReceiptDetailMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MaterialReceiptDetailDTO> findOne(Long id) {
        log.debug("Request to get MaterialReceiptDetail : {}", id);
        return materialReceiptDetailRepository.findById(id).map(materialReceiptDetailMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MaterialReceiptDetail : {}", id);
        materialReceiptDetailRepository.deleteById(id);
    }
}
