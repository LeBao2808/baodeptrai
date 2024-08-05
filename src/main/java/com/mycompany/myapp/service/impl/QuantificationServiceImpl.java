package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Quantification;
import com.mycompany.myapp.repository.QuantificationRepository;
import com.mycompany.myapp.service.QuantificationService;
import com.mycompany.myapp.service.dto.QuantificationDTO;
import com.mycompany.myapp.service.mapper.QuantificationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Quantification}.
 */
@Service
@Transactional
public class QuantificationServiceImpl implements QuantificationService {

    private static final Logger log = LoggerFactory.getLogger(QuantificationServiceImpl.class);

    private final QuantificationRepository quantificationRepository;

    private final QuantificationMapper quantificationMapper;

    public QuantificationServiceImpl(QuantificationRepository quantificationRepository, QuantificationMapper quantificationMapper) {
        this.quantificationRepository = quantificationRepository;
        this.quantificationMapper = quantificationMapper;
    }

    @Override
    public QuantificationDTO save(QuantificationDTO quantificationDTO) {
        log.debug("Request to save Quantification : {}", quantificationDTO);
        Quantification quantification = quantificationMapper.toEntity(quantificationDTO);
        quantification = quantificationRepository.save(quantification);
        return quantificationMapper.toDto(quantification);
    }

    @Override
    public QuantificationDTO update(QuantificationDTO quantificationDTO) {
        log.debug("Request to update Quantification : {}", quantificationDTO);
        Quantification quantification = quantificationMapper.toEntity(quantificationDTO);
        quantification = quantificationRepository.save(quantification);
        return quantificationMapper.toDto(quantification);
    }

    @Override
    public Optional<QuantificationDTO> partialUpdate(QuantificationDTO quantificationDTO) {
        log.debug("Request to partially update Quantification : {}", quantificationDTO);

        return quantificationRepository
            .findById(quantificationDTO.getId())
            .map(existingQuantification -> {
                quantificationMapper.partialUpdate(existingQuantification, quantificationDTO);

                return existingQuantification;
            })
            .map(quantificationRepository::save)
            .map(quantificationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QuantificationDTO> findOne(Long id) {
        log.debug("Request to get Quantification : {}", id);
        return quantificationRepository.findById(id).map(quantificationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Quantification : {}", id);
        quantificationRepository.deleteById(id);
    }
}
