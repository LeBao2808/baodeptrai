package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ProductReceiptAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ProductReceipt;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.ProductReceiptRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.service.dto.ProductReceiptDTO;
import com.mycompany.myapp.service.mapper.ProductReceiptMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ProductReceiptResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductReceiptResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_PAYMENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAYMENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_RECEIPT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RECEIPT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;
    private static final Integer SMALLER_STATUS = 1 - 1;

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-receipts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductReceiptRepository productReceiptRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductReceiptMapper productReceiptMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductReceiptMockMvc;

    private ProductReceipt productReceipt;

    private ProductReceipt insertedProductReceipt;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductReceipt createEntity(EntityManager em) {
        ProductReceipt productReceipt = new ProductReceipt()
            .creationDate(DEFAULT_CREATION_DATE)
            .paymentDate(DEFAULT_PAYMENT_DATE)
            .receiptDate(DEFAULT_RECEIPT_DATE)
            .status(DEFAULT_STATUS)
            .code(DEFAULT_CODE);
        return productReceipt;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductReceipt createUpdatedEntity(EntityManager em) {
        ProductReceipt productReceipt = new ProductReceipt()
            .creationDate(UPDATED_CREATION_DATE)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .receiptDate(UPDATED_RECEIPT_DATE)
            .status(UPDATED_STATUS)
            .code(UPDATED_CODE);
        return productReceipt;
    }

    @BeforeEach
    public void initTest() {
        productReceipt = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductReceipt != null) {
            productReceiptRepository.delete(insertedProductReceipt);
            insertedProductReceipt = null;
        }
    }

    @Test
    @Transactional
    void createProductReceipt() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductReceipt
        ProductReceiptDTO productReceiptDTO = productReceiptMapper.toDto(productReceipt);
        var returnedProductReceiptDTO = om.readValue(
            restProductReceiptMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productReceiptDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductReceiptDTO.class
        );

        // Validate the ProductReceipt in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProductReceipt = productReceiptMapper.toEntity(returnedProductReceiptDTO);
        assertProductReceiptUpdatableFieldsEquals(returnedProductReceipt, getPersistedProductReceipt(returnedProductReceipt));

        insertedProductReceipt = returnedProductReceipt;
    }

    @Test
    @Transactional
    void createProductReceiptWithExistingId() throws Exception {
        // Create the ProductReceipt with an existing ID
        productReceipt.setId(1L);
        ProductReceiptDTO productReceiptDTO = productReceiptMapper.toDto(productReceipt);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductReceiptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productReceiptDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductReceipt in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductReceipts() throws Exception {
        // Initialize the database
        insertedProductReceipt = productReceiptRepository.saveAndFlush(productReceipt);

        // Get all the productReceiptList
        restProductReceiptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productReceipt.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].receiptDate").value(hasItem(DEFAULT_RECEIPT_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getProductReceipt() throws Exception {
        // Initialize the database
        insertedProductReceipt = productReceiptRepository.saveAndFlush(productReceipt);

        // Get the productReceipt
        restProductReceiptMockMvc
            .perform(get(ENTITY_API_URL_ID, productReceipt.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productReceipt.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.receiptDate").value(DEFAULT_RECEIPT_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getProductReceiptsByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductReceipt = productReceiptRepository.saveAndFlush(productReceipt);

        Long id = productReceipt.getId();

        defaultProductReceiptFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductReceiptFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductReceiptFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductReceiptsByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductReceipt = productReceiptRepository.saveAndFlush(productReceipt);

        // Get all the productReceiptList where creationDate equals to
        defaultProductReceiptFiltering("creationDate.equals=" + DEFAULT_CREATION_DATE, "creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllProductReceiptsByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductReceipt = productReceiptRepository.saveAndFlush(productReceipt);

        // Get all the productReceiptList where creationDate in
        defaultProductReceiptFiltering(
            "creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE,
            "creationDate.in=" + UPDATED_CREATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllProductReceiptsByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductReceipt = productReceiptRepository.saveAndFlush(productReceipt);

        // Get all the productReceiptList where creationDate is not null
        defaultProductReceiptFiltering("creationDate.specified=true", "creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductReceiptsByPaymentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductReceipt = productReceiptRepository.saveAndFlush(productReceipt);

        // Get all the productReceiptList where paymentDate equals to
        defaultProductReceiptFiltering("paymentDate.equals=" + DEFAULT_PAYMENT_DATE, "paymentDate.equals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllProductReceiptsByPaymentDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductReceipt = productReceiptRepository.saveAndFlush(productReceipt);

        // Get all the productReceiptList where paymentDate in
        defaultProductReceiptFiltering(
            "paymentDate.in=" + DEFAULT_PAYMENT_DATE + "," + UPDATED_PAYMENT_DATE,
            "paymentDate.in=" + UPDATED_PAYMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllProductReceiptsByPaymentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductReceipt = productReceiptRepository.saveAndFlush(productReceipt);

        // Get all the productReceiptList where paymentDate is not null
        defaultProductReceiptFiltering("paymentDate.specified=true", "paymentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductReceiptsByReceiptDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductReceipt = productReceiptRepository.saveAndFlush(productReceipt);

        // Get all the productReceiptList where receiptDate equals to
        defaultProductReceiptFiltering("receiptDate.equals=" + DEFAULT_RECEIPT_DATE, "receiptDate.equals=" + UPDATED_RECEIPT_DATE);
    }

    @Test
    @Transactional
    void getAllProductReceiptsByReceiptDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductReceipt = productReceiptRepository.saveAndFlush(productReceipt);

        // Get all the productReceiptList where receiptDate in
        defaultProductReceiptFiltering(
            "receiptDate.in=" + DEFAULT_RECEIPT_DATE + "," + UPDATED_RECEIPT_DATE,
            "receiptDate.in=" + UPDATED_RECEIPT_DATE
        );
    }

    @Test
    @Transactional
    void getAllProductReceiptsByReceiptDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductReceipt = productReceiptRepository.saveAndFlush(productReceipt);

        // Get all the productReceiptList where receiptDate is not null
        defaultProductReceiptFiltering("receiptDate.specified=true", "receiptDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductReceiptsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductReceipt = productReceiptRepository.saveAndFlush(productReceipt);

        // Get all the productReceiptList where status equals to
        defaultProductReceiptFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllProductReceiptsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductReceipt = productReceiptRepository.saveAndFlush(productReceipt);

        // Get all the productReceiptList where status in
        defaultProductReceiptFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllProductReceiptsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductReceipt = productReceiptRepository.saveAndFlush(productReceipt);

        // Get all the productReceiptList where status is not null
        defaultProductReceiptFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllProductReceiptsByStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductReceipt = productReceiptRepository.saveAndFlush(productReceipt);

        // Get all the productReceiptList where status is greater than or equal to
        defaultProductReceiptFiltering("status.greaterThanOrEqual=" + DEFAULT_STATUS, "status.greaterThanOrEqual=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllProductReceiptsByStatusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductReceipt = productReceiptRepository.saveAndFlush(productReceipt);

        // Get all the productReceiptList where status is less than or equal to
        defaultProductReceiptFiltering("status.lessThanOrEqual=" + DEFAULT_STATUS, "status.lessThanOrEqual=" + SMALLER_STATUS);
    }

    @Test
    @Transactional
    void getAllProductReceiptsByStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductReceipt = productReceiptRepository.saveAndFlush(productReceipt);

        // Get all the productReceiptList where status is less than
        defaultProductReceiptFiltering("status.lessThan=" + UPDATED_STATUS, "status.lessThan=" + DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void getAllProductReceiptsByStatusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductReceipt = productReceiptRepository.saveAndFlush(productReceipt);

        // Get all the productReceiptList where status is greater than
        defaultProductReceiptFiltering("status.greaterThan=" + SMALLER_STATUS, "status.greaterThan=" + DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void getAllProductReceiptsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductReceipt = productReceiptRepository.saveAndFlush(productReceipt);

        // Get all the productReceiptList where code equals to
        defaultProductReceiptFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllProductReceiptsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductReceipt = productReceiptRepository.saveAndFlush(productReceipt);

        // Get all the productReceiptList where code in
        defaultProductReceiptFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllProductReceiptsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductReceipt = productReceiptRepository.saveAndFlush(productReceipt);

        // Get all the productReceiptList where code is not null
        defaultProductReceiptFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllProductReceiptsByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedProductReceipt = productReceiptRepository.saveAndFlush(productReceipt);

        // Get all the productReceiptList where code contains
        defaultProductReceiptFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllProductReceiptsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductReceipt = productReceiptRepository.saveAndFlush(productReceipt);

        // Get all the productReceiptList where code does not contain
        defaultProductReceiptFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllProductReceiptsByCreatedIsEqualToSomething() throws Exception {
        User created;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            productReceiptRepository.saveAndFlush(productReceipt);
            created = UserResourceIT.createEntity(em);
        } else {
            created = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(created);
        em.flush();
        productReceipt.setCreated(created);
        productReceiptRepository.saveAndFlush(productReceipt);
        Long createdId = created.getId();
        // Get all the productReceiptList where created equals to createdId
        defaultProductReceiptShouldBeFound("createdId.equals=" + createdId);

        // Get all the productReceiptList where created equals to (createdId + 1)
        defaultProductReceiptShouldNotBeFound("createdId.equals=" + (createdId + 1));
    }

    private void defaultProductReceiptFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductReceiptShouldBeFound(shouldBeFound);
        defaultProductReceiptShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductReceiptShouldBeFound(String filter) throws Exception {
        restProductReceiptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productReceipt.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].receiptDate").value(hasItem(DEFAULT_RECEIPT_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));

        // Check, that the count call also returns 1
        restProductReceiptMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductReceiptShouldNotBeFound(String filter) throws Exception {
        restProductReceiptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductReceiptMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductReceipt() throws Exception {
        // Get the productReceipt
        restProductReceiptMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductReceipt() throws Exception {
        // Initialize the database
        insertedProductReceipt = productReceiptRepository.saveAndFlush(productReceipt);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productReceipt
        ProductReceipt updatedProductReceipt = productReceiptRepository.findById(productReceipt.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductReceipt are not directly saved in db
        em.detach(updatedProductReceipt);
        updatedProductReceipt
            .creationDate(UPDATED_CREATION_DATE)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .receiptDate(UPDATED_RECEIPT_DATE)
            .status(UPDATED_STATUS)
            .code(UPDATED_CODE);
        ProductReceiptDTO productReceiptDTO = productReceiptMapper.toDto(updatedProductReceipt);

        restProductReceiptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productReceiptDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productReceiptDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductReceipt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductReceiptToMatchAllProperties(updatedProductReceipt);
    }

    @Test
    @Transactional
    void putNonExistingProductReceipt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productReceipt.setId(longCount.incrementAndGet());

        // Create the ProductReceipt
        ProductReceiptDTO productReceiptDTO = productReceiptMapper.toDto(productReceipt);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductReceiptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productReceiptDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productReceiptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductReceipt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductReceipt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productReceipt.setId(longCount.incrementAndGet());

        // Create the ProductReceipt
        ProductReceiptDTO productReceiptDTO = productReceiptMapper.toDto(productReceipt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductReceiptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productReceiptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductReceipt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductReceipt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productReceipt.setId(longCount.incrementAndGet());

        // Create the ProductReceipt
        ProductReceiptDTO productReceiptDTO = productReceiptMapper.toDto(productReceipt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductReceiptMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productReceiptDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductReceipt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductReceiptWithPatch() throws Exception {
        // Initialize the database
        insertedProductReceipt = productReceiptRepository.saveAndFlush(productReceipt);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productReceipt using partial update
        ProductReceipt partialUpdatedProductReceipt = new ProductReceipt();
        partialUpdatedProductReceipt.setId(productReceipt.getId());

        partialUpdatedProductReceipt
            .creationDate(UPDATED_CREATION_DATE)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .receiptDate(UPDATED_RECEIPT_DATE)
            .status(UPDATED_STATUS)
            .code(UPDATED_CODE);

        restProductReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductReceipt.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductReceipt))
            )
            .andExpect(status().isOk());

        // Validate the ProductReceipt in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductReceiptUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductReceipt, productReceipt),
            getPersistedProductReceipt(productReceipt)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductReceiptWithPatch() throws Exception {
        // Initialize the database
        insertedProductReceipt = productReceiptRepository.saveAndFlush(productReceipt);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productReceipt using partial update
        ProductReceipt partialUpdatedProductReceipt = new ProductReceipt();
        partialUpdatedProductReceipt.setId(productReceipt.getId());

        partialUpdatedProductReceipt
            .creationDate(UPDATED_CREATION_DATE)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .receiptDate(UPDATED_RECEIPT_DATE)
            .status(UPDATED_STATUS)
            .code(UPDATED_CODE);

        restProductReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductReceipt.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductReceipt))
            )
            .andExpect(status().isOk());

        // Validate the ProductReceipt in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductReceiptUpdatableFieldsEquals(partialUpdatedProductReceipt, getPersistedProductReceipt(partialUpdatedProductReceipt));
    }

    @Test
    @Transactional
    void patchNonExistingProductReceipt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productReceipt.setId(longCount.incrementAndGet());

        // Create the ProductReceipt
        ProductReceiptDTO productReceiptDTO = productReceiptMapper.toDto(productReceipt);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productReceiptDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productReceiptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductReceipt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductReceipt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productReceipt.setId(longCount.incrementAndGet());

        // Create the ProductReceipt
        ProductReceiptDTO productReceiptDTO = productReceiptMapper.toDto(productReceipt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productReceiptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductReceipt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductReceipt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productReceipt.setId(longCount.incrementAndGet());

        // Create the ProductReceipt
        ProductReceiptDTO productReceiptDTO = productReceiptMapper.toDto(productReceipt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductReceiptMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productReceiptDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductReceipt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductReceipt() throws Exception {
        // Initialize the database
        insertedProductReceipt = productReceiptRepository.saveAndFlush(productReceipt);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productReceipt
        restProductReceiptMockMvc
            .perform(delete(ENTITY_API_URL_ID, productReceipt.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productReceiptRepository.count();
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

    protected ProductReceipt getPersistedProductReceipt(ProductReceipt productReceipt) {
        return productReceiptRepository.findById(productReceipt.getId()).orElseThrow();
    }

    protected void assertPersistedProductReceiptToMatchAllProperties(ProductReceipt expectedProductReceipt) {
        assertProductReceiptAllPropertiesEquals(expectedProductReceipt, getPersistedProductReceipt(expectedProductReceipt));
    }

    protected void assertPersistedProductReceiptToMatchUpdatableProperties(ProductReceipt expectedProductReceipt) {
        assertProductReceiptAllUpdatablePropertiesEquals(expectedProductReceipt, getPersistedProductReceipt(expectedProductReceipt));
    }
}
