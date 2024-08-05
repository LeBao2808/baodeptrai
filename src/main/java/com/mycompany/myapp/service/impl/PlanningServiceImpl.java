package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Planning;
import com.mycompany.myapp.repository.PlanningRepository;
import com.mycompany.myapp.service.PlanningService;
import com.mycompany.myapp.service.dto.PlanningDTO;
import com.mycompany.myapp.service.mapper.PlanningMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Planning}.
 */
@Service
@Transactional
public class PlanningServiceImpl implements PlanningService {

    private static final Logger log = LoggerFactory.getLogger(PlanningServiceImpl.class);

    private final PlanningRepository planningRepository;

    private final PlanningMapper planningMapper;

    public PlanningServiceImpl(PlanningRepository planningRepository, PlanningMapper planningMapper) {
        this.planningRepository = planningRepository;
        this.planningMapper = planningMapper;
    }

    @Override
    public PlanningDTO save(PlanningDTO planningDTO) {
        log.debug("Request to save Planning : {}", planningDTO);
        Planning planning = planningMapper.toEntity(planningDTO);
        planning = planningRepository.save(planning);
        return planningMapper.toDto(planning);
    }

    @Override
    public PlanningDTO update(PlanningDTO planningDTO) {
        log.debug("Request to update Planning : {}", planningDTO);
        Planning planning = planningMapper.toEntity(planningDTO);
        planning = planningRepository.save(planning);
        return planningMapper.toDto(planning);
    }

    @Override
    public Optional<PlanningDTO> partialUpdate(PlanningDTO planningDTO) {
        log.debug("Request to partially update Planning : {}", planningDTO);

        return planningRepository
            .findById(planningDTO.getId())
            .map(existingPlanning -> {
                planningMapper.partialUpdate(existingPlanning, planningDTO);

                return existingPlanning;
            })
            .map(planningRepository::save)
            .map(planningMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PlanningDTO> findOne(Long id) {
        log.debug("Request to get Planning : {}", id);
        return planningRepository.findById(id).map(planningMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Planning : {}", id);
        planningRepository.deleteById(id);
    }
}
