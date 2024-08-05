package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.MaterialReceiptAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.MaterialReceipt;
import com.mycompany.myapp.domain.Planning;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.MaterialReceiptRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.service.dto.MaterialReceiptDTO;
import com.mycompany.myapp.service.mapper.MaterialReceiptMapper;
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
 * Integration tests for the {@link MaterialReceiptResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MaterialReceiptResourceIT {

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

    private static final String ENTITY_API_URL = "/api/material-receipts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MaterialReceiptRepository materialReceiptRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MaterialReceiptMapper materialReceiptMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMaterialReceiptMockMvc;

    private MaterialReceipt materialReceipt;

    private MaterialReceipt insertedMaterialReceipt;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaterialReceipt createEntity(EntityManager em) {
        MaterialReceipt materialReceipt = new MaterialReceipt()
            .creationDate(DEFAULT_CREATION_DATE)
            .paymentDate(DEFAULT_PAYMENT_DATE)
            .receiptDate(DEFAULT_RECEIPT_DATE)
            .status(DEFAULT_STATUS)
            .code(DEFAULT_CODE);
        return materialReceipt;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaterialReceipt createUpdatedEntity(EntityManager em) {
        MaterialReceipt materialReceipt = new MaterialReceipt()
            .creationDate(UPDATED_CREATION_DATE)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .receiptDate(UPDATED_RECEIPT_DATE)
            .status(UPDATED_STATUS)
            .code(UPDATED_CODE);
        return materialReceipt;
    }

    @BeforeEach
    public void initTest() {
        materialReceipt = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedMaterialReceipt != null) {
            materialReceiptRepository.delete(insertedMaterialReceipt);
            insertedMaterialReceipt = null;
        }
    }

    @Test
    @Transactional
    void createMaterialReceipt() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MaterialReceipt
        MaterialReceiptDTO materialReceiptDTO = materialReceiptMapper.toDto(materialReceipt);
        var returnedMaterialReceiptDTO = om.readValue(
            restMaterialReceiptMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materialReceiptDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MaterialReceiptDTO.class
        );

        // Validate the MaterialReceipt in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMaterialReceipt = materialReceiptMapper.toEntity(returnedMaterialReceiptDTO);
        assertMaterialReceiptUpdatableFieldsEquals(returnedMaterialReceipt, getPersistedMaterialReceipt(returnedMaterialReceipt));

        insertedMaterialReceipt = returnedMaterialReceipt;
    }

    @Test
    @Transactional
    void createMaterialReceiptWithExistingId() throws Exception {
        // Create the MaterialReceipt with an existing ID
        materialReceipt.setId(1L);
        MaterialReceiptDTO materialReceiptDTO = materialReceiptMapper.toDto(materialReceipt);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaterialReceiptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materialReceiptDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MaterialReceipt in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMaterialReceipts() throws Exception {
        // Initialize the database
        insertedMaterialReceipt = materialReceiptRepository.saveAndFlush(materialReceipt);

        // Get all the materialReceiptList
        restMaterialReceiptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(materialReceipt.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].receiptDate").value(hasItem(DEFAULT_RECEIPT_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getMaterialReceipt() throws Exception {
        // Initialize the database
        insertedMaterialReceipt = materialReceiptRepository.saveAndFlush(materialReceipt);

        // Get the materialReceipt
        restMaterialReceiptMockMvc
            .perform(get(ENTITY_API_URL_ID, materialReceipt.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(materialReceipt.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.receiptDate").value(DEFAULT_RECEIPT_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getMaterialReceiptsByIdFiltering() throws Exception {
        // Initialize the database
        insertedMaterialReceipt = materialReceiptRepository.saveAndFlush(materialReceipt);

        Long id = materialReceipt.getId();

        defaultMaterialReceiptFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMaterialReceiptFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMaterialReceiptFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMaterialReceiptsByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialReceipt = materialReceiptRepository.saveAndFlush(materialReceipt);

        // Get all the materialReceiptList where creationDate equals to
        defaultMaterialReceiptFiltering("creationDate.equals=" + DEFAULT_CREATION_DATE, "creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllMaterialReceiptsByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaterialReceipt = materialReceiptRepository.saveAndFlush(materialReceipt);

        // Get all the materialReceiptList where creationDate in
        defaultMaterialReceiptFiltering(
            "creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE,
            "creationDate.in=" + UPDATED_CREATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllMaterialReceiptsByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaterialReceipt = materialReceiptRepository.saveAndFlush(materialReceipt);

        // Get all the materialReceiptList where creationDate is not null
        defaultMaterialReceiptFiltering("creationDate.specified=true", "creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMaterialReceiptsByPaymentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialReceipt = materialReceiptRepository.saveAndFlush(materialReceipt);

        // Get all the materialReceiptList where paymentDate equals to
        defaultMaterialReceiptFiltering("paymentDate.equals=" + DEFAULT_PAYMENT_DATE, "paymentDate.equals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllMaterialReceiptsByPaymentDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaterialReceipt = materialReceiptRepository.saveAndFlush(materialReceipt);

        // Get all the materialReceiptList where paymentDate in
        defaultMaterialReceiptFiltering(
            "paymentDate.in=" + DEFAULT_PAYMENT_DATE + "," + UPDATED_PAYMENT_DATE,
            "paymentDate.in=" + UPDATED_PAYMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllMaterialReceiptsByPaymentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaterialReceipt = materialReceiptRepository.saveAndFlush(materialReceipt);

        // Get all the materialReceiptList where paymentDate is not null
        defaultMaterialReceiptFiltering("paymentDate.specified=true", "paymentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMaterialReceiptsByReceiptDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialReceipt = materialReceiptRepository.saveAndFlush(materialReceipt);

        // Get all the materialReceiptList where receiptDate equals to
        defaultMaterialReceiptFiltering("receiptDate.equals=" + DEFAULT_RECEIPT_DATE, "receiptDate.equals=" + UPDATED_RECEIPT_DATE);
    }

    @Test
    @Transactional
    void getAllMaterialReceiptsByReceiptDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaterialReceipt = materialReceiptRepository.saveAndFlush(materialReceipt);

        // Get all the materialReceiptList where receiptDate in
        defaultMaterialReceiptFiltering(
            "receiptDate.in=" + DEFAULT_RECEIPT_DATE + "," + UPDATED_RECEIPT_DATE,
            "receiptDate.in=" + UPDATED_RECEIPT_DATE
        );
    }

    @Test
    @Transactional
    void getAllMaterialReceiptsByReceiptDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaterialReceipt = materialReceiptRepository.saveAndFlush(materialReceipt);

        // Get all the materialReceiptList where receiptDate is not null
        defaultMaterialReceiptFiltering("receiptDate.specified=true", "receiptDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMaterialReceiptsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialReceipt = materialReceiptRepository.saveAndFlush(materialReceipt);

        // Get all the materialReceiptList where status equals to
        defaultMaterialReceiptFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMaterialReceiptsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaterialReceipt = materialReceiptRepository.saveAndFlush(materialReceipt);

        // Get all the materialReceiptList where status in
        defaultMaterialReceiptFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMaterialReceiptsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaterialReceipt = materialReceiptRepository.saveAndFlush(materialReceipt);

        // Get all the materialReceiptList where status is not null
        defaultMaterialReceiptFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllMaterialReceiptsByStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialReceipt = materialReceiptRepository.saveAndFlush(materialReceipt);

        // Get all the materialReceiptList where status is greater than or equal to
        defaultMaterialReceiptFiltering("status.greaterThanOrEqual=" + DEFAULT_STATUS, "status.greaterThanOrEqual=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMaterialReceiptsByStatusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialReceipt = materialReceiptRepository.saveAndFlush(materialReceipt);

        // Get all the materialReceiptList where status is less than or equal to
        defaultMaterialReceiptFiltering("status.lessThanOrEqual=" + DEFAULT_STATUS, "status.lessThanOrEqual=" + SMALLER_STATUS);
    }

    @Test
    @Transactional
    void getAllMaterialReceiptsByStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMaterialReceipt = materialReceiptRepository.saveAndFlush(materialReceipt);

        // Get all the materialReceiptList where status is less than
        defaultMaterialReceiptFiltering("status.lessThan=" + UPDATED_STATUS, "status.lessThan=" + DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void getAllMaterialReceiptsByStatusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMaterialReceipt = materialReceiptRepository.saveAndFlush(materialReceipt);

        // Get all the materialReceiptList where status is greater than
        defaultMaterialReceiptFiltering("status.greaterThan=" + SMALLER_STATUS, "status.greaterThan=" + DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void getAllMaterialReceiptsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialReceipt = materialReceiptRepository.saveAndFlush(materialReceipt);

        // Get all the materialReceiptList where code equals to
        defaultMaterialReceiptFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMaterialReceiptsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaterialReceipt = materialReceiptRepository.saveAndFlush(materialReceipt);

        // Get all the materialReceiptList where code in
        defaultMaterialReceiptFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMaterialReceiptsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaterialReceipt = materialReceiptRepository.saveAndFlush(materialReceipt);

        // Get all the materialReceiptList where code is not null
        defaultMaterialReceiptFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllMaterialReceiptsByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedMaterialReceipt = materialReceiptRepository.saveAndFlush(materialReceipt);

        // Get all the materialReceiptList where code contains
        defaultMaterialReceiptFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMaterialReceiptsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMaterialReceipt = materialReceiptRepository.saveAndFlush(materialReceipt);

        // Get all the materialReceiptList where code does not contain
        defaultMaterialReceiptFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllMaterialReceiptsByCreatedByUserIsEqualToSomething() throws Exception {
        User createdByUser;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            materialReceiptRepository.saveAndFlush(materialReceipt);
            createdByUser = UserResourceIT.createEntity(em);
        } else {
            createdByUser = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(createdByUser);
        em.flush();
        materialReceipt.setCreatedByUser(createdByUser);
        materialReceiptRepository.saveAndFlush(materialReceipt);
        Long createdByUserId = createdByUser.getId();
        // Get all the materialReceiptList where createdByUser equals to createdByUserId
        defaultMaterialReceiptShouldBeFound("createdByUserId.equals=" + createdByUserId);

        // Get all the materialReceiptList where createdByUser equals to (createdByUserId + 1)
        defaultMaterialReceiptShouldNotBeFound("createdByUserId.equals=" + (createdByUserId + 1));
    }

    @Test
    @Transactional
    void getAllMaterialReceiptsByQuantificationOrderIsEqualToSomething() throws Exception {
        Planning quantificationOrder;
        if (TestUtil.findAll(em, Planning.class).isEmpty()) {
            materialReceiptRepository.saveAndFlush(materialReceipt);
            quantificationOrder = PlanningResourceIT.createEntity(em);
        } else {
            quantificationOrder = TestUtil.findAll(em, Planning.class).get(0);
        }
        em.persist(quantificationOrder);
        em.flush();
        materialReceipt.setQuantificationOrder(quantificationOrder);
        materialReceiptRepository.saveAndFlush(materialReceipt);
        Long quantificationOrderId = quantificationOrder.getId();
        // Get all the materialReceiptList where quantificationOrder equals to quantificationOrderId
        defaultMaterialReceiptShouldBeFound("quantificationOrderId.equals=" + quantificationOrderId);

        // Get all the materialReceiptList where quantificationOrder equals to (quantificationOrderId + 1)
        defaultMaterialReceiptShouldNotBeFound("quantificationOrderId.equals=" + (quantificationOrderId + 1));
    }

    private void defaultMaterialReceiptFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMaterialReceiptShouldBeFound(shouldBeFound);
        defaultMaterialReceiptShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMaterialReceiptShouldBeFound(String filter) throws Exception {
        restMaterialReceiptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(materialReceipt.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].receiptDate").value(hasItem(DEFAULT_RECEIPT_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));

        // Check, that the count call also returns 1
        restMaterialReceiptMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMaterialReceiptShouldNotBeFound(String filter) throws Exception {
        restMaterialReceiptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMaterialReceiptMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMaterialReceipt() throws Exception {
        // Get the materialReceipt
        restMaterialReceiptMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMaterialReceipt() throws Exception {
        // Initialize the database
        insertedMaterialReceipt = materialReceiptRepository.saveAndFlush(materialReceipt);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the materialReceipt
        MaterialReceipt updatedMaterialReceipt = materialReceiptRepository.findById(materialReceipt.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMaterialReceipt are not directly saved in db
        em.detach(updatedMaterialReceipt);
        updatedMaterialReceipt
            .creationDate(UPDATED_CREATION_DATE)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .receiptDate(UPDATED_RECEIPT_DATE)
            .status(UPDATED_STATUS)
            .code(UPDATED_CODE);
        MaterialReceiptDTO materialReceiptDTO = materialReceiptMapper.toDto(updatedMaterialReceipt);

        restMaterialReceiptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, materialReceiptDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materialReceiptDTO))
            )
            .andExpect(status().isOk());

        // Validate the MaterialReceipt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMaterialReceiptToMatchAllProperties(updatedMaterialReceipt);
    }

    @Test
    @Transactional
    void putNonExistingMaterialReceipt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materialReceipt.setId(longCount.incrementAndGet());

        // Create the MaterialReceipt
        MaterialReceiptDTO materialReceiptDTO = materialReceiptMapper.toDto(materialReceipt);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterialReceiptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, materialReceiptDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materialReceiptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaterialReceipt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMaterialReceipt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materialReceipt.setId(longCount.incrementAndGet());

        // Create the MaterialReceipt
        MaterialReceiptDTO materialReceiptDTO = materialReceiptMapper.toDto(materialReceipt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialReceiptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materialReceiptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaterialReceipt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMaterialReceipt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materialReceipt.setId(longCount.incrementAndGet());

        // Create the MaterialReceipt
        MaterialReceiptDTO materialReceiptDTO = materialReceiptMapper.toDto(materialReceipt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialReceiptMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materialReceiptDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MaterialReceipt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMaterialReceiptWithPatch() throws Exception {
        // Initialize the database
        insertedMaterialReceipt = materialReceiptRepository.saveAndFlush(materialReceipt);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the materialReceipt using partial update
        MaterialReceipt partialUpdatedMaterialReceipt = new MaterialReceipt();
        partialUpdatedMaterialReceipt.setId(materialReceipt.getId());

        partialUpdatedMaterialReceipt.creationDate(UPDATED_CREATION_DATE).receiptDate(UPDATED_RECEIPT_DATE);

        restMaterialReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaterialReceipt.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMaterialReceipt))
            )
            .andExpect(status().isOk());

        // Validate the MaterialReceipt in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMaterialReceiptUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMaterialReceipt, materialReceipt),
            getPersistedMaterialReceipt(materialReceipt)
        );
    }

    @Test
    @Transactional
    void fullUpdateMaterialReceiptWithPatch() throws Exception {
        // Initialize the database
        insertedMaterialReceipt = materialReceiptRepository.saveAndFlush(materialReceipt);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the materialReceipt using partial update
        MaterialReceipt partialUpdatedMaterialReceipt = new MaterialReceipt();
        partialUpdatedMaterialReceipt.setId(materialReceipt.getId());

        partialUpdatedMaterialReceipt
            .creationDate(UPDATED_CREATION_DATE)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .receiptDate(UPDATED_RECEIPT_DATE)
            .status(UPDATED_STATUS)
            .code(UPDATED_CODE);

        restMaterialReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaterialReceipt.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMaterialReceipt))
            )
            .andExpect(status().isOk());

        // Validate the MaterialReceipt in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMaterialReceiptUpdatableFieldsEquals(
            partialUpdatedMaterialReceipt,
            getPersistedMaterialReceipt(partialUpdatedMaterialReceipt)
        );
    }

    @Test
    @Transactional
    void patchNonExistingMaterialReceipt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materialReceipt.setId(longCount.incrementAndGet());

        // Create the MaterialReceipt
        MaterialReceiptDTO materialReceiptDTO = materialReceiptMapper.toDto(materialReceipt);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterialReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, materialReceiptDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(materialReceiptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaterialReceipt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMaterialReceipt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materialReceipt.setId(longCount.incrementAndGet());

        // Create the MaterialReceipt
        MaterialReceiptDTO materialReceiptDTO = materialReceiptMapper.toDto(materialReceipt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(materialReceiptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaterialReceipt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMaterialReceipt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materialReceipt.setId(longCount.incrementAndGet());

        // Create the MaterialReceipt
        MaterialReceiptDTO materialReceiptDTO = materialReceiptMapper.toDto(materialReceipt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialReceiptMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(materialReceiptDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MaterialReceipt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMaterialReceipt() throws Exception {
        // Initialize the database
        insertedMaterialReceipt = materialReceiptRepository.saveAndFlush(materialReceipt);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the materialReceipt
        restMaterialReceiptMockMvc
            .perform(delete(ENTITY_API_URL_ID, materialReceipt.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return materialReceiptRepository.count();
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

    protected MaterialReceipt getPersistedMaterialReceipt(MaterialReceipt materialReceipt) {
        return materialReceiptRepository.findById(materialReceipt.getId()).orElseThrow();
    }

    protected void assertPersistedMaterialReceiptToMatchAllProperties(MaterialReceipt expectedMaterialReceipt) {
        assertMaterialReceiptAllPropertiesEquals(expectedMaterialReceipt, getPersistedMaterialReceipt(expectedMaterialReceipt));
    }

    protected void assertPersistedMaterialReceiptToMatchUpdatableProperties(MaterialReceipt expectedMaterialReceipt) {
        assertMaterialReceiptAllUpdatablePropertiesEquals(expectedMaterialReceipt, getPersistedMaterialReceipt(expectedMaterialReceipt));
    }
}
