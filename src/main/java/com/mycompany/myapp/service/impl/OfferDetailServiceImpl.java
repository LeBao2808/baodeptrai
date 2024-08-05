package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.OfferDetail;
import com.mycompany.myapp.repository.OfferDetailRepository;
import com.mycompany.myapp.service.OfferDetailService;
import com.mycompany.myapp.service.dto.OfferDetailDTO;
import com.mycompany.myapp.service.mapper.OfferDetailMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.OfferDetail}.
 */
@Service
@Transactional
public class OfferDetailServiceImpl implements OfferDetailService {

    private static final Logger log = LoggerFactory.getLogger(OfferDetailServiceImpl.class);

    private final OfferDetailRepository offerDetailRepository;

    private final OfferDetailMapper offerDetailMapper;

    public OfferDetailServiceImpl(OfferDetailRepository offerDetailRepository, OfferDetailMapper offerDetailMapper) {
        this.offerDetailRepository = offerDetailRepository;
        this.offerDetailMapper = offerDetailMapper;
    }

    @Override
    public OfferDetailDTO save(OfferDetailDTO offerDetailDTO) {
        log.debug("Request to save OfferDetail : {}", offerDetailDTO);
        OfferDetail offerDetail = offerDetailMapper.toEntity(offerDetailDTO);
        offerDetail = offerDetailRepository.save(offerDetail);
        return offerDetailMapper.toDto(offerDetail);
    }

    @Override
    public OfferDetailDTO update(OfferDetailDTO offerDetailDTO) {
        log.debug("Request to update OfferDetail : {}", offerDetailDTO);
        OfferDetail offerDetail = offerDetailMapper.toEntity(offerDetailDTO);
        offerDetail = offerDetailRepository.save(offerDetail);
        return offerDetailMapper.toDto(offerDetail);
    }

    @Override
    public Optional<OfferDetailDTO> partialUpdate(OfferDetailDTO offerDetailDTO) {
        log.debug("Request to partially update OfferDetail : {}", offerDetailDTO);

        return offerDetailRepository
            .findById(offerDetailDTO.getId())
            .map(existingOfferDetail -> {
                offerDetailMapper.partialUpdate(existingOfferDetail, offerDetailDTO);

                return existingOfferDetail;
            })
            .map(offerDetailRepository::save)
            .map(offerDetailMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OfferDetailDTO> findOne(Long id) {
        log.debug("Request to get OfferDetail : {}", id);
        return offerDetailRepository.findById(id).map(offerDetailMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OfferDetail : {}", id);
        offerDetailRepository.deleteById(id);
    }
}
