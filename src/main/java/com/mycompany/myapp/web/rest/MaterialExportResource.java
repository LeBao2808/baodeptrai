package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.MaterialExportRepository;
import com.mycompany.myapp.service.MaterialExportQueryService;
import com.mycompany.myapp.service.MaterialExportService;
import com.mycompany.myapp.service.criteria.MaterialExportCriteria;
import com.mycompany.myapp.service.dto.MaterialExportDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.MaterialExport}.
 */
@RestController
@RequestMapping("/api/material-exports")
public class MaterialExportResource {

    private static final Logger log = LoggerFactory.getLogger(MaterialExportResource.class);

    private static final String ENTITY_NAME = "materialExport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MaterialExportService materialExportService;

    private final MaterialExportRepository materialExportRepository;

    private final MaterialExportQueryService materialExportQueryService;

    public MaterialExportResource(
        MaterialExportService materialExportService,
        MaterialExportRepository materialExportRepository,
        MaterialExportQueryService materialExportQueryService
    ) {
        this.materialExportService = materialExportService;
        this.materialExportRepository = materialExportRepository;
        this.materialExportQueryService = materialExportQueryService;
    }

    /**
     * {@code POST  /material-exports} : Create a new materialExport.
     *
     * @param materialExportDTO the materialExportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new materialExportDTO, or with status {@code 400 (Bad Request)} if the materialExport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MaterialExportDTO> createMaterialExport(@RequestBody MaterialExportDTO materialExportDTO)
        throws URISyntaxException {
        log.debug("REST request to save MaterialExport : {}", materialExportDTO);
        if (materialExportDTO.getId() != null) {
            throw new BadRequestAlertException("A new materialExport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        materialExportDTO = materialExportService.save(materialExportDTO);
        return ResponseEntity.created(new URI("/api/material-exports/" + materialExportDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, materialExportDTO.getId().toString()))
            .body(materialExportDTO);
    }

    /**
     * {@code PUT  /material-exports/:id} : Updates an existing materialExport.
     *
     * @param id the id of the materialExportDTO to save.
     * @param materialExportDTO the materialExportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated materialExportDTO,
     * or with status {@code 400 (Bad Request)} if the materialExportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the materialExportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MaterialExportDTO> updateMaterialExport(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MaterialExportDTO materialExportDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MaterialExport : {}, {}", id, materialExportDTO);
        if (materialExportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, materialExportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!materialExportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        materialExportDTO = materialExportService.update(materialExportDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, materialExportDTO.getId().toString()))
            .body(materialExportDTO);
    }

    /**
     * {@code PATCH  /material-exports/:id} : Partial updates given fields of an existing materialExport, field will ignore if it is null
     *
     * @param id the id of the materialExportDTO to save.
     * @param materialExportDTO the materialExportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated materialExportDTO,
     * or with status {@code 400 (Bad Request)} if the materialExportDTO is not valid,
     * or with status {@code 404 (Not Found)} if the materialExportDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the materialExportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MaterialExportDTO> partialUpdateMaterialExport(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MaterialExportDTO materialExportDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MaterialExport partially : {}, {}", id, materialExportDTO);
        if (materialExportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, materialExportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!materialExportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MaterialExportDTO> result = materialExportService.partialUpdate(materialExportDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, materialExportDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /material-exports} : get all the materialExports.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of materialExports in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MaterialExportDTO>> getAllMaterialExports(
        MaterialExportCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get MaterialExports by criteria: {}", criteria);

        Page<MaterialExportDTO> page = materialExportQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /material-exports/count} : count all the materialExports.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMaterialExports(MaterialExportCriteria criteria) {
        log.debug("REST request to count MaterialExports by criteria: {}", criteria);
        return ResponseEntity.ok().body(materialExportQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /material-exports/:id} : get the "id" materialExport.
     *
     * @param id the id of the materialExportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the materialExportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MaterialExportDTO> getMaterialExport(@PathVariable("id") Long id) {
        log.debug("REST request to get MaterialExport : {}", id);
        Optional<MaterialExportDTO> materialExportDTO = materialExportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(materialExportDTO);
    }

    /**
     * {@code DELETE  /material-exports/:id} : delete the "id" materialExport.
     *
     * @param id the id of the materialExportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaterialExport(@PathVariable("id") Long id) {
        log.debug("REST request to delete MaterialExport : {}", id);
        materialExportService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
