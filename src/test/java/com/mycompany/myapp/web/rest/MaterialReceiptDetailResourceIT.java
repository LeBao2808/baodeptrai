package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.MaterialReceiptDetailAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Material;
import com.mycompany.myapp.domain.MaterialReceipt;
import com.mycompany.myapp.domain.MaterialReceiptDetail;
import com.mycompany.myapp.repository.MaterialReceiptDetailRepository;
import com.mycompany.myapp.service.dto.MaterialReceiptDetailDTO;
import com.mycompany.myapp.service.mapper.MaterialReceiptDetailMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link MaterialReceiptDetailResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MaterialReceiptDetailResourceIT {

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_IMPORT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_IMPORT_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_IMPORT_PRICE = new BigDecimal(1 - 1);

    private static final Float DEFAULT_QUANTITY = 1F;
    private static final Float UPDATED_QUANTITY = 2F;
    private static final Float SMALLER_QUANTITY = 1F - 1F;

    private static final String ENTITY_API_URL = "/api/material-receipt-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MaterialReceiptDetailRepository materialReceiptDetailRepository;

    @Autowired
    private MaterialReceiptDetailMapper materialReceiptDetailMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMaterialReceiptDetailMockMvc;

    private MaterialReceiptDetail materialReceiptDetail;

    private MaterialReceiptDetail insertedMaterialReceiptDetail;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaterialReceiptDetail createEntity(EntityManager em) {
        MaterialReceiptDetail materialReceiptDetail = new MaterialReceiptDetail()
            .note(DEFAULT_NOTE)
            .importPrice(DEFAULT_IMPORT_PRICE)
            .quantity(DEFAULT_QUANTITY);
        return materialReceiptDetail;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaterialReceiptDetail createUpdatedEntity(EntityManager em) {
        MaterialReceiptDetail materialReceiptDetail = new MaterialReceiptDetail()
            .note(UPDATED_NOTE)
            .importPrice(UPDATED_IMPORT_PRICE)
            .quantity(UPDATED_QUANTITY);
        return materialReceiptDetail;
    }

    @BeforeEach
    public void initTest() {
        materialReceiptDetail = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedMaterialReceiptDetail != null) {
            materialReceiptDetailRepository.delete(insertedMaterialReceiptDetail);
            insertedMaterialReceiptDetail = null;
        }
    }

    @Test
    @Transactional
    void createMaterialReceiptDetail() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MaterialReceiptDetail
        MaterialReceiptDetailDTO materialReceiptDetailDTO = materialReceiptDetailMapper.toDto(materialReceiptDetail);
        var returnedMaterialReceiptDetailDTO = om.readValue(
            restMaterialReceiptDetailMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materialReceiptDetailDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MaterialReceiptDetailDTO.class
        );

        // Validate the MaterialReceiptDetail in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMaterialReceiptDetail = materialReceiptDetailMapper.toEntity(returnedMaterialReceiptDetailDTO);
        assertMaterialReceiptDetailUpdatableFieldsEquals(
            returnedMaterialReceiptDetail,
            getPersistedMaterialReceiptDetail(returnedMaterialReceiptDetail)
        );

        insertedMaterialReceiptDetail = returnedMaterialReceiptDetail;
    }

    @Test
    @Transactional
    void createMaterialReceiptDetailWithExistingId() throws Exception {
        // Create the MaterialReceiptDetail with an existing ID
        materialReceiptDetail.setId(1L);
        MaterialReceiptDetailDTO materialReceiptDetailDTO = materialReceiptDetailMapper.toDto(materialReceiptDetail);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaterialReceiptDetailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materialReceiptDetailDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MaterialReceiptDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMaterialReceiptDetails() throws Exception {
        // Initialize the database
        insertedMaterialReceiptDetail = materialReceiptDetailRepository.saveAndFlush(materialReceiptDetail);

        // Get all the materialReceiptDetailList
        restMaterialReceiptDetailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(materialReceiptDetail.getId().intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].importPrice").value(hasItem(sameNumber(DEFAULT_IMPORT_PRICE))))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.doubleValue())));
    }

    @Test
    @Transactional
    void getMaterialReceiptDetail() throws Exception {
        // Initialize the database
        insertedMaterialReceiptDetail = materialReceiptDetailRepository.saveAndFlush(materialReceiptDetail);

        // Get the materialReceiptDetail
        restMaterialReceiptDetailMockMvc
            .perform(get(ENTITY_API_URL_ID, materialReceiptDetail.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(materialReceiptDetail.getId().intValue()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.importPrice").value(sameNumber(DEFAULT_IMPORT_PRICE)))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.doubleValue()));
    }

    @Test
    @Transactional
    void getMaterialReceiptDetailsByIdFiltering() throws Exception {
        // Initialize the database
        insertedMaterialReceiptDetail = materialReceiptDetailRepository.saveAndFlush(materialReceiptDetail);

        Long id = materialReceiptDetail.getId();

        defaultMaterialReceiptDetailFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMaterialReceiptDetailFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMaterialReceiptDetailFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMaterialReceiptDetailsByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialReceiptDetail = materialReceiptDetailRepository.saveAndFlush(materialReceiptDetail);

        // Get all the materialReceiptDetailList where note equals to
        defaultMaterialReceiptDetailFiltering("note.equals=" + DEFAULT_NOTE, "note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllMaterialReceiptDetailsByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaterialReceiptDetail = materialReceiptDetailRepository.saveAndFlush(materialReceiptDetail);

        // Get all the materialReceiptDetailList where note in
        defaultMaterialReceiptDetailFiltering("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE, "note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllMaterialReceiptDetailsByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaterialReceiptDetail = materialReceiptDetailRepository.saveAndFlush(materialReceiptDetail);

        // Get all the materialReceiptDetailList where note is not null
        defaultMaterialReceiptDetailFiltering("note.specified=true", "note.specified=false");
    }

    @Test
    @Transactional
    void getAllMaterialReceiptDetailsByNoteContainsSomething() throws Exception {
        // Initialize the database
        insertedMaterialReceiptDetail = materialReceiptDetailRepository.saveAndFlush(materialReceiptDetail);

        // Get all the materialReceiptDetailList where note contains
        defaultMaterialReceiptDetailFiltering("note.contains=" + DEFAULT_NOTE, "note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllMaterialReceiptDetailsByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMaterialReceiptDetail = materialReceiptDetailRepository.saveAndFlush(materialReceiptDetail);

        // Get all the materialReceiptDetailList where note does not contain
        defaultMaterialReceiptDetailFiltering("note.doesNotContain=" + UPDATED_NOTE, "note.doesNotContain=" + DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void getAllMaterialReceiptDetailsByImportPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialReceiptDetail = materialReceiptDetailRepository.saveAndFlush(materialReceiptDetail);

        // Get all the materialReceiptDetailList where importPrice equals to
        defaultMaterialReceiptDetailFiltering("importPrice.equals=" + DEFAULT_IMPORT_PRICE, "importPrice.equals=" + UPDATED_IMPORT_PRICE);
    }

    @Test
    @Transactional
    void getAllMaterialReceiptDetailsByImportPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaterialReceiptDetail = materialReceiptDetailRepository.saveAndFlush(materialReceiptDetail);

        // Get all the materialReceiptDetailList where importPrice in
        defaultMaterialReceiptDetailFiltering(
            "importPrice.in=" + DEFAULT_IMPORT_PRICE + "," + UPDATED_IMPORT_PRICE,
            "importPrice.in=" + UPDATED_IMPORT_PRICE
        );
    }

    @Test
    @Transactional
    void getAllMaterialReceiptDetailsByImportPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaterialReceiptDetail = materialReceiptDetailRepository.saveAndFlush(materialReceiptDetail);

        // Get all the materialReceiptDetailList where importPrice is not null
        defaultMaterialReceiptDetailFiltering("importPrice.specified=true", "importPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllMaterialReceiptDetailsByImportPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialReceiptDetail = materialReceiptDetailRepository.saveAndFlush(materialReceiptDetail);

        // Get all the materialReceiptDetailList where importPrice is greater than or equal to
        defaultMaterialReceiptDetailFiltering(
            "importPrice.greaterThanOrEqual=" + DEFAULT_IMPORT_PRICE,
            "importPrice.greaterThanOrEqual=" + UPDATED_IMPORT_PRICE
        );
    }

    @Test
    @Transactional
    void getAllMaterialReceiptDetailsByImportPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialReceiptDetail = materialReceiptDetailRepository.saveAndFlush(materialReceiptDetail);

        // Get all the materialReceiptDetailList where importPrice is less than or equal to
        defaultMaterialReceiptDetailFiltering(
            "importPrice.lessThanOrEqual=" + DEFAULT_IMPORT_PRICE,
            "importPrice.lessThanOrEqual=" + SMALLER_IMPORT_PRICE
        );
    }

    @Test
    @Transactional
    void getAllMaterialReceiptDetailsByImportPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMaterialReceiptDetail = materialReceiptDetailRepository.saveAndFlush(materialReceiptDetail);

        // Get all the materialReceiptDetailList where importPrice is less than
        defaultMaterialReceiptDetailFiltering(
            "importPrice.lessThan=" + UPDATED_IMPORT_PRICE,
            "importPrice.lessThan=" + DEFAULT_IMPORT_PRICE
        );
    }

    @Test
    @Transactional
    void getAllMaterialReceiptDetailsByImportPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMaterialReceiptDetail = materialReceiptDetailRepository.saveAndFlush(materialReceiptDetail);

        // Get all the materialReceiptDetailList where importPrice is greater than
        defaultMaterialReceiptDetailFiltering(
            "importPrice.greaterThan=" + SMALLER_IMPORT_PRICE,
            "importPrice.greaterThan=" + DEFAULT_IMPORT_PRICE
        );
    }

    @Test
    @Transactional
    void getAllMaterialReceiptDetailsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialReceiptDetail = materialReceiptDetailRepository.saveAndFlush(materialReceiptDetail);

        // Get all the materialReceiptDetailList where quantity equals to
        defaultMaterialReceiptDetailFiltering("quantity.equals=" + DEFAULT_QUANTITY, "quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllMaterialReceiptDetailsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaterialReceiptDetail = materialReceiptDetailRepository.saveAndFlush(materialReceiptDetail);

        // Get all the materialReceiptDetailList where quantity in
        defaultMaterialReceiptDetailFiltering(
            "quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY,
            "quantity.in=" + UPDATED_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllMaterialReceiptDetailsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaterialReceiptDetail = materialReceiptDetailRepository.saveAndFlush(materialReceiptDetail);

        // Get all the materialReceiptDetailList where quantity is not null
        defaultMaterialReceiptDetailFiltering("quantity.specified=true", "quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllMaterialReceiptDetailsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialReceiptDetail = materialReceiptDetailRepository.saveAndFlush(materialReceiptDetail);

        // Get all the materialReceiptDetailList where quantity is greater than or equal to
        defaultMaterialReceiptDetailFiltering(
            "quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY,
            "quantity.greaterThanOrEqual=" + UPDATED_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllMaterialReceiptDetailsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialReceiptDetail = materialReceiptDetailRepository.saveAndFlush(materialReceiptDetail);

        // Get all the materialReceiptDetailList where quantity is less than or equal to
        defaultMaterialReceiptDetailFiltering(
            "quantity.lessThanOrEqual=" + DEFAULT_QUANTITY,
            "quantity.lessThanOrEqual=" + SMALLER_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllMaterialReceiptDetailsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMaterialReceiptDetail = materialReceiptDetailRepository.saveAndFlush(materialReceiptDetail);

        // Get all the materialReceiptDetailList where quantity is less than
        defaultMaterialReceiptDetailFiltering("quantity.lessThan=" + UPDATED_QUANTITY, "quantity.lessThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllMaterialReceiptDetailsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMaterialReceiptDetail = materialReceiptDetailRepository.saveAndFlush(materialReceiptDetail);

        // Get all the materialReceiptDetailList where quantity is greater than
        defaultMaterialReceiptDetailFiltering("quantity.greaterThan=" + SMALLER_QUANTITY, "quantity.greaterThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllMaterialReceiptDetailsByMaterialIsEqualToSomething() throws Exception {
        Material material;
        if (TestUtil.findAll(em, Material.class).isEmpty()) {
            materialReceiptDetailRepository.saveAndFlush(materialReceiptDetail);
            material = MaterialResourceIT.createEntity(em);
        } else {
            material = TestUtil.findAll(em, Material.class).get(0);
        }
        em.persist(material);
        em.flush();
        materialReceiptDetail.setMaterial(material);
        materialReceiptDetailRepository.saveAndFlush(materialReceiptDetail);
        Long materialId = material.getId();
        // Get all the materialReceiptDetailList where material equals to materialId
        defaultMaterialReceiptDetailShouldBeFound("materialId.equals=" + materialId);

        // Get all the materialReceiptDetailList where material equals to (materialId + 1)
        defaultMaterialReceiptDetailShouldNotBeFound("materialId.equals=" + (materialId + 1));
    }

    @Test
    @Transactional
    void getAllMaterialReceiptDetailsByReceiptIsEqualToSomething() throws Exception {
        MaterialReceipt receipt;
        if (TestUtil.findAll(em, MaterialReceipt.class).isEmpty()) {
            materialReceiptDetailRepository.saveAndFlush(materialReceiptDetail);
            receipt = MaterialReceiptResourceIT.createEntity(em);
        } else {
            receipt = TestUtil.findAll(em, MaterialReceipt.class).get(0);
        }
        em.persist(receipt);
        em.flush();
        materialReceiptDetail.setReceipt(receipt);
        materialReceiptDetailRepository.saveAndFlush(materialReceiptDetail);
        Long receiptId = receipt.getId();
        // Get all the materialReceiptDetailList where receipt equals to receiptId
        defaultMaterialReceiptDetailShouldBeFound("receiptId.equals=" + receiptId);

        // Get all the materialReceiptDetailList where receipt equals to (receiptId + 1)
        defaultMaterialReceiptDetailShouldNotBeFound("receiptId.equals=" + (receiptId + 1));
    }

    private void defaultMaterialReceiptDetailFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMaterialReceiptDetailShouldBeFound(shouldBeFound);
        defaultMaterialReceiptDetailShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMaterialReceiptDetailShouldBeFound(String filter) throws Exception {
        restMaterialReceiptDetailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(materialReceiptDetail.getId().intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].importPrice").value(hasItem(sameNumber(DEFAULT_IMPORT_PRICE))))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.doubleValue())));

        // Check, that the count call also returns 1
        restMaterialReceiptDetailMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMaterialReceiptDetailShouldNotBeFound(String filter) throws Exception {
        restMaterialReceiptDetailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMaterialReceiptDetailMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMaterialReceiptDetail() throws Exception {
        // Get the materialReceiptDetail
        restMaterialReceiptDetailMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMaterialReceiptDetail() throws Exception {
        // Initialize the database
        insertedMaterialReceiptDetail = materialReceiptDetailRepository.saveAndFlush(materialReceiptDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the materialReceiptDetail
        MaterialReceiptDetail updatedMaterialReceiptDetail = materialReceiptDetailRepository
            .findById(materialReceiptDetail.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedMaterialReceiptDetail are not directly saved in db
        em.detach(updatedMaterialReceiptDetail);
        updatedMaterialReceiptDetail.note(UPDATED_NOTE).importPrice(UPDATED_IMPORT_PRICE).quantity(UPDATED_QUANTITY);
        MaterialReceiptDetailDTO materialReceiptDetailDTO = materialReceiptDetailMapper.toDto(updatedMaterialReceiptDetail);

        restMaterialReceiptDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, materialReceiptDetailDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materialReceiptDetailDTO))
            )
            .andExpect(status().isOk());

        // Validate the MaterialReceiptDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMaterialReceiptDetailToMatchAllProperties(updatedMaterialReceiptDetail);
    }

    @Test
    @Transactional
    void putNonExistingMaterialReceiptDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materialReceiptDetail.setId(longCount.incrementAndGet());

        // Create the MaterialReceiptDetail
        MaterialReceiptDetailDTO materialReceiptDetailDTO = materialReceiptDetailMapper.toDto(materialReceiptDetail);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterialReceiptDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, materialReceiptDetailDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materialReceiptDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaterialReceiptDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMaterialReceiptDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materialReceiptDetail.setId(longCount.incrementAndGet());

        // Create the MaterialReceiptDetail
        MaterialReceiptDetailDTO materialReceiptDetailDTO = materialReceiptDetailMapper.toDto(materialReceiptDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialReceiptDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materialReceiptDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaterialReceiptDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMaterialReceiptDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materialReceiptDetail.setId(longCount.incrementAndGet());

        // Create the MaterialReceiptDetail
        MaterialReceiptDetailDTO materialReceiptDetailDTO = materialReceiptDetailMapper.toDto(materialReceiptDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialReceiptDetailMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materialReceiptDetailDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MaterialReceiptDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMaterialReceiptDetailWithPatch() throws Exception {
        // Initialize the database
        insertedMaterialReceiptDetail = materialReceiptDetailRepository.saveAndFlush(materialReceiptDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the materialReceiptDetail using partial update
        MaterialReceiptDetail partialUpdatedMaterialReceiptDetail = new MaterialReceiptDetail();
        partialUpdatedMaterialReceiptDetail.setId(materialReceiptDetail.getId());

        partialUpdatedMaterialReceiptDetail.note(UPDATED_NOTE).importPrice(UPDATED_IMPORT_PRICE).quantity(UPDATED_QUANTITY);

        restMaterialReceiptDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaterialReceiptDetail.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMaterialReceiptDetail))
            )
            .andExpect(status().isOk());

        // Validate the MaterialReceiptDetail in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMaterialReceiptDetailUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMaterialReceiptDetail, materialReceiptDetail),
            getPersistedMaterialReceiptDetail(materialReceiptDetail)
        );
    }

    @Test
    @Transactional
    void fullUpdateMaterialReceiptDetailWithPatch() throws Exception {
        // Initialize the database
        insertedMaterialReceiptDetail = materialReceiptDetailRepository.saveAndFlush(materialReceiptDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the materialReceiptDetail using partial update
        MaterialReceiptDetail partialUpdatedMaterialReceiptDetail = new MaterialReceiptDetail();
        partialUpdatedMaterialReceiptDetail.setId(materialReceiptDetail.getId());

        partialUpdatedMaterialReceiptDetail.note(UPDATED_NOTE).importPrice(UPDATED_IMPORT_PRICE).quantity(UPDATED_QUANTITY);

        restMaterialReceiptDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaterialReceiptDetail.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMaterialReceiptDetail))
            )
            .andExpect(status().isOk());

        // Validate the MaterialReceiptDetail in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMaterialReceiptDetailUpdatableFieldsEquals(
            partialUpdatedMaterialReceiptDetail,
            getPersistedMaterialReceiptDetail(partialUpdatedMaterialReceiptDetail)
        );
    }

    @Test
    @Transactional
    void patchNonExistingMaterialReceiptDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materialReceiptDetail.setId(longCount.incrementAndGet());

        // Create the MaterialReceiptDetail
        MaterialReceiptDetailDTO materialReceiptDetailDTO = materialReceiptDetailMapper.toDto(materialReceiptDetail);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterialReceiptDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, materialReceiptDetailDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(materialReceiptDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaterialReceiptDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMaterialReceiptDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materialReceiptDetail.setId(longCount.incrementAndGet());

        // Create the MaterialReceiptDetail
        MaterialReceiptDetailDTO materialReceiptDetailDTO = materialReceiptDetailMapper.toDto(materialReceiptDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialReceiptDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(materialReceiptDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaterialReceiptDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMaterialReceiptDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materialReceiptDetail.setId(longCount.incrementAndGet());

        // Create the MaterialReceiptDetail
        MaterialReceiptDetailDTO materialReceiptDetailDTO = materialReceiptDetailMapper.toDto(materialReceiptDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialReceiptDetailMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(materialReceiptDetailDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MaterialReceiptDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMaterialReceiptDetail() throws Exception {
        // Initialize the database
        insertedMaterialReceiptDetail = materialReceiptDetailRepository.saveAndFlush(materialReceiptDetail);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the materialReceiptDetail
        restMaterialReceiptDetailMockMvc
            .perform(delete(ENTITY_API_URL_ID, materialReceiptDetail.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return materialReceiptDetailRepository.count();
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

    protected MaterialReceiptDetail getPersistedMaterialReceiptDetail(MaterialReceiptDetail materialReceiptDetail) {
        return materialReceiptDetailRepository.findById(materialReceiptDetail.getId()).orElseThrow();
    }

    protected void assertPersistedMaterialReceiptDetailToMatchAllProperties(MaterialReceiptDetail expectedMaterialReceiptDetail) {
        assertMaterialReceiptDetailAllPropertiesEquals(
            expectedMaterialReceiptDetail,
            getPersistedMaterialReceiptDetail(expectedMaterialReceiptDetail)
        );
    }

    protected void assertPersistedMaterialReceiptDetailToMatchUpdatableProperties(MaterialReceiptDetail expectedMaterialReceiptDetail) {
        assertMaterialReceiptDetailAllUpdatablePropertiesEquals(
            expectedMaterialReceiptDetail,
            getPersistedMaterialReceiptDetail(expectedMaterialReceiptDetail)
        );
    }
}
