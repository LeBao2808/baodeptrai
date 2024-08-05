package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.QuantificationRepository;
import com.mycompany.myapp.service.QuantificationQueryService;
import com.mycompany.myapp.service.QuantificationService;
import com.mycompany.myapp.service.criteria.QuantificationCriteria;
import com.mycompany.myapp.service.dto.QuantificationDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Quantification}.
 */
@RestController
@RequestMapping("/api/quantifications")
public class QuantificationResource {

    private static final Logger log = LoggerFactory.getLogger(QuantificationResource.class);

    private static final String ENTITY_NAME = "quantification";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuantificationService quantificationService;

    private final QuantificationRepository quantificationRepository;

    private final QuantificationQueryService quantificationQueryService;

    public QuantificationResource(
        QuantificationService quantificationService,
        QuantificationRepository quantificationRepository,
        QuantificationQueryService quantificationQueryService
    ) {
        this.quantificationService = quantificationService;
        this.quantificationRepository = quantificationRepository;
        this.quantificationQueryService = quantificationQueryService;
    }

    /**
     * {@code POST  /quantifications} : Create a new quantification.
     *
     * @param quantificationDTO the quantificationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quantificationDTO, or with status {@code 400 (Bad Request)} if the quantification has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<QuantificationDTO> createQuantification(@RequestBody QuantificationDTO quantificationDTO)
        throws URISyntaxException {
        log.debug("REST request to save Quantification : {}", quantificationDTO);
        if (quantificationDTO.getId() != null) {
            throw new BadRequestAlertException("A new quantification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        quantificationDTO = quantificationService.save(quantificationDTO);
        return ResponseEntity.created(new URI("/api/quantifications/" + quantificationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, quantificationDTO.getId().toString()))
            .body(quantificationDTO);
    }

    /**
     * {@code PUT  /quantifications/:id} : Updates an existing quantification.
     *
     * @param id the id of the quantificationDTO to save.
     * @param quantificationDTO the quantificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quantificationDTO,
     * or with status {@code 400 (Bad Request)} if the quantificationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the quantificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<QuantificationDTO> updateQuantification(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuantificationDTO quantificationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Quantification : {}, {}", id, quantificationDTO);
        if (quantificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quantificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quantificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        quantificationDTO = quantificationService.update(quantificationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, quantificationDTO.getId().toString()))
            .body(quantificationDTO);
    }

    /**
     * {@code PATCH  /quantifications/:id} : Partial updates given fields of an existing quantification, field will ignore if it is null
     *
     * @param id the id of the quantificationDTO to save.
     * @param quantificationDTO the quantificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quantificationDTO,
     * or with status {@code 400 (Bad Request)} if the quantificationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the quantificationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the quantificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuantificationDTO> partialUpdateQuantification(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuantificationDTO quantificationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Quantification partially : {}, {}", id, quantificationDTO);
        if (quantificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quantificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quantificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuantificationDTO> result = quantificationService.partialUpdate(quantificationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, quantificationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /quantifications} : get all the quantifications.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of quantifications in body.
     */
    @GetMapping("")
    public ResponseEntity<List<QuantificationDTO>> getAllQuantifications(
        QuantificationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Quantifications by criteria: {}", criteria);

        Page<QuantificationDTO> page = quantificationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /quantifications/count} : count all the quantifications.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countQuantifications(QuantificationCriteria criteria) {
        log.debug("REST request to count Quantifications by criteria: {}", criteria);
        return ResponseEntity.ok().body(quantificationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /quantifications/:id} : get the "id" quantification.
     *
     * @param id the id of the quantificationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the quantificationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuantificationDTO> getQuantification(@PathVariable("id") Long id) {
        log.debug("REST request to get Quantification : {}", id);
        Optional<QuantificationDTO> quantificationDTO = quantificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(quantificationDTO);
    }

    /**
     * {@code DELETE  /quantifications/:id} : delete the "id" quantification.
     *
     * @param id the id of the quantificationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuantification(@PathVariable("id") Long id) {
        log.debug("REST request to delete Quantification : {}", id);
        quantificationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
