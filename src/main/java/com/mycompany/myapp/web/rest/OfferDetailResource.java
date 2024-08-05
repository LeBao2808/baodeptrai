package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.OfferDetailRepository;
import com.mycompany.myapp.service.OfferDetailQueryService;
import com.mycompany.myapp.service.OfferDetailService;
import com.mycompany.myapp.service.criteria.OfferDetailCriteria;
import com.mycompany.myapp.service.dto.OfferDetailDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.OfferDetail}.
 */
@RestController
@RequestMapping("/api/offer-details")
public class OfferDetailResource {

    private static final Logger log = LoggerFactory.getLogger(OfferDetailResource.class);

    private static final String ENTITY_NAME = "offerDetail";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OfferDetailService offerDetailService;

    private final OfferDetailRepository offerDetailRepository;

    private final OfferDetailQueryService offerDetailQueryService;

    public OfferDetailResource(
        OfferDetailService offerDetailService,
        OfferDetailRepository offerDetailRepository,
        OfferDetailQueryService offerDetailQueryService
    ) {
        this.offerDetailService = offerDetailService;
        this.offerDetailRepository = offerDetailRepository;
        this.offerDetailQueryService = offerDetailQueryService;
    }

    /**
     * {@code POST  /offer-details} : Create a new offerDetail.
     *
     * @param offerDetailDTO the offerDetailDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new offerDetailDTO, or with status {@code 400 (Bad Request)} if the offerDetail has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OfferDetailDTO> createOfferDetail(@RequestBody OfferDetailDTO offerDetailDTO) throws URISyntaxException {
        log.debug("REST request to save OfferDetail : {}", offerDetailDTO);
        if (offerDetailDTO.getId() != null) {
            throw new BadRequestAlertException("A new offerDetail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        offerDetailDTO = offerDetailService.save(offerDetailDTO);
        return ResponseEntity.created(new URI("/api/offer-details/" + offerDetailDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, offerDetailDTO.getId().toString()))
            .body(offerDetailDTO);
    }

    /**
     * {@code PUT  /offer-details/:id} : Updates an existing offerDetail.
     *
     * @param id the id of the offerDetailDTO to save.
     * @param offerDetailDTO the offerDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated offerDetailDTO,
     * or with status {@code 400 (Bad Request)} if the offerDetailDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the offerDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OfferDetailDTO> updateOfferDetail(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OfferDetailDTO offerDetailDTO
    ) throws URISyntaxException {
        log.debug("REST request to update OfferDetail : {}, {}", id, offerDetailDTO);
        if (offerDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, offerDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!offerDetailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        offerDetailDTO = offerDetailService.update(offerDetailDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, offerDetailDTO.getId().toString()))
            .body(offerDetailDTO);
    }

    /**
     * {@code PATCH  /offer-details/:id} : Partial updates given fields of an existing offerDetail, field will ignore if it is null
     *
     * @param id the id of the offerDetailDTO to save.
     * @param offerDetailDTO the offerDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated offerDetailDTO,
     * or with status {@code 400 (Bad Request)} if the offerDetailDTO is not valid,
     * or with status {@code 404 (Not Found)} if the offerDetailDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the offerDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OfferDetailDTO> partialUpdateOfferDetail(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OfferDetailDTO offerDetailDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update OfferDetail partially : {}, {}", id, offerDetailDTO);
        if (offerDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, offerDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!offerDetailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OfferDetailDTO> result = offerDetailService.partialUpdate(offerDetailDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, offerDetailDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /offer-details} : get all the offerDetails.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of offerDetails in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OfferDetailDTO>> getAllOfferDetails(
        OfferDetailCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get OfferDetails by criteria: {}", criteria);

        Page<OfferDetailDTO> page = offerDetailQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /offer-details/count} : count all the offerDetails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOfferDetails(OfferDetailCriteria criteria) {
        log.debug("REST request to count OfferDetails by criteria: {}", criteria);
        return ResponseEntity.ok().body(offerDetailQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /offer-details/:id} : get the "id" offerDetail.
     *
     * @param id the id of the offerDetailDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the offerDetailDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OfferDetailDTO> getOfferDetail(@PathVariable("id") Long id) {
        log.debug("REST request to get OfferDetail : {}", id);
        Optional<OfferDetailDTO> offerDetailDTO = offerDetailService.findOne(id);
        return ResponseUtil.wrapOrNotFound(offerDetailDTO);
    }

    /**
     * {@code DELETE  /offer-details/:id} : delete the "id" offerDetail.
     *
     * @param id the id of the offerDetailDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOfferDetail(@PathVariable("id") Long id) {
        log.debug("REST request to delete OfferDetail : {}", id);
        offerDetailService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
