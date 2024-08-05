package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.MaterialReceiptRepository;
import com.mycompany.myapp.service.MaterialReceiptQueryService;
import com.mycompany.myapp.service.MaterialReceiptService;
import com.mycompany.myapp.service.criteria.MaterialReceiptCriteria;
import com.mycompany.myapp.service.dto.MaterialReceiptDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.MaterialReceipt}.
 */
@RestController
@RequestMapping("/api/material-receipts")
public class MaterialReceiptResource {

    private static final Logger log = LoggerFactory.getLogger(MaterialReceiptResource.class);

    private static final String ENTITY_NAME = "materialReceipt";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MaterialReceiptService materialReceiptService;

    private final MaterialReceiptRepository materialReceiptRepository;

    private final MaterialReceiptQueryService materialReceiptQueryService;

    public MaterialReceiptResource(
        MaterialReceiptService materialReceiptService,
        MaterialReceiptRepository materialReceiptRepository,
        MaterialReceiptQueryService materialReceiptQueryService
    ) {
        this.materialReceiptService = materialReceiptService;
        this.materialReceiptRepository = materialReceiptRepository;
        this.materialReceiptQueryService = materialReceiptQueryService;
    }

    /**
     * {@code POST  /material-receipts} : Create a new materialReceipt.
     *
     * @param materialReceiptDTO the materialReceiptDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new materialReceiptDTO, or with status {@code 400 (Bad Request)} if the materialReceipt has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MaterialReceiptDTO> createMaterialReceipt(@RequestBody MaterialReceiptDTO materialReceiptDTO)
        throws URISyntaxException {
        log.debug("REST request to save MaterialReceipt : {}", materialReceiptDTO);
        if (materialReceiptDTO.getId() != null) {
            throw new BadRequestAlertException("A new materialReceipt cannot already have an ID", ENTITY_NAME, "idexists");
        }
        materialReceiptDTO = materialReceiptService.save(materialReceiptDTO);
        return ResponseEntity.created(new URI("/api/material-receipts/" + materialReceiptDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, materialReceiptDTO.getId().toString()))
            .body(materialReceiptDTO);
    }

    /**
     * {@code PUT  /material-receipts/:id} : Updates an existing materialReceipt.
     *
     * @param id the id of the materialReceiptDTO to save.
     * @param materialReceiptDTO the materialReceiptDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated materialReceiptDTO,
     * or with status {@code 400 (Bad Request)} if the materialReceiptDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the materialReceiptDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MaterialReceiptDTO> updateMaterialReceipt(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MaterialReceiptDTO materialReceiptDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MaterialReceipt : {}, {}", id, materialReceiptDTO);
        if (materialReceiptDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, materialReceiptDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!materialReceiptRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        materialReceiptDTO = materialReceiptService.update(materialReceiptDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, materialReceiptDTO.getId().toString()))
            .body(materialReceiptDTO);
    }

    /**
     * {@code PATCH  /material-receipts/:id} : Partial updates given fields of an existing materialReceipt, field will ignore if it is null
     *
     * @param id the id of the materialReceiptDTO to save.
     * @param materialReceiptDTO the materialReceiptDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated materialReceiptDTO,
     * or with status {@code 400 (Bad Request)} if the materialReceiptDTO is not valid,
     * or with status {@code 404 (Not Found)} if the materialReceiptDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the materialReceiptDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MaterialReceiptDTO> partialUpdateMaterialReceipt(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MaterialReceiptDTO materialReceiptDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MaterialReceipt partially : {}, {}", id, materialReceiptDTO);
        if (materialReceiptDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, materialReceiptDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!materialReceiptRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MaterialReceiptDTO> result = materialReceiptService.partialUpdate(materialReceiptDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, materialReceiptDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /material-receipts} : get all the materialReceipts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of materialReceipts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MaterialReceiptDTO>> getAllMaterialReceipts(
        MaterialReceiptCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get MaterialReceipts by criteria: {}", criteria);

        Page<MaterialReceiptDTO> page = materialReceiptQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /material-receipts/count} : count all the materialReceipts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMaterialReceipts(MaterialReceiptCriteria criteria) {
        log.debug("REST request to count MaterialReceipts by criteria: {}", criteria);
        return ResponseEntity.ok().body(materialReceiptQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /material-receipts/:id} : get the "id" materialReceipt.
     *
     * @param id the id of the materialReceiptDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the materialReceiptDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MaterialReceiptDTO> getMaterialReceipt(@PathVariable("id") Long id) {
        log.debug("REST request to get MaterialReceipt : {}", id);
        Optional<MaterialReceiptDTO> materialReceiptDTO = materialReceiptService.findOne(id);
        return ResponseUtil.wrapOrNotFound(materialReceiptDTO);
    }

    /**
     * {@code DELETE  /material-receipts/:id} : delete the "id" materialReceipt.
     *
     * @param id the id of the materialReceiptDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaterialReceipt(@PathVariable("id") Long id) {
        log.debug("REST request to delete MaterialReceipt : {}", id);
        materialReceiptService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
