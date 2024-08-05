package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.MaterialReceiptDetailRepository;
import com.mycompany.myapp.service.MaterialReceiptDetailQueryService;
import com.mycompany.myapp.service.MaterialReceiptDetailService;
import com.mycompany.myapp.service.criteria.MaterialReceiptDetailCriteria;
import com.mycompany.myapp.service.dto.MaterialReceiptDetailDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.MaterialReceiptDetail}.
 */
@RestController
@RequestMapping("/api/material-receipt-details")
public class MaterialReceiptDetailResource {

    private static final Logger log = LoggerFactory.getLogger(MaterialReceiptDetailResource.class);

    private static final String ENTITY_NAME = "materialReceiptDetail";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MaterialReceiptDetailService materialReceiptDetailService;

    private final MaterialReceiptDetailRepository materialReceiptDetailRepository;

    private final MaterialReceiptDetailQueryService materialReceiptDetailQueryService;

    public MaterialReceiptDetailResource(
        MaterialReceiptDetailService materialReceiptDetailService,
        MaterialReceiptDetailRepository materialReceiptDetailRepository,
        MaterialReceiptDetailQueryService materialReceiptDetailQueryService
    ) {
        this.materialReceiptDetailService = materialReceiptDetailService;
        this.materialReceiptDetailRepository = materialReceiptDetailRepository;
        this.materialReceiptDetailQueryService = materialReceiptDetailQueryService;
    }

    /**
     * {@code POST  /material-receipt-details} : Create a new materialReceiptDetail.
     *
     * @param materialReceiptDetailDTO the materialReceiptDetailDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new materialReceiptDetailDTO, or with status {@code 400 (Bad Request)} if the materialReceiptDetail has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MaterialReceiptDetailDTO> createMaterialReceiptDetail(
        @RequestBody MaterialReceiptDetailDTO materialReceiptDetailDTO
    ) throws URISyntaxException {
        log.debug("REST request to save MaterialReceiptDetail : {}", materialReceiptDetailDTO);
        if (materialReceiptDetailDTO.getId() != null) {
            throw new BadRequestAlertException("A new materialReceiptDetail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        materialReceiptDetailDTO = materialReceiptDetailService.save(materialReceiptDetailDTO);
        return ResponseEntity.created(new URI("/api/material-receipt-details/" + materialReceiptDetailDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, materialReceiptDetailDTO.getId().toString()))
            .body(materialReceiptDetailDTO);
    }

    /**
     * {@code PUT  /material-receipt-details/:id} : Updates an existing materialReceiptDetail.
     *
     * @param id the id of the materialReceiptDetailDTO to save.
     * @param materialReceiptDetailDTO the materialReceiptDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated materialReceiptDetailDTO,
     * or with status {@code 400 (Bad Request)} if the materialReceiptDetailDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the materialReceiptDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MaterialReceiptDetailDTO> updateMaterialReceiptDetail(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MaterialReceiptDetailDTO materialReceiptDetailDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MaterialReceiptDetail : {}, {}", id, materialReceiptDetailDTO);
        if (materialReceiptDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, materialReceiptDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!materialReceiptDetailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        materialReceiptDetailDTO = materialReceiptDetailService.update(materialReceiptDetailDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, materialReceiptDetailDTO.getId().toString()))
            .body(materialReceiptDetailDTO);
    }

    /**
     * {@code PATCH  /material-receipt-details/:id} : Partial updates given fields of an existing materialReceiptDetail, field will ignore if it is null
     *
     * @param id the id of the materialReceiptDetailDTO to save.
     * @param materialReceiptDetailDTO the materialReceiptDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated materialReceiptDetailDTO,
     * or with status {@code 400 (Bad Request)} if the materialReceiptDetailDTO is not valid,
     * or with status {@code 404 (Not Found)} if the materialReceiptDetailDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the materialReceiptDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MaterialReceiptDetailDTO> partialUpdateMaterialReceiptDetail(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MaterialReceiptDetailDTO materialReceiptDetailDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MaterialReceiptDetail partially : {}, {}", id, materialReceiptDetailDTO);
        if (materialReceiptDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, materialReceiptDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!materialReceiptDetailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MaterialReceiptDetailDTO> result = materialReceiptDetailService.partialUpdate(materialReceiptDetailDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, materialReceiptDetailDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /material-receipt-details} : get all the materialReceiptDetails.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of materialReceiptDetails in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MaterialReceiptDetailDTO>> getAllMaterialReceiptDetails(
        MaterialReceiptDetailCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get MaterialReceiptDetails by criteria: {}", criteria);

        Page<MaterialReceiptDetailDTO> page = materialReceiptDetailQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /material-receipt-details/count} : count all the materialReceiptDetails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMaterialReceiptDetails(MaterialReceiptDetailCriteria criteria) {
        log.debug("REST request to count MaterialReceiptDetails by criteria: {}", criteria);
        return ResponseEntity.ok().body(materialReceiptDetailQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /material-receipt-details/:id} : get the "id" materialReceiptDetail.
     *
     * @param id the id of the materialReceiptDetailDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the materialReceiptDetailDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MaterialReceiptDetailDTO> getMaterialReceiptDetail(@PathVariable("id") Long id) {
        log.debug("REST request to get MaterialReceiptDetail : {}", id);
        Optional<MaterialReceiptDetailDTO> materialReceiptDetailDTO = materialReceiptDetailService.findOne(id);
        return ResponseUtil.wrapOrNotFound(materialReceiptDetailDTO);
    }

    /**
     * {@code DELETE  /material-receipt-details/:id} : delete the "id" materialReceiptDetail.
     *
     * @param id the id of the materialReceiptDetailDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaterialReceiptDetail(@PathVariable("id") Long id) {
        log.debug("REST request to delete MaterialReceiptDetail : {}", id);
        materialReceiptDetailService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
