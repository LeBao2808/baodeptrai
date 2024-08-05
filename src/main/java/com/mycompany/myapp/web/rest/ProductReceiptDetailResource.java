package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ProductReceiptDetailRepository;
import com.mycompany.myapp.service.ProductReceiptDetailQueryService;
import com.mycompany.myapp.service.ProductReceiptDetailService;
import com.mycompany.myapp.service.criteria.ProductReceiptDetailCriteria;
import com.mycompany.myapp.service.dto.ProductReceiptDetailDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ProductReceiptDetail}.
 */
@RestController
@RequestMapping("/api/product-receipt-details")
public class ProductReceiptDetailResource {

    private static final Logger log = LoggerFactory.getLogger(ProductReceiptDetailResource.class);

    private static final String ENTITY_NAME = "productReceiptDetail";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductReceiptDetailService productReceiptDetailService;

    private final ProductReceiptDetailRepository productReceiptDetailRepository;

    private final ProductReceiptDetailQueryService productReceiptDetailQueryService;

    public ProductReceiptDetailResource(
        ProductReceiptDetailService productReceiptDetailService,
        ProductReceiptDetailRepository productReceiptDetailRepository,
        ProductReceiptDetailQueryService productReceiptDetailQueryService
    ) {
        this.productReceiptDetailService = productReceiptDetailService;
        this.productReceiptDetailRepository = productReceiptDetailRepository;
        this.productReceiptDetailQueryService = productReceiptDetailQueryService;
    }

    /**
     * {@code POST  /product-receipt-details} : Create a new productReceiptDetail.
     *
     * @param productReceiptDetailDTO the productReceiptDetailDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productReceiptDetailDTO, or with status {@code 400 (Bad Request)} if the productReceiptDetail has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductReceiptDetailDTO> createProductReceiptDetail(@RequestBody ProductReceiptDetailDTO productReceiptDetailDTO)
        throws URISyntaxException {
        log.debug("REST request to save ProductReceiptDetail : {}", productReceiptDetailDTO);
        if (productReceiptDetailDTO.getId() != null) {
            throw new BadRequestAlertException("A new productReceiptDetail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productReceiptDetailDTO = productReceiptDetailService.save(productReceiptDetailDTO);
        return ResponseEntity.created(new URI("/api/product-receipt-details/" + productReceiptDetailDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, productReceiptDetailDTO.getId().toString()))
            .body(productReceiptDetailDTO);
    }

    /**
     * {@code PUT  /product-receipt-details/:id} : Updates an existing productReceiptDetail.
     *
     * @param id the id of the productReceiptDetailDTO to save.
     * @param productReceiptDetailDTO the productReceiptDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productReceiptDetailDTO,
     * or with status {@code 400 (Bad Request)} if the productReceiptDetailDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productReceiptDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductReceiptDetailDTO> updateProductReceiptDetail(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductReceiptDetailDTO productReceiptDetailDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProductReceiptDetail : {}, {}", id, productReceiptDetailDTO);
        if (productReceiptDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productReceiptDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productReceiptDetailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productReceiptDetailDTO = productReceiptDetailService.update(productReceiptDetailDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productReceiptDetailDTO.getId().toString()))
            .body(productReceiptDetailDTO);
    }

    /**
     * {@code PATCH  /product-receipt-details/:id} : Partial updates given fields of an existing productReceiptDetail, field will ignore if it is null
     *
     * @param id the id of the productReceiptDetailDTO to save.
     * @param productReceiptDetailDTO the productReceiptDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productReceiptDetailDTO,
     * or with status {@code 400 (Bad Request)} if the productReceiptDetailDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productReceiptDetailDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productReceiptDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductReceiptDetailDTO> partialUpdateProductReceiptDetail(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductReceiptDetailDTO productReceiptDetailDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductReceiptDetail partially : {}, {}", id, productReceiptDetailDTO);
        if (productReceiptDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productReceiptDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productReceiptDetailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductReceiptDetailDTO> result = productReceiptDetailService.partialUpdate(productReceiptDetailDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productReceiptDetailDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /product-receipt-details} : get all the productReceiptDetails.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productReceiptDetails in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductReceiptDetailDTO>> getAllProductReceiptDetails(
        ProductReceiptDetailCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ProductReceiptDetails by criteria: {}", criteria);

        Page<ProductReceiptDetailDTO> page = productReceiptDetailQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-receipt-details/count} : count all the productReceiptDetails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProductReceiptDetails(ProductReceiptDetailCriteria criteria) {
        log.debug("REST request to count ProductReceiptDetails by criteria: {}", criteria);
        return ResponseEntity.ok().body(productReceiptDetailQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-receipt-details/:id} : get the "id" productReceiptDetail.
     *
     * @param id the id of the productReceiptDetailDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productReceiptDetailDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductReceiptDetailDTO> getProductReceiptDetail(@PathVariable("id") Long id) {
        log.debug("REST request to get ProductReceiptDetail : {}", id);
        Optional<ProductReceiptDetailDTO> productReceiptDetailDTO = productReceiptDetailService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productReceiptDetailDTO);
    }

    /**
     * {@code DELETE  /product-receipt-details/:id} : delete the "id" productReceiptDetail.
     *
     * @param id the id of the productReceiptDetailDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductReceiptDetail(@PathVariable("id") Long id) {
        log.debug("REST request to delete ProductReceiptDetail : {}", id);
        productReceiptDetailService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
