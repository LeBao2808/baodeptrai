package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ProductReceiptRepository;
import com.mycompany.myapp.service.ProductReceiptQueryService;
import com.mycompany.myapp.service.ProductReceiptService;
import com.mycompany.myapp.service.criteria.ProductReceiptCriteria;
import com.mycompany.myapp.service.dto.ProductReceiptDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ProductReceipt}.
 */
@RestController
@RequestMapping("/api/product-receipts")
public class ProductReceiptResource {

    private static final Logger log = LoggerFactory.getLogger(ProductReceiptResource.class);

    private static final String ENTITY_NAME = "productReceipt";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductReceiptService productReceiptService;

    private final ProductReceiptRepository productReceiptRepository;

    private final ProductReceiptQueryService productReceiptQueryService;

    public ProductReceiptResource(
        ProductReceiptService productReceiptService,
        ProductReceiptRepository productReceiptRepository,
        ProductReceiptQueryService productReceiptQueryService
    ) {
        this.productReceiptService = productReceiptService;
        this.productReceiptRepository = productReceiptRepository;
        this.productReceiptQueryService = productReceiptQueryService;
    }

    /**
     * {@code POST  /product-receipts} : Create a new productReceipt.
     *
     * @param productReceiptDTO the productReceiptDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productReceiptDTO, or with status {@code 400 (Bad Request)} if the productReceipt has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductReceiptDTO> createProductReceipt(@RequestBody ProductReceiptDTO productReceiptDTO)
        throws URISyntaxException {
        log.debug("REST request to save ProductReceipt : {}", productReceiptDTO);
        if (productReceiptDTO.getId() != null) {
            throw new BadRequestAlertException("A new productReceipt cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productReceiptDTO = productReceiptService.save(productReceiptDTO);
        return ResponseEntity.created(new URI("/api/product-receipts/" + productReceiptDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, productReceiptDTO.getId().toString()))
            .body(productReceiptDTO);
    }

    /**
     * {@code PUT  /product-receipts/:id} : Updates an existing productReceipt.
     *
     * @param id the id of the productReceiptDTO to save.
     * @param productReceiptDTO the productReceiptDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productReceiptDTO,
     * or with status {@code 400 (Bad Request)} if the productReceiptDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productReceiptDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductReceiptDTO> updateProductReceipt(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductReceiptDTO productReceiptDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProductReceipt : {}, {}", id, productReceiptDTO);
        if (productReceiptDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productReceiptDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productReceiptRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productReceiptDTO = productReceiptService.update(productReceiptDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productReceiptDTO.getId().toString()))
            .body(productReceiptDTO);
    }

    /**
     * {@code PATCH  /product-receipts/:id} : Partial updates given fields of an existing productReceipt, field will ignore if it is null
     *
     * @param id the id of the productReceiptDTO to save.
     * @param productReceiptDTO the productReceiptDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productReceiptDTO,
     * or with status {@code 400 (Bad Request)} if the productReceiptDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productReceiptDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productReceiptDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductReceiptDTO> partialUpdateProductReceipt(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductReceiptDTO productReceiptDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductReceipt partially : {}, {}", id, productReceiptDTO);
        if (productReceiptDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productReceiptDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productReceiptRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductReceiptDTO> result = productReceiptService.partialUpdate(productReceiptDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productReceiptDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /product-receipts} : get all the productReceipts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productReceipts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductReceiptDTO>> getAllProductReceipts(
        ProductReceiptCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ProductReceipts by criteria: {}", criteria);

        Page<ProductReceiptDTO> page = productReceiptQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-receipts/count} : count all the productReceipts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProductReceipts(ProductReceiptCriteria criteria) {
        log.debug("REST request to count ProductReceipts by criteria: {}", criteria);
        return ResponseEntity.ok().body(productReceiptQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-receipts/:id} : get the "id" productReceipt.
     *
     * @param id the id of the productReceiptDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productReceiptDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductReceiptDTO> getProductReceipt(@PathVariable("id") Long id) {
        log.debug("REST request to get ProductReceipt : {}", id);
        Optional<ProductReceiptDTO> productReceiptDTO = productReceiptService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productReceiptDTO);
    }

    /**
     * {@code DELETE  /product-receipts/:id} : delete the "id" productReceipt.
     *
     * @param id the id of the productReceiptDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductReceipt(@PathVariable("id") Long id) {
        log.debug("REST request to delete ProductReceipt : {}", id);
        productReceiptService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
