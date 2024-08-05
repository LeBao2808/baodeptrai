package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.MaterialInventoryRepository;
import com.mycompany.myapp.service.MaterialInventoryQueryService;
import com.mycompany.myapp.service.MaterialInventoryService;
import com.mycompany.myapp.service.criteria.MaterialInventoryCriteria;
import com.mycompany.myapp.service.dto.MaterialInventoryDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.MaterialInventory}.
 */
@RestController
@RequestMapping("/api/material-inventories")
public class MaterialInventoryResource {

    private static final Logger log = LoggerFactory.getLogger(MaterialInventoryResource.class);

    private static final String ENTITY_NAME = "materialInventory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MaterialInventoryService materialInventoryService;

    private final MaterialInventoryRepository materialInventoryRepository;

    private final MaterialInventoryQueryService materialInventoryQueryService;

    public MaterialInventoryResource(
        MaterialInventoryService materialInventoryService,
        MaterialInventoryRepository materialInventoryRepository,
        MaterialInventoryQueryService materialInventoryQueryService
    ) {
        this.materialInventoryService = materialInventoryService;
        this.materialInventoryRepository = materialInventoryRepository;
        this.materialInventoryQueryService = materialInventoryQueryService;
    }

    /**
     * {@code POST  /material-inventories} : Create a new materialInventory.
     *
     * @param materialInventoryDTO the materialInventoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new materialInventoryDTO, or with status {@code 400 (Bad Request)} if the materialInventory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MaterialInventoryDTO> createMaterialInventory(@RequestBody MaterialInventoryDTO materialInventoryDTO)
        throws URISyntaxException {
        log.debug("REST request to save MaterialInventory : {}", materialInventoryDTO);
        if (materialInventoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new materialInventory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        materialInventoryDTO = materialInventoryService.save(materialInventoryDTO);
        return ResponseEntity.created(new URI("/api/material-inventories/" + materialInventoryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, materialInventoryDTO.getId().toString()))
            .body(materialInventoryDTO);
    }

    /**
     * {@code PUT  /material-inventories/:id} : Updates an existing materialInventory.
     *
     * @param id the id of the materialInventoryDTO to save.
     * @param materialInventoryDTO the materialInventoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated materialInventoryDTO,
     * or with status {@code 400 (Bad Request)} if the materialInventoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the materialInventoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MaterialInventoryDTO> updateMaterialInventory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MaterialInventoryDTO materialInventoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MaterialInventory : {}, {}", id, materialInventoryDTO);
        if (materialInventoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, materialInventoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!materialInventoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        materialInventoryDTO = materialInventoryService.update(materialInventoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, materialInventoryDTO.getId().toString()))
            .body(materialInventoryDTO);
    }

    /**
     * {@code PATCH  /material-inventories/:id} : Partial updates given fields of an existing materialInventory, field will ignore if it is null
     *
     * @param id the id of the materialInventoryDTO to save.
     * @param materialInventoryDTO the materialInventoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated materialInventoryDTO,
     * or with status {@code 400 (Bad Request)} if the materialInventoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the materialInventoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the materialInventoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MaterialInventoryDTO> partialUpdateMaterialInventory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MaterialInventoryDTO materialInventoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MaterialInventory partially : {}, {}", id, materialInventoryDTO);
        if (materialInventoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, materialInventoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!materialInventoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MaterialInventoryDTO> result = materialInventoryService.partialUpdate(materialInventoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, materialInventoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /material-inventories} : get all the materialInventories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of materialInventories in body.
     */
    //    @GetMapping("")
    //    public ResponseEntity<List<MaterialInventoryDTO>> getAllMaterialInventories(
    //        MaterialInventoryCriteria criteria,
    //        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    //    ) {
    //        log.debug("REST request to get MaterialInventories by criteria: {}", criteria);
    //
    //        Page<MaterialInventoryDTO> page = materialInventoryQueryService.findByCriteria(criteria, pageable);
    //        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
    //        return ResponseEntity.ok().headers(headers).body(page.getContent());
    //    }

    /**
     * {@code GET  /material-inventories/count} : count all the materialInventories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMaterialInventories(MaterialInventoryCriteria criteria) {
        log.debug("REST request to count MaterialInventories by criteria: {}", criteria);
        return ResponseEntity.ok().body(materialInventoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /material-inventories/:id} : get the "id" materialInventory.
     *
     * @param id the id of the materialInventoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the materialInventoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MaterialInventoryDTO> getMaterialInventory(@PathVariable("id") Long id) {
        log.debug("REST request to get MaterialInventory : {}", id);
        Optional<MaterialInventoryDTO> materialInventoryDTO = materialInventoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(materialInventoryDTO);
    }

    /**
     * {@code DELETE  /material-inventories/:id} : delete the "id" materialInventory.
     *
     * @param id the id of the materialInventoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaterialInventory(@PathVariable("id") Long id) {
        log.debug("REST request to delete MaterialInventory : {}", id);
        materialInventoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
