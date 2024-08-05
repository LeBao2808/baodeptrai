package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.MaterialExportDetailAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Material;
import com.mycompany.myapp.domain.MaterialExport;
import com.mycompany.myapp.domain.MaterialExportDetail;
import com.mycompany.myapp.repository.MaterialExportDetailRepository;
import com.mycompany.myapp.service.dto.MaterialExportDetailDTO;
import com.mycompany.myapp.service.mapper.MaterialExportDetailMapper;
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
 * Integration tests for the {@link MaterialExportDetailResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MaterialExportDetailResourceIT {

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_EXPORT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_EXPORT_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_EXPORT_PRICE = new BigDecimal(1 - 1);

    private static final Float DEFAULT_QUANTITY = 1F;
    private static final Float UPDATED_QUANTITY = 2F;
    private static final Float SMALLER_QUANTITY = 1F - 1F;

    private static final String ENTITY_API_URL = "/api/material-export-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MaterialExportDetailRepository materialExportDetailRepository;

    @Autowired
    private MaterialExportDetailMapper materialExportDetailMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMaterialExportDetailMockMvc;

    private MaterialExportDetail materialExportDetail;

    private MaterialExportDetail insertedMaterialExportDetail;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaterialExportDetail createEntity(EntityManager em) {
        MaterialExportDetail materialExportDetail = new MaterialExportDetail()
            .note(DEFAULT_NOTE)
            .exportPrice(DEFAULT_EXPORT_PRICE)
            .quantity(DEFAULT_QUANTITY);
        return materialExportDetail;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaterialExportDetail createUpdatedEntity(EntityManager em) {
        MaterialExportDetail materialExportDetail = new MaterialExportDetail()
            .note(UPDATED_NOTE)
            .exportPrice(UPDATED_EXPORT_PRICE)
            .quantity(UPDATED_QUANTITY);
        return materialExportDetail;
    }

    @BeforeEach
    public void initTest() {
        materialExportDetail = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedMaterialExportDetail != null) {
            materialExportDetailRepository.delete(insertedMaterialExportDetail);
            insertedMaterialExportDetail = null;
        }
    }

    @Test
    @Transactional
    void createMaterialExportDetail() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MaterialExportDetail
        MaterialExportDetailDTO materialExportDetailDTO = materialExportDetailMapper.toDto(materialExportDetail);
        var returnedMaterialExportDetailDTO = om.readValue(
            restMaterialExportDetailMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materialExportDetailDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MaterialExportDetailDTO.class
        );

        // Validate the MaterialExportDetail in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMaterialExportDetail = materialExportDetailMapper.toEntity(returnedMaterialExportDetailDTO);
        assertMaterialExportDetailUpdatableFieldsEquals(
            returnedMaterialExportDetail,
            getPersistedMaterialExportDetail(returnedMaterialExportDetail)
        );

        insertedMaterialExportDetail = returnedMaterialExportDetail;
    }

    @Test
    @Transactional
    void createMaterialExportDetailWithExistingId() throws Exception {
        // Create the MaterialExportDetail with an existing ID
        materialExportDetail.setId(1L);
        MaterialExportDetailDTO materialExportDetailDTO = materialExportDetailMapper.toDto(materialExportDetail);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaterialExportDetailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materialExportDetailDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MaterialExportDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMaterialExportDetails() throws Exception {
        // Initialize the database
        insertedMaterialExportDetail = materialExportDetailRepository.saveAndFlush(materialExportDetail);

        // Get all the materialExportDetailList
        restMaterialExportDetailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(materialExportDetail.getId().intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].exportPrice").value(hasItem(sameNumber(DEFAULT_EXPORT_PRICE))))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.doubleValue())));
    }

    @Test
    @Transactional
    void getMaterialExportDetail() throws Exception {
        // Initialize the database
        insertedMaterialExportDetail = materialExportDetailRepository.saveAndFlush(materialExportDetail);

        // Get the materialExportDetail
        restMaterialExportDetailMockMvc
            .perform(get(ENTITY_API_URL_ID, materialExportDetail.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(materialExportDetail.getId().intValue()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.exportPrice").value(sameNumber(DEFAULT_EXPORT_PRICE)))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.doubleValue()));
    }

    @Test
    @Transactional
    void getMaterialExportDetailsByIdFiltering() throws Exception {
        // Initialize the database
        insertedMaterialExportDetail = materialExportDetailRepository.saveAndFlush(materialExportDetail);

        Long id = materialExportDetail.getId();

        defaultMaterialExportDetailFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMaterialExportDetailFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMaterialExportDetailFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMaterialExportDetailsByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialExportDetail = materialExportDetailRepository.saveAndFlush(materialExportDetail);

        // Get all the materialExportDetailList where note equals to
        defaultMaterialExportDetailFiltering("note.equals=" + DEFAULT_NOTE, "note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllMaterialExportDetailsByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaterialExportDetail = materialExportDetailRepository.saveAndFlush(materialExportDetail);

        // Get all the materialExportDetailList where note in
        defaultMaterialExportDetailFiltering("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE, "note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllMaterialExportDetailsByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaterialExportDetail = materialExportDetailRepository.saveAndFlush(materialExportDetail);

        // Get all the materialExportDetailList where note is not null
        defaultMaterialExportDetailFiltering("note.specified=true", "note.specified=false");
    }

    @Test
    @Transactional
    void getAllMaterialExportDetailsByNoteContainsSomething() throws Exception {
        // Initialize the database
        insertedMaterialExportDetail = materialExportDetailRepository.saveAndFlush(materialExportDetail);

        // Get all the materialExportDetailList where note contains
        defaultMaterialExportDetailFiltering("note.contains=" + DEFAULT_NOTE, "note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllMaterialExportDetailsByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMaterialExportDetail = materialExportDetailRepository.saveAndFlush(materialExportDetail);

        // Get all the materialExportDetailList where note does not contain
        defaultMaterialExportDetailFiltering("note.doesNotContain=" + UPDATED_NOTE, "note.doesNotContain=" + DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void getAllMaterialExportDetailsByExportPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialExportDetail = materialExportDetailRepository.saveAndFlush(materialExportDetail);

        // Get all the materialExportDetailList where exportPrice equals to
        defaultMaterialExportDetailFiltering("exportPrice.equals=" + DEFAULT_EXPORT_PRICE, "exportPrice.equals=" + UPDATED_EXPORT_PRICE);
    }

    @Test
    @Transactional
    void getAllMaterialExportDetailsByExportPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaterialExportDetail = materialExportDetailRepository.saveAndFlush(materialExportDetail);

        // Get all the materialExportDetailList where exportPrice in
        defaultMaterialExportDetailFiltering(
            "exportPrice.in=" + DEFAULT_EXPORT_PRICE + "," + UPDATED_EXPORT_PRICE,
            "exportPrice.in=" + UPDATED_EXPORT_PRICE
        );
    }

    @Test
    @Transactional
    void getAllMaterialExportDetailsByExportPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaterialExportDetail = materialExportDetailRepository.saveAndFlush(materialExportDetail);

        // Get all the materialExportDetailList where exportPrice is not null
        defaultMaterialExportDetailFiltering("exportPrice.specified=true", "exportPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllMaterialExportDetailsByExportPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialExportDetail = materialExportDetailRepository.saveAndFlush(materialExportDetail);

        // Get all the materialExportDetailList where exportPrice is greater than or equal to
        defaultMaterialExportDetailFiltering(
            "exportPrice.greaterThanOrEqual=" + DEFAULT_EXPORT_PRICE,
            "exportPrice.greaterThanOrEqual=" + UPDATED_EXPORT_PRICE
        );
    }

    @Test
    @Transactional
    void getAllMaterialExportDetailsByExportPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialExportDetail = materialExportDetailRepository.saveAndFlush(materialExportDetail);

        // Get all the materialExportDetailList where exportPrice is less than or equal to
        defaultMaterialExportDetailFiltering(
            "exportPrice.lessThanOrEqual=" + DEFAULT_EXPORT_PRICE,
            "exportPrice.lessThanOrEqual=" + SMALLER_EXPORT_PRICE
        );
    }

    @Test
    @Transactional
    void getAllMaterialExportDetailsByExportPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMaterialExportDetail = materialExportDetailRepository.saveAndFlush(materialExportDetail);

        // Get all the materialExportDetailList where exportPrice is less than
        defaultMaterialExportDetailFiltering(
            "exportPrice.lessThan=" + UPDATED_EXPORT_PRICE,
            "exportPrice.lessThan=" + DEFAULT_EXPORT_PRICE
        );
    }

    @Test
    @Transactional
    void getAllMaterialExportDetailsByExportPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMaterialExportDetail = materialExportDetailRepository.saveAndFlush(materialExportDetail);

        // Get all the materialExportDetailList where exportPrice is greater than
        defaultMaterialExportDetailFiltering(
            "exportPrice.greaterThan=" + SMALLER_EXPORT_PRICE,
            "exportPrice.greaterThan=" + DEFAULT_EXPORT_PRICE
        );
    }

    @Test
    @Transactional
    void getAllMaterialExportDetailsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialExportDetail = materialExportDetailRepository.saveAndFlush(materialExportDetail);

        // Get all the materialExportDetailList where quantity equals to
        defaultMaterialExportDetailFiltering("quantity.equals=" + DEFAULT_QUANTITY, "quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllMaterialExportDetailsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaterialExportDetail = materialExportDetailRepository.saveAndFlush(materialExportDetail);

        // Get all the materialExportDetailList where quantity in
        defaultMaterialExportDetailFiltering("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY, "quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllMaterialExportDetailsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaterialExportDetail = materialExportDetailRepository.saveAndFlush(materialExportDetail);

        // Get all the materialExportDetailList where quantity is not null
        defaultMaterialExportDetailFiltering("quantity.specified=true", "quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllMaterialExportDetailsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialExportDetail = materialExportDetailRepository.saveAndFlush(materialExportDetail);

        // Get all the materialExportDetailList where quantity is greater than or equal to
        defaultMaterialExportDetailFiltering(
            "quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY,
            "quantity.greaterThanOrEqual=" + UPDATED_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllMaterialExportDetailsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialExportDetail = materialExportDetailRepository.saveAndFlush(materialExportDetail);

        // Get all the materialExportDetailList where quantity is less than or equal to
        defaultMaterialExportDetailFiltering(
            "quantity.lessThanOrEqual=" + DEFAULT_QUANTITY,
            "quantity.lessThanOrEqual=" + SMALLER_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllMaterialExportDetailsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMaterialExportDetail = materialExportDetailRepository.saveAndFlush(materialExportDetail);

        // Get all the materialExportDetailList where quantity is less than
        defaultMaterialExportDetailFiltering("quantity.lessThan=" + UPDATED_QUANTITY, "quantity.lessThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllMaterialExportDetailsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMaterialExportDetail = materialExportDetailRepository.saveAndFlush(materialExportDetail);

        // Get all the materialExportDetailList where quantity is greater than
        defaultMaterialExportDetailFiltering("quantity.greaterThan=" + SMALLER_QUANTITY, "quantity.greaterThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllMaterialExportDetailsByMaterialExportIsEqualToSomething() throws Exception {
        MaterialExport materialExport;
        if (TestUtil.findAll(em, MaterialExport.class).isEmpty()) {
            materialExportDetailRepository.saveAndFlush(materialExportDetail);
            materialExport = MaterialExportResourceIT.createEntity(em);
        } else {
            materialExport = TestUtil.findAll(em, MaterialExport.class).get(0);
        }
        em.persist(materialExport);
        em.flush();
        materialExportDetail.setMaterialExport(materialExport);
        materialExportDetailRepository.saveAndFlush(materialExportDetail);
        Long materialExportId = materialExport.getId();
        // Get all the materialExportDetailList where materialExport equals to materialExportId
        defaultMaterialExportDetailShouldBeFound("materialExportId.equals=" + materialExportId);

        // Get all the materialExportDetailList where materialExport equals to (materialExportId + 1)
        defaultMaterialExportDetailShouldNotBeFound("materialExportId.equals=" + (materialExportId + 1));
    }

    @Test
    @Transactional
    void getAllMaterialExportDetailsByMaterialIsEqualToSomething() throws Exception {
        Material material;
        if (TestUtil.findAll(em, Material.class).isEmpty()) {
            materialExportDetailRepository.saveAndFlush(materialExportDetail);
            material = MaterialResourceIT.createEntity(em);
        } else {
            material = TestUtil.findAll(em, Material.class).get(0);
        }
        em.persist(material);
        em.flush();
        materialExportDetail.setMaterial(material);
        materialExportDetailRepository.saveAndFlush(materialExportDetail);
        Long materialId = material.getId();
        // Get all the materialExportDetailList where material equals to materialId
        defaultMaterialExportDetailShouldBeFound("materialId.equals=" + materialId);

        // Get all the materialExportDetailList where material equals to (materialId + 1)
        defaultMaterialExportDetailShouldNotBeFound("materialId.equals=" + (materialId + 1));
    }

    private void defaultMaterialExportDetailFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMaterialExportDetailShouldBeFound(shouldBeFound);
        defaultMaterialExportDetailShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMaterialExportDetailShouldBeFound(String filter) throws Exception {
        restMaterialExportDetailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(materialExportDetail.getId().intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].exportPrice").value(hasItem(sameNumber(DEFAULT_EXPORT_PRICE))))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.doubleValue())));

        // Check, that the count call also returns 1
        restMaterialExportDetailMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMaterialExportDetailShouldNotBeFound(String filter) throws Exception {
        restMaterialExportDetailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMaterialExportDetailMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMaterialExportDetail() throws Exception {
        // Get the materialExportDetail
        restMaterialExportDetailMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMaterialExportDetail() throws Exception {
        // Initialize the database
        insertedMaterialExportDetail = materialExportDetailRepository.saveAndFlush(materialExportDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the materialExportDetail
        MaterialExportDetail updatedMaterialExportDetail = materialExportDetailRepository
            .findById(materialExportDetail.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedMaterialExportDetail are not directly saved in db
        em.detach(updatedMaterialExportDetail);
        updatedMaterialExportDetail.note(UPDATED_NOTE).exportPrice(UPDATED_EXPORT_PRICE).quantity(UPDATED_QUANTITY);
        MaterialExportDetailDTO materialExportDetailDTO = materialExportDetailMapper.toDto(updatedMaterialExportDetail);

        restMaterialExportDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, materialExportDetailDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materialExportDetailDTO))
            )
            .andExpect(status().isOk());

        // Validate the MaterialExportDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMaterialExportDetailToMatchAllProperties(updatedMaterialExportDetail);
    }

    @Test
    @Transactional
    void putNonExistingMaterialExportDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materialExportDetail.setId(longCount.incrementAndGet());

        // Create the MaterialExportDetail
        MaterialExportDetailDTO materialExportDetailDTO = materialExportDetailMapper.toDto(materialExportDetail);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterialExportDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, materialExportDetailDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materialExportDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaterialExportDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMaterialExportDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materialExportDetail.setId(longCount.incrementAndGet());

        // Create the MaterialExportDetail
        MaterialExportDetailDTO materialExportDetailDTO = materialExportDetailMapper.toDto(materialExportDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialExportDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materialExportDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaterialExportDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMaterialExportDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materialExportDetail.setId(longCount.incrementAndGet());

        // Create the MaterialExportDetail
        MaterialExportDetailDTO materialExportDetailDTO = materialExportDetailMapper.toDto(materialExportDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialExportDetailMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materialExportDetailDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MaterialExportDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMaterialExportDetailWithPatch() throws Exception {
        // Initialize the database
        insertedMaterialExportDetail = materialExportDetailRepository.saveAndFlush(materialExportDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the materialExportDetail using partial update
        MaterialExportDetail partialUpdatedMaterialExportDetail = new MaterialExportDetail();
        partialUpdatedMaterialExportDetail.setId(materialExportDetail.getId());

        partialUpdatedMaterialExportDetail.exportPrice(UPDATED_EXPORT_PRICE).quantity(UPDATED_QUANTITY);

        restMaterialExportDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaterialExportDetail.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMaterialExportDetail))
            )
            .andExpect(status().isOk());

        // Validate the MaterialExportDetail in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMaterialExportDetailUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMaterialExportDetail, materialExportDetail),
            getPersistedMaterialExportDetail(materialExportDetail)
        );
    }

    @Test
    @Transactional
    void fullUpdateMaterialExportDetailWithPatch() throws Exception {
        // Initialize the database
        insertedMaterialExportDetail = materialExportDetailRepository.saveAndFlush(materialExportDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the materialExportDetail using partial update
        MaterialExportDetail partialUpdatedMaterialExportDetail = new MaterialExportDetail();
        partialUpdatedMaterialExportDetail.setId(materialExportDetail.getId());

        partialUpdatedMaterialExportDetail.note(UPDATED_NOTE).exportPrice(UPDATED_EXPORT_PRICE).quantity(UPDATED_QUANTITY);

        restMaterialExportDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaterialExportDetail.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMaterialExportDetail))
            )
            .andExpect(status().isOk());

        // Validate the MaterialExportDetail in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMaterialExportDetailUpdatableFieldsEquals(
            partialUpdatedMaterialExportDetail,
            getPersistedMaterialExportDetail(partialUpdatedMaterialExportDetail)
        );
    }

    @Test
    @Transactional
    void patchNonExistingMaterialExportDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materialExportDetail.setId(longCount.incrementAndGet());

        // Create the MaterialExportDetail
        MaterialExportDetailDTO materialExportDetailDTO = materialExportDetailMapper.toDto(materialExportDetail);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterialExportDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, materialExportDetailDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(materialExportDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaterialExportDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMaterialExportDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materialExportDetail.setId(longCount.incrementAndGet());

        // Create the MaterialExportDetail
        MaterialExportDetailDTO materialExportDetailDTO = materialExportDetailMapper.toDto(materialExportDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialExportDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(materialExportDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaterialExportDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMaterialExportDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materialExportDetail.setId(longCount.incrementAndGet());

        // Create the MaterialExportDetail
        MaterialExportDetailDTO materialExportDetailDTO = materialExportDetailMapper.toDto(materialExportDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialExportDetailMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(materialExportDetailDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MaterialExportDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMaterialExportDetail() throws Exception {
        // Initialize the database
        insertedMaterialExportDetail = materialExportDetailRepository.saveAndFlush(materialExportDetail);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the materialExportDetail
        restMaterialExportDetailMockMvc
            .perform(delete(ENTITY_API_URL_ID, materialExportDetail.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return materialExportDetailRepository.count();
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

    protected MaterialExportDetail getPersistedMaterialExportDetail(MaterialExportDetail materialExportDetail) {
        return materialExportDetailRepository.findById(materialExportDetail.getId()).orElseThrow();
    }

    protected void assertPersistedMaterialExportDetailToMatchAllProperties(MaterialExportDetail expectedMaterialExportDetail) {
        assertMaterialExportDetailAllPropertiesEquals(
            expectedMaterialExportDetail,
            getPersistedMaterialExportDetail(expectedMaterialExportDetail)
        );
    }

    protected void assertPersistedMaterialExportDetailToMatchUpdatableProperties(MaterialExportDetail expectedMaterialExportDetail) {
        assertMaterialExportDetailAllUpdatablePropertiesEquals(
            expectedMaterialExportDetail,
            getPersistedMaterialExportDetail(expectedMaterialExportDetail)
        );
    }
}
