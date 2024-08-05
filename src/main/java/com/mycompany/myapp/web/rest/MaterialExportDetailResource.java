package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.MaterialExportDetailRepository;
import com.mycompany.myapp.service.MaterialExportDetailQueryService;
import com.mycompany.myapp.service.MaterialExportDetailService;
import com.mycompany.myapp.service.criteria.MaterialExportDetailCriteria;
import com.mycompany.myapp.service.dto.MaterialExportDetailDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.MaterialExportDetail}.
 */
@RestController
@RequestMapping("/api/material-export-details")
public class MaterialExportDetailResource {

    private static final Logger log = LoggerFactory.getLogger(MaterialExportDetailResource.class);

    private static final String ENTITY_NAME = "materialExportDetail";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MaterialExportDetailService materialExportDetailService;

    private final MaterialExportDetailRepository materialExportDetailRepository;

    private final MaterialExportDetailQueryService materialExportDetailQueryService;

    public MaterialExportDetailResource(
        MaterialExportDetailService materialExportDetailService,
        MaterialExportDetailRepository materialExportDetailRepository,
        MaterialExportDetailQueryService materialExportDetailQueryService
    ) {
        this.materialExportDetailService = materialExportDetailService;
        this.materialExportDetailRepository = materialExportDetailRepository;
        this.materialExportDetailQueryService = materialExportDetailQueryService;
    }

    /**
     * {@code POST  /material-export-details} : Create a new materialExportDetail.
     *
     * @param materialExportDetailDTO the materialExportDetailDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new materialExportDetailDTO, or with status {@code 400 (Bad Request)} if the materialExportDetail has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MaterialExportDetailDTO> createMaterialExportDetail(@RequestBody MaterialExportDetailDTO materialExportDetailDTO)
        throws URISyntaxException {
        log.debug("REST request to save MaterialExportDetail : {}", materialExportDetailDTO);
        if (materialExportDetailDTO.getId() != null) {
            throw new BadRequestAlertException("A new materialExportDetail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        materialExportDetailDTO = materialExportDetailService.save(materialExportDetailDTO);
        return ResponseEntity.created(new URI("/api/material-export-details/" + materialExportDetailDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, materialExportDetailDTO.getId().toString()))
            .body(materialExportDetailDTO);
    }

    /**
     * {@code PUT  /material-export-details/:id} : Updates an existing materialExportDetail.
     *
     * @param id the id of the materialExportDetailDTO to save.
     * @param materialExportDetailDTO the materialExportDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated materialExportDetailDTO,
     * or with status {@code 400 (Bad Request)} if the materialExportDetailDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the materialExportDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MaterialExportDetailDTO> updateMaterialExportDetail(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MaterialExportDetailDTO materialExportDetailDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MaterialExportDetail : {}, {}", id, materialExportDetailDTO);
        if (materialExportDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, materialExportDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!materialExportDetailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        materialExportDetailDTO = materialExportDetailService.update(materialExportDetailDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, materialExportDetailDTO.getId().toString()))
            .body(materialExportDetailDTO);
    }

    /**
     * {@code PATCH  /material-export-details/:id} : Partial updates given fields of an existing materialExportDetail, field will ignore if it is null
     *
     * @param id the id of the materialExportDetailDTO to save.
     * @param materialExportDetailDTO the materialExportDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated materialExportDetailDTO,
     * or with status {@code 400 (Bad Request)} if the materialExportDetailDTO is not valid,
     * or with status {@code 404 (Not Found)} if the materialExportDetailDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the materialExportDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MaterialExportDetailDTO> partialUpdateMaterialExportDetail(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MaterialExportDetailDTO materialExportDetailDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MaterialExportDetail partially : {}, {}", id, materialExportDetailDTO);
        if (materialExportDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, materialExportDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!materialExportDetailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MaterialExportDetailDTO> result = materialExportDetailService.partialUpdate(materialExportDetailDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, materialExportDetailDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /material-export-details} : get all the materialExportDetails.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of materialExportDetails in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MaterialExportDetailDTO>> getAllMaterialExportDetails(
        MaterialExportDetailCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get MaterialExportDetails by criteria: {}", criteria);

        Page<MaterialExportDetailDTO> page = materialExportDetailQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /material-export-details/count} : count all the materialExportDetails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMaterialExportDetails(MaterialExportDetailCriteria criteria) {
        log.debug("REST request to count MaterialExportDetails by criteria: {}", criteria);
        return ResponseEntity.ok().body(materialExportDetailQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /material-export-details/:id} : get the "id" materialExportDetail.
     *
     * @param id the id of the materialExportDetailDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the materialExportDetailDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MaterialExportDetailDTO> getMaterialExportDetail(@PathVariable("id") Long id) {
        log.debug("REST request to get MaterialExportDetail : {}", id);
        Optional<MaterialExportDetailDTO> materialExportDetailDTO = materialExportDetailService.findOne(id);
        return ResponseUtil.wrapOrNotFound(materialExportDetailDTO);
    }

    /**
     * {@code DELETE  /material-export-details/:id} : delete the "id" materialExportDetail.
     *
     * @param id the id of the materialExportDetailDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaterialExportDetail(@PathVariable("id") Long id) {
        log.debug("REST request to delete MaterialExportDetail : {}", id);
        materialExportDetailService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
