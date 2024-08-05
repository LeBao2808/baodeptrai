package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ProductReceiptDetailAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.domain.ProductReceipt;
import com.mycompany.myapp.domain.ProductReceiptDetail;
import com.mycompany.myapp.repository.ProductReceiptDetailRepository;
import com.mycompany.myapp.service.dto.ProductReceiptDetailDTO;
import com.mycompany.myapp.service.mapper.ProductReceiptDetailMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProductReceiptDetailResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductReceiptDetailResourceIT {

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;
    private static final Integer SMALLER_QUANTITY = 1 - 1;

    private static final String ENTITY_API_URL = "/api/product-receipt-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductReceiptDetailRepository productReceiptDetailRepository;

    @Autowired
    private ProductReceiptDetailMapper productReceiptDetailMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductReceiptDetailMockMvc;

    private ProductReceiptDetail productReceiptDetail;

    private ProductReceiptDetail insertedProductReceiptDetail;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductReceiptDetail createEntity(EntityManager em) {
        ProductReceiptDetail productReceiptDetail = new ProductReceiptDetail().note(DEFAULT_NOTE).quantity(DEFAULT_QUANTITY);
        return productReceiptDetail;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductReceiptDetail createUpdatedEntity(EntityManager em) {
        ProductReceiptDetail productReceiptDetail = new ProductReceiptDetail().note(UPDATED_NOTE).quantity(UPDATED_QUANTITY);
        return productReceiptDetail;
    }

    @BeforeEach
    public void initTest() {
        productReceiptDetail = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductReceiptDetail != null) {
            productReceiptDetailRepository.delete(insertedProductReceiptDetail);
            insertedProductReceiptDetail = null;
        }
    }

    @Test
    @Transactional
    void createProductReceiptDetail() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductReceiptDetail
        ProductReceiptDetailDTO productReceiptDetailDTO = productReceiptDetailMapper.toDto(productReceiptDetail);
        var returnedProductReceiptDetailDTO = om.readValue(
            restProductReceiptDetailMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productReceiptDetailDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductReceiptDetailDTO.class
        );

        // Validate the ProductReceiptDetail in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProductReceiptDetail = productReceiptDetailMapper.toEntity(returnedProductReceiptDetailDTO);
        assertProductReceiptDetailUpdatableFieldsEquals(
            returnedProductReceiptDetail,
            getPersistedProductReceiptDetail(returnedProductReceiptDetail)
        );

        insertedProductReceiptDetail = returnedProductReceiptDetail;
    }

    @Test
    @Transactional
    void createProductReceiptDetailWithExistingId() throws Exception {
        // Create the ProductReceiptDetail with an existing ID
        productReceiptDetail.setId(1L);
        ProductReceiptDetailDTO productReceiptDetailDTO = productReceiptDetailMapper.toDto(productReceiptDetail);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductReceiptDetailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productReceiptDetailDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductReceiptDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductReceiptDetails() throws Exception {
        // Initialize the database
        insertedProductReceiptDetail = productReceiptDetailRepository.saveAndFlush(productReceiptDetail);

        // Get all the productReceiptDetailList
        restProductReceiptDetailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productReceiptDetail.getId().intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }

    @Test
    @Transactional
    void getProductReceiptDetail() throws Exception {
        // Initialize the database
        insertedProductReceiptDetail = productReceiptDetailRepository.saveAndFlush(productReceiptDetail);

        // Get the productReceiptDetail
        restProductReceiptDetailMockMvc
            .perform(get(ENTITY_API_URL_ID, productReceiptDetail.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productReceiptDetail.getId().intValue()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
    }

    @Test
    @Transactional
    void getProductReceiptDetailsByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductReceiptDetail = productReceiptDetailRepository.saveAndFlush(productReceiptDetail);

        Long id = productReceiptDetail.getId();

        defaultProductReceiptDetailFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductReceiptDetailFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductReceiptDetailFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductReceiptDetailsByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductReceiptDetail = productReceiptDetailRepository.saveAndFlush(productReceiptDetail);

        // Get all the productReceiptDetailList where note equals to
        defaultProductReceiptDetailFiltering("note.equals=" + DEFAULT_NOTE, "note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllProductReceiptDetailsByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductReceiptDetail = productReceiptDetailRepository.saveAndFlush(productReceiptDetail);

        // Get all the productReceiptDetailList where note in
        defaultProductReceiptDetailFiltering("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE, "note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllProductReceiptDetailsByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductReceiptDetail = productReceiptDetailRepository.saveAndFlush(productReceiptDetail);

        // Get all the productReceiptDetailList where note is not null
        defaultProductReceiptDetailFiltering("note.specified=true", "note.specified=false");
    }

    @Test
    @Transactional
    void getAllProductReceiptDetailsByNoteContainsSomething() throws Exception {
        // Initialize the database
        insertedProductReceiptDetail = productReceiptDetailRepository.saveAndFlush(productReceiptDetail);

        // Get all the productReceiptDetailList where note contains
        defaultProductReceiptDetailFiltering("note.contains=" + DEFAULT_NOTE, "note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllProductReceiptDetailsByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductReceiptDetail = productReceiptDetailRepository.saveAndFlush(productReceiptDetail);

        // Get all the productReceiptDetailList where note does not contain
        defaultProductReceiptDetailFiltering("note.doesNotContain=" + UPDATED_NOTE, "note.doesNotContain=" + DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void getAllProductReceiptDetailsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductReceiptDetail = productReceiptDetailRepository.saveAndFlush(productReceiptDetail);

        // Get all the productReceiptDetailList where quantity equals to
        defaultProductReceiptDetailFiltering("quantity.equals=" + DEFAULT_QUANTITY, "quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductReceiptDetailsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductReceiptDetail = productReceiptDetailRepository.saveAndFlush(productReceiptDetail);

        // Get all the productReceiptDetailList where quantity in
        defaultProductReceiptDetailFiltering("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY, "quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductReceiptDetailsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductReceiptDetail = productReceiptDetailRepository.saveAndFlush(productReceiptDetail);

        // Get all the productReceiptDetailList where quantity is not null
        defaultProductReceiptDetailFiltering("quantity.specified=true", "quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllProductReceiptDetailsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductReceiptDetail = productReceiptDetailRepository.saveAndFlush(productReceiptDetail);

        // Get all the productReceiptDetailList where quantity is greater than or equal to
        defaultProductReceiptDetailFiltering(
            "quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY,
            "quantity.greaterThanOrEqual=" + UPDATED_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllProductReceiptDetailsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductReceiptDetail = productReceiptDetailRepository.saveAndFlush(productReceiptDetail);

        // Get all the productReceiptDetailList where quantity is less than or equal to
        defaultProductReceiptDetailFiltering(
            "quantity.lessThanOrEqual=" + DEFAULT_QUANTITY,
            "quantity.lessThanOrEqual=" + SMALLER_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllProductReceiptDetailsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductReceiptDetail = productReceiptDetailRepository.saveAndFlush(productReceiptDetail);

        // Get all the productReceiptDetailList where quantity is less than
        defaultProductReceiptDetailFiltering("quantity.lessThan=" + UPDATED_QUANTITY, "quantity.lessThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductReceiptDetailsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductReceiptDetail = productReceiptDetailRepository.saveAndFlush(productReceiptDetail);

        // Get all the productReceiptDetailList where quantity is greater than
        defaultProductReceiptDetailFiltering("quantity.greaterThan=" + SMALLER_QUANTITY, "quantity.greaterThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductReceiptDetailsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            productReceiptDetailRepository.saveAndFlush(productReceiptDetail);
            product = ProductResourceIT.createEntity(em);
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product);
        em.flush();
        productReceiptDetail.setProduct(product);
        productReceiptDetailRepository.saveAndFlush(productReceiptDetail);
        Long productId = product.getId();
        // Get all the productReceiptDetailList where product equals to productId
        defaultProductReceiptDetailShouldBeFound("productId.equals=" + productId);

        // Get all the productReceiptDetailList where product equals to (productId + 1)
        defaultProductReceiptDetailShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllProductReceiptDetailsByReceiptIsEqualToSomething() throws Exception {
        ProductReceipt receipt;
        if (TestUtil.findAll(em, ProductReceipt.class).isEmpty()) {
            productReceiptDetailRepository.saveAndFlush(productReceiptDetail);
            receipt = ProductReceiptResourceIT.createEntity(em);
        } else {
            receipt = TestUtil.findAll(em, ProductReceipt.class).get(0);
        }
        em.persist(receipt);
        em.flush();
        productReceiptDetail.setReceipt(receipt);
        productReceiptDetailRepository.saveAndFlush(productReceiptDetail);
        Long receiptId = receipt.getId();
        // Get all the productReceiptDetailList where receipt equals to receiptId
        defaultProductReceiptDetailShouldBeFound("receiptId.equals=" + receiptId);

        // Get all the productReceiptDetailList where receipt equals to (receiptId + 1)
        defaultProductReceiptDetailShouldNotBeFound("receiptId.equals=" + (receiptId + 1));
    }

    private void defaultProductReceiptDetailFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductReceiptDetailShouldBeFound(shouldBeFound);
        defaultProductReceiptDetailShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductReceiptDetailShouldBeFound(String filter) throws Exception {
        restProductReceiptDetailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productReceiptDetail.getId().intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));

        // Check, that the count call also returns 1
        restProductReceiptDetailMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductReceiptDetailShouldNotBeFound(String filter) throws Exception {
        restProductReceiptDetailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductReceiptDetailMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductReceiptDetail() throws Exception {
        // Get the productReceiptDetail
        restProductReceiptDetailMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductReceiptDetail() throws Exception {
        // Initialize the database
        insertedProductReceiptDetail = productReceiptDetailRepository.saveAndFlush(productReceiptDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productReceiptDetail
        ProductReceiptDetail updatedProductReceiptDetail = productReceiptDetailRepository
            .findById(productReceiptDetail.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedProductReceiptDetail are not directly saved in db
        em.detach(updatedProductReceiptDetail);
        updatedProductReceiptDetail.note(UPDATED_NOTE).quantity(UPDATED_QUANTITY);
        ProductReceiptDetailDTO productReceiptDetailDTO = productReceiptDetailMapper.toDto(updatedProductReceiptDetail);

        restProductReceiptDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productReceiptDetailDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productReceiptDetailDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductReceiptDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductReceiptDetailToMatchAllProperties(updatedProductReceiptDetail);
    }

    @Test
    @Transactional
    void putNonExistingProductReceiptDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productReceiptDetail.setId(longCount.incrementAndGet());

        // Create the ProductReceiptDetail
        ProductReceiptDetailDTO productReceiptDetailDTO = productReceiptDetailMapper.toDto(productReceiptDetail);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductReceiptDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productReceiptDetailDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productReceiptDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductReceiptDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductReceiptDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productReceiptDetail.setId(longCount.incrementAndGet());

        // Create the ProductReceiptDetail
        ProductReceiptDetailDTO productReceiptDetailDTO = productReceiptDetailMapper.toDto(productReceiptDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductReceiptDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productReceiptDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductReceiptDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductReceiptDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productReceiptDetail.setId(longCount.incrementAndGet());

        // Create the ProductReceiptDetail
        ProductReceiptDetailDTO productReceiptDetailDTO = productReceiptDetailMapper.toDto(productReceiptDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductReceiptDetailMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productReceiptDetailDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductReceiptDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductReceiptDetailWithPatch() throws Exception {
        // Initialize the database
        insertedProductReceiptDetail = productReceiptDetailRepository.saveAndFlush(productReceiptDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productReceiptDetail using partial update
        ProductReceiptDetail partialUpdatedProductReceiptDetail = new ProductReceiptDetail();
        partialUpdatedProductReceiptDetail.setId(productReceiptDetail.getId());

        restProductReceiptDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductReceiptDetail.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductReceiptDetail))
            )
            .andExpect(status().isOk());

        // Validate the ProductReceiptDetail in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductReceiptDetailUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductReceiptDetail, productReceiptDetail),
            getPersistedProductReceiptDetail(productReceiptDetail)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductReceiptDetailWithPatch() throws Exception {
        // Initialize the database
        insertedProductReceiptDetail = productReceiptDetailRepository.saveAndFlush(productReceiptDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productReceiptDetail using partial update
        ProductReceiptDetail partialUpdatedProductReceiptDetail = new ProductReceiptDetail();
        partialUpdatedProductReceiptDetail.setId(productReceiptDetail.getId());

        partialUpdatedProductReceiptDetail.note(UPDATED_NOTE).quantity(UPDATED_QUANTITY);

        restProductReceiptDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductReceiptDetail.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductReceiptDetail))
            )
            .andExpect(status().isOk());

        // Validate the ProductReceiptDetail in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductReceiptDetailUpdatableFieldsEquals(
            partialUpdatedProductReceiptDetail,
            getPersistedProductReceiptDetail(partialUpdatedProductReceiptDetail)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProductReceiptDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productReceiptDetail.setId(longCount.incrementAndGet());

        // Create the ProductReceiptDetail
        ProductReceiptDetailDTO productReceiptDetailDTO = productReceiptDetailMapper.toDto(productReceiptDetail);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductReceiptDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productReceiptDetailDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productReceiptDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductReceiptDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductReceiptDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productReceiptDetail.setId(longCount.incrementAndGet());

        // Create the ProductReceiptDetail
        ProductReceiptDetailDTO productReceiptDetailDTO = productReceiptDetailMapper.toDto(productReceiptDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductReceiptDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productReceiptDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductReceiptDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductReceiptDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productReceiptDetail.setId(longCount.incrementAndGet());

        // Create the ProductReceiptDetail
        ProductReceiptDetailDTO productReceiptDetailDTO = productReceiptDetailMapper.toDto(productReceiptDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductReceiptDetailMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productReceiptDetailDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductReceiptDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductReceiptDetail() throws Exception {
        // Initialize the database
        insertedProductReceiptDetail = productReceiptDetailRepository.saveAndFlush(productReceiptDetail);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productReceiptDetail
        restProductReceiptDetailMockMvc
            .perform(delete(ENTITY_API_URL_ID, productReceiptDetail.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productReceiptDetailRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected ProductReceiptDetail getPersistedProductReceiptDetail(ProductReceiptDetail productReceiptDetail) {
        return productReceiptDetailRepository.findById(productReceiptDetail.getId()).orElseThrow();
    }

    protected void assertPersistedProductReceiptDetailToMatchAllProperties(ProductReceiptDetail expectedProductReceiptDetail) {
        assertProductReceiptDetailAllPropertiesEquals(
            expectedProductReceiptDetail,
            getPersistedProductReceiptDetail(expectedProductReceiptDetail)
        );
    }

    protected void assertPersistedProductReceiptDetailToMatchUpdatableProperties(ProductReceiptDetail expectedProductReceiptDetail) {
        assertProductReceiptDetailAllUpdatablePropertiesEquals(
            expectedProductReceiptDetail,
            getPersistedProductReceiptDetail(expectedProductReceiptDetail)
        );
    }
}
