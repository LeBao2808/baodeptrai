package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.PlanningRepository;
import com.mycompany.myapp.service.PlanningQueryService;
import com.mycompany.myapp.service.PlanningService;
import com.mycompany.myapp.service.criteria.PlanningCriteria;
import com.mycompany.myapp.service.dto.PlanningDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Planning}.
 */
@RestController
@RequestMapping("/api/plannings")
public class PlanningResource {

    private static final Logger log = LoggerFactory.getLogger(PlanningResource.class);

    private static final String ENTITY_NAME = "planning";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlanningService planningService;

    private final PlanningRepository planningRepository;

    private final PlanningQueryService planningQueryService;

    public PlanningResource(
        PlanningService planningService,
        PlanningRepository planningRepository,
        PlanningQueryService planningQueryService
    ) {
        this.planningService = planningService;
        this.planningRepository = planningRepository;
        this.planningQueryService = planningQueryService;
    }

    /**
     * {@code POST  /plannings} : Create a new planning.
     *
     * @param planningDTO the planningDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new planningDTO, or with status {@code 400 (Bad Request)} if the planning has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PlanningDTO> createPlanning(@RequestBody PlanningDTO planningDTO) throws URISyntaxException {
        log.debug("REST request to save Planning : {}", planningDTO);
        if (planningDTO.getId() != null) {
            throw new BadRequestAlertException("A new planning cannot already have an ID", ENTITY_NAME, "idexists");
        }
        planningDTO = planningService.save(planningDTO);
        return ResponseEntity.created(new URI("/api/plannings/" + planningDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, planningDTO.getId().toString()))
            .body(planningDTO);
    }

    /**
     * {@code PUT  /plannings/:id} : Updates an existing planning.
     *
     * @param id the id of the planningDTO to save.
     * @param planningDTO the planningDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planningDTO,
     * or with status {@code 400 (Bad Request)} if the planningDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the planningDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PlanningDTO> updatePlanning(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PlanningDTO planningDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Planning : {}, {}", id, planningDTO);
        if (planningDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, planningDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!planningRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        planningDTO = planningService.update(planningDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, planningDTO.getId().toString()))
            .body(planningDTO);
    }

    /**
     * {@code PATCH  /plannings/:id} : Partial updates given fields of an existing planning, field will ignore if it is null
     *
     * @param id the id of the planningDTO to save.
     * @param planningDTO the planningDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planningDTO,
     * or with status {@code 400 (Bad Request)} if the planningDTO is not valid,
     * or with status {@code 404 (Not Found)} if the planningDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the planningDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PlanningDTO> partialUpdatePlanning(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PlanningDTO planningDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Planning partially : {}, {}", id, planningDTO);
        if (planningDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, planningDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!planningRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PlanningDTO> result = planningService.partialUpdate(planningDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, planningDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /plannings} : get all the plannings.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of plannings in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PlanningDTO>> getAllPlannings(
        PlanningCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Plannings by criteria: {}", criteria);

        Page<PlanningDTO> page = planningQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /plannings/count} : count all the plannings.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPlannings(PlanningCriteria criteria) {
        log.debug("REST request to count Plannings by criteria: {}", criteria);
        return ResponseEntity.ok().body(planningQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /plannings/:id} : get the "id" planning.
     *
     * @param id the id of the planningDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the planningDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlanningDTO> getPlanning(@PathVariable("id") Long id) {
        log.debug("REST request to get Planning : {}", id);
        Optional<PlanningDTO> planningDTO = planningService.findOne(id);
        return ResponseUtil.wrapOrNotFound(planningDTO);
    }

    /**
     * {@code DELETE  /plannings/:id} : delete the "id" planning.
     *
     * @param id the id of the planningDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlanning(@PathVariable("id") Long id) {
        log.debug("REST request to delete Planning : {}", id);
        planningService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
