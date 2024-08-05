package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ProductOrderDetailRepository;
import com.mycompany.myapp.service.ProductOrderDetailQueryService;
import com.mycompany.myapp.service.ProductOrderDetailService;
import com.mycompany.myapp.service.criteria.ProductOrderDetailCriteria;
import com.mycompany.myapp.service.dto.ProductOrderDetailDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ProductOrderDetail}.
 */
@RestController
@RequestMapping("/api/product-order-details")
public class ProductOrderDetailResource {

    private static final Logger log = LoggerFactory.getLogger(ProductOrderDetailResource.class);

    private static final String ENTITY_NAME = "productOrderDetail";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductOrderDetailService productOrderDetailService;

    private final ProductOrderDetailRepository productOrderDetailRepository;

    private final ProductOrderDetailQueryService productOrderDetailQueryService;

    public ProductOrderDetailResource(
        ProductOrderDetailService productOrderDetailService,
        ProductOrderDetailRepository productOrderDetailRepository,
        ProductOrderDetailQueryService productOrderDetailQueryService
    ) {
        this.productOrderDetailService = productOrderDetailService;
        this.productOrderDetailRepository = productOrderDetailRepository;
        this.productOrderDetailQueryService = productOrderDetailQueryService;
    }

    /**
     * {@code POST  /product-order-details} : Create a new productOrderDetail.
     *
     * @param productOrderDetailDTO the productOrderDetailDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productOrderDetailDTO, or with status {@code 400 (Bad Request)} if the productOrderDetail has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductOrderDetailDTO> createProductOrderDetail(@RequestBody ProductOrderDetailDTO productOrderDetailDTO)
        throws URISyntaxException {
        log.debug("REST request to save ProductOrderDetail : {}", productOrderDetailDTO);
        if (productOrderDetailDTO.getId() != null) {
            throw new BadRequestAlertException("A new productOrderDetail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productOrderDetailDTO = productOrderDetailService.save(productOrderDetailDTO);
        return ResponseEntity.created(new URI("/api/product-order-details/" + productOrderDetailDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, productOrderDetailDTO.getId().toString()))
            .body(productOrderDetailDTO);
    }

    /**
     * {@code PUT  /product-order-details/:id} : Updates an existing productOrderDetail.
     *
     * @param id the id of the productOrderDetailDTO to save.
     * @param productOrderDetailDTO the productOrderDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productOrderDetailDTO,
     * or with status {@code 400 (Bad Request)} if the productOrderDetailDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productOrderDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductOrderDetailDTO> updateProductOrderDetail(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductOrderDetailDTO productOrderDetailDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProductOrderDetail : {}, {}", id, productOrderDetailDTO);
        if (productOrderDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productOrderDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productOrderDetailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productOrderDetailDTO = productOrderDetailService.update(productOrderDetailDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productOrderDetailDTO.getId().toString()))
            .body(productOrderDetailDTO);
    }

    /**
     * {@code PATCH  /product-order-details/:id} : Partial updates given fields of an existing productOrderDetail, field will ignore if it is null
     *
     * @param id the id of the productOrderDetailDTO to save.
     * @param productOrderDetailDTO the productOrderDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productOrderDetailDTO,
     * or with status {@code 400 (Bad Request)} if the productOrderDetailDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productOrderDetailDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productOrderDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductOrderDetailDTO> partialUpdateProductOrderDetail(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductOrderDetailDTO productOrderDetailDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductOrderDetail partially : {}, {}", id, productOrderDetailDTO);
        if (productOrderDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productOrderDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productOrderDetailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductOrderDetailDTO> result = productOrderDetailService.partialUpdate(productOrderDetailDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productOrderDetailDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /product-order-details} : get all the productOrderDetails.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productOrderDetails in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductOrderDetailDTO>> getAllProductOrderDetails(
        ProductOrderDetailCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ProductOrderDetails by criteria: {}", criteria);

        Page<ProductOrderDetailDTO> page = productOrderDetailQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-order-details/count} : count all the productOrderDetails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProductOrderDetails(ProductOrderDetailCriteria criteria) {
        log.debug("REST request to count ProductOrderDetails by criteria: {}", criteria);
        return ResponseEntity.ok().body(productOrderDetailQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-order-details/:id} : get the "id" productOrderDetail.
     *
     * @param id the id of the productOrderDetailDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productOrderDetailDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductOrderDetailDTO> getProductOrderDetail(@PathVariable("id") Long id) {
        log.debug("REST request to get ProductOrderDetail : {}", id);
        Optional<ProductOrderDetailDTO> productOrderDetailDTO = productOrderDetailService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productOrderDetailDTO);
    }

    /**
     * {@code DELETE  /product-order-details/:id} : delete the "id" productOrderDetail.
     *
     * @param id the id of the productOrderDetailDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductOrderDetail(@PathVariable("id") Long id) {
        log.debug("REST request to delete ProductOrderDetail : {}", id);
        productOrderDetailService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
