package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.MaterialExportAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.MaterialExport;
import com.mycompany.myapp.domain.ProductionSite;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.MaterialExportRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.service.dto.MaterialExportDTO;
import com.mycompany.myapp.service.mapper.MaterialExportMapper;
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
 * Integration tests for the {@link MaterialExportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MaterialExportResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPORT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPORT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;
    private static final Integer SMALLER_STATUS = 1 - 1;

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/material-exports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MaterialExportRepository materialExportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MaterialExportMapper materialExportMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMaterialExportMockMvc;

    private MaterialExport materialExport;

    private MaterialExport insertedMaterialExport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaterialExport createEntity(EntityManager em) {
        MaterialExport materialExport = new MaterialExport()
            .creationDate(DEFAULT_CREATION_DATE)
            .exportDate(DEFAULT_EXPORT_DATE)
            .status(DEFAULT_STATUS)
            .code(DEFAULT_CODE);
        return materialExport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaterialExport createUpdatedEntity(EntityManager em) {
        MaterialExport materialExport = new MaterialExport()
            .creationDate(UPDATED_CREATION_DATE)
            .exportDate(UPDATED_EXPORT_DATE)
            .status(UPDATED_STATUS)
            .code(UPDATED_CODE);
        return materialExport;
    }

    @BeforeEach
    public void initTest() {
        materialExport = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedMaterialExport != null) {
            materialExportRepository.delete(insertedMaterialExport);
            insertedMaterialExport = null;
        }
    }

    @Test
    @Transactional
    void createMaterialExport() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MaterialExport
        MaterialExportDTO materialExportDTO = materialExportMapper.toDto(materialExport);
        var returnedMaterialExportDTO = om.readValue(
            restMaterialExportMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materialExportDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MaterialExportDTO.class
        );

        // Validate the MaterialExport in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMaterialExport = materialExportMapper.toEntity(returnedMaterialExportDTO);
        assertMaterialExportUpdatableFieldsEquals(returnedMaterialExport, getPersistedMaterialExport(returnedMaterialExport));

        insertedMaterialExport = returnedMaterialExport;
    }

    @Test
    @Transactional
    void createMaterialExportWithExistingId() throws Exception {
        // Create the MaterialExport with an existing ID
        materialExport.setId(1L);
        MaterialExportDTO materialExportDTO = materialExportMapper.toDto(materialExport);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaterialExportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materialExportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MaterialExport in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMaterialExports() throws Exception {
        // Initialize the database
        insertedMaterialExport = materialExportRepository.saveAndFlush(materialExport);

        // Get all the materialExportList
        restMaterialExportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(materialExport.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].exportDate").value(hasItem(DEFAULT_EXPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getMaterialExport() throws Exception {
        // Initialize the database
        insertedMaterialExport = materialExportRepository.saveAndFlush(materialExport);

        // Get the materialExport
        restMaterialExportMockMvc
            .perform(get(ENTITY_API_URL_ID, materialExport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(materialExport.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.exportDate").value(DEFAULT_EXPORT_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getMaterialExportsByIdFiltering() throws Exception {
        // Initialize the database
        insertedMaterialExport = materialExportRepository.saveAndFlush(materialExport);

        Long id = materialExport.getId();

        defaultMaterialExportFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMaterialExportFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMaterialExportFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMaterialExportsByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialExport = materialExportRepository.saveAndFlush(materialExport);

        // Get all the materialExportList where creationDate equals to
        defaultMaterialExportFiltering("creationDate.equals=" + DEFAULT_CREATION_DATE, "creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllMaterialExportsByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaterialExport = materialExportRepository.saveAndFlush(materialExport);

        // Get all the materialExportList where creationDate in
        defaultMaterialExportFiltering(
            "creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE,
            "creationDate.in=" + UPDATED_CREATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllMaterialExportsByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaterialExport = materialExportRepository.saveAndFlush(materialExport);

        // Get all the materialExportList where creationDate is not null
        defaultMaterialExportFiltering("creationDate.specified=true", "creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMaterialExportsByExportDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialExport = materialExportRepository.saveAndFlush(materialExport);

        // Get all the materialExportList where exportDate equals to
        defaultMaterialExportFiltering("exportDate.equals=" + DEFAULT_EXPORT_DATE, "exportDate.equals=" + UPDATED_EXPORT_DATE);
    }

    @Test
    @Transactional
    void getAllMaterialExportsByExportDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaterialExport = materialExportRepository.saveAndFlush(materialExport);

        // Get all the materialExportList where exportDate in
        defaultMaterialExportFiltering(
            "exportDate.in=" + DEFAULT_EXPORT_DATE + "," + UPDATED_EXPORT_DATE,
            "exportDate.in=" + UPDATED_EXPORT_DATE
        );
    }

    @Test
    @Transactional
    void getAllMaterialExportsByExportDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaterialExport = materialExportRepository.saveAndFlush(materialExport);

        // Get all the materialExportList where exportDate is not null
        defaultMaterialExportFiltering("exportDate.specified=true", "exportDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMaterialExportsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialExport = materialExportRepository.saveAndFlush(materialExport);

        // Get all the materialExportList where status equals to
        defaultMaterialExportFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMaterialExportsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaterialExport = materialExportRepository.saveAndFlush(materialExport);

        // Get all the materialExportList where status in
        defaultMaterialExportFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMaterialExportsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaterialExport = materialExportRepository.saveAndFlush(materialExport);

        // Get all the materialExportList where status is not null
        defaultMaterialExportFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllMaterialExportsByStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialExport = materialExportRepository.saveAndFlush(materialExport);

        // Get all the materialExportList where status is greater than or equal to
        defaultMaterialExportFiltering("status.greaterThanOrEqual=" + DEFAULT_STATUS, "status.greaterThanOrEqual=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMaterialExportsByStatusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialExport = materialExportRepository.saveAndFlush(materialExport);

        // Get all the materialExportList where status is less than or equal to
        defaultMaterialExportFiltering("status.lessThanOrEqual=" + DEFAULT_STATUS, "status.lessThanOrEqual=" + SMALLER_STATUS);
    }

    @Test
    @Transactional
    void getAllMaterialExportsByStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMaterialExport = materialExportRepository.saveAndFlush(materialExport);

        // Get all the materialExportList where status is less than
        defaultMaterialExportFiltering("status.lessThan=" + UPDATED_STATUS, "status.lessThan=" + DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void getAllMaterialExportsByStatusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMaterialExport = materialExportRepository.saveAndFlush(materialExport);

        // Get all the materialExportList where status is greater than
        defaultMaterialExportFiltering("status.greaterThan=" + SMALLER_STATUS, "status.greaterThan=" + DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void getAllMaterialExportsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialExport = materialExportRepository.saveAndFlush(materialExport);

        // Get all the materialExportList where code equals to
        defaultMaterialExportFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMaterialExportsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaterialExport = materialExportRepository.saveAndFlush(materialExport);

        // Get all the materialExportList where code in
        defaultMaterialExportFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMaterialExportsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaterialExport = materialExportRepository.saveAndFlush(materialExport);

        // Get all the materialExportList where code is not null
        defaultMaterialExportFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllMaterialExportsByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedMaterialExport = materialExportRepository.saveAndFlush(materialExport);

        // Get all the materialExportList where code contains
        defaultMaterialExportFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMaterialExportsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMaterialExport = materialExportRepository.saveAndFlush(materialExport);

        // Get all the materialExportList where code does not contain
        defaultMaterialExportFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllMaterialExportsByCreatedByUserIsEqualToSomething() throws Exception {
        User createdByUser;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            materialExportRepository.saveAndFlush(materialExport);
            createdByUser = UserResourceIT.createEntity(em);
        } else {
            createdByUser = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(createdByUser);
        em.flush();
        materialExport.setCreatedByUser(createdByUser);
        materialExportRepository.saveAndFlush(materialExport);
        Long createdByUserId = createdByUser.getId();
        // Get all the materialExportList where createdByUser equals to createdByUserId
        defaultMaterialExportShouldBeFound("createdByUserId.equals=" + createdByUserId);

        // Get all the materialExportList where createdByUser equals to (createdByUserId + 1)
        defaultMaterialExportShouldNotBeFound("createdByUserId.equals=" + (createdByUserId + 1));
    }

    @Test
    @Transactional
    void getAllMaterialExportsByProductionSiteIsEqualToSomething() throws Exception {
        ProductionSite productionSite;
        if (TestUtil.findAll(em, ProductionSite.class).isEmpty()) {
            materialExportRepository.saveAndFlush(materialExport);
            productionSite = ProductionSiteResourceIT.createEntity(em);
        } else {
            productionSite = TestUtil.findAll(em, ProductionSite.class).get(0);
        }
        em.persist(productionSite);
        em.flush();
        materialExport.setProductionSite(productionSite);
        materialExportRepository.saveAndFlush(materialExport);
        Long productionSiteId = productionSite.getId();
        // Get all the materialExportList where productionSite equals to productionSiteId
        defaultMaterialExportShouldBeFound("productionSiteId.equals=" + productionSiteId);

        // Get all the materialExportList where productionSite equals to (productionSiteId + 1)
        defaultMaterialExportShouldNotBeFound("productionSiteId.equals=" + (productionSiteId + 1));
    }

    private void defaultMaterialExportFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMaterialExportShouldBeFound(shouldBeFound);
        defaultMaterialExportShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMaterialExportShouldBeFound(String filter) throws Exception {
        restMaterialExportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(materialExport.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].exportDate").value(hasItem(DEFAULT_EXPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));

        // Check, that the count call also returns 1
        restMaterialExportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMaterialExportShouldNotBeFound(String filter) throws Exception {
        restMaterialExportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMaterialExportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMaterialExport() throws Exception {
        // Get the materialExport
        restMaterialExportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMaterialExport() throws Exception {
        // Initialize the database
        insertedMaterialExport = materialExportRepository.saveAndFlush(materialExport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the materialExport
        MaterialExport updatedMaterialExport = materialExportRepository.findById(materialExport.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMaterialExport are not directly saved in db
        em.detach(updatedMaterialExport);
        updatedMaterialExport.creationDate(UPDATED_CREATION_DATE).exportDate(UPDATED_EXPORT_DATE).status(UPDATED_STATUS).code(UPDATED_CODE);
        MaterialExportDTO materialExportDTO = materialExportMapper.toDto(updatedMaterialExport);

        restMaterialExportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, materialExportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materialExportDTO))
            )
            .andExpect(status().isOk());

        // Validate the MaterialExport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMaterialExportToMatchAllProperties(updatedMaterialExport);
    }

    @Test
    @Transactional
    void putNonExistingMaterialExport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materialExport.setId(longCount.incrementAndGet());

        // Create the MaterialExport
        MaterialExportDTO materialExportDTO = materialExportMapper.toDto(materialExport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterialExportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, materialExportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materialExportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaterialExport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMaterialExport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materialExport.setId(longCount.incrementAndGet());

        // Create the MaterialExport
        MaterialExportDTO materialExportDTO = materialExportMapper.toDto(materialExport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialExportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materialExportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaterialExport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMaterialExport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materialExport.setId(longCount.incrementAndGet());

        // Create the MaterialExport
        MaterialExportDTO materialExportDTO = materialExportMapper.toDto(materialExport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialExportMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materialExportDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MaterialExport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMaterialExportWithPatch() throws Exception {
        // Initialize the database
        insertedMaterialExport = materialExportRepository.saveAndFlush(materialExport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the materialExport using partial update
        MaterialExport partialUpdatedMaterialExport = new MaterialExport();
        partialUpdatedMaterialExport.setId(materialExport.getId());

        partialUpdatedMaterialExport.status(UPDATED_STATUS);

        restMaterialExportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaterialExport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMaterialExport))
            )
            .andExpect(status().isOk());

        // Validate the MaterialExport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMaterialExportUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMaterialExport, materialExport),
            getPersistedMaterialExport(materialExport)
        );
    }

    @Test
    @Transactional
    void fullUpdateMaterialExportWithPatch() throws Exception {
        // Initialize the database
        insertedMaterialExport = materialExportRepository.saveAndFlush(materialExport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the materialExport using partial update
        MaterialExport partialUpdatedMaterialExport = new MaterialExport();
        partialUpdatedMaterialExport.setId(materialExport.getId());

        partialUpdatedMaterialExport
            .creationDate(UPDATED_CREATION_DATE)
            .exportDate(UPDATED_EXPORT_DATE)
            .status(UPDATED_STATUS)
            .code(UPDATED_CODE);

        restMaterialExportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaterialExport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMaterialExport))
            )
            .andExpect(status().isOk());

        // Validate the MaterialExport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMaterialExportUpdatableFieldsEquals(partialUpdatedMaterialExport, getPersistedMaterialExport(partialUpdatedMaterialExport));
    }

    @Test
    @Transactional
    void patchNonExistingMaterialExport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materialExport.setId(longCount.incrementAndGet());

        // Create the MaterialExport
        MaterialExportDTO materialExportDTO = materialExportMapper.toDto(materialExport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterialExportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, materialExportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(materialExportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaterialExport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMaterialExport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materialExport.setId(longCount.incrementAndGet());

        // Create the MaterialExport
        MaterialExportDTO materialExportDTO = materialExportMapper.toDto(materialExport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialExportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(materialExportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaterialExport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMaterialExport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materialExport.setId(longCount.incrementAndGet());

        // Create the MaterialExport
        MaterialExportDTO materialExportDTO = materialExportMapper.toDto(materialExport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialExportMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(materialExportDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MaterialExport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMaterialExport() throws Exception {
        // Initialize the database
        insertedMaterialExport = materialExportRepository.saveAndFlush(materialExport);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the materialExport
        restMaterialExportMockMvc
            .perform(delete(ENTITY_API_URL_ID, materialExport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return materialExportRepository.count();
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

    protected MaterialExport getPersistedMaterialExport(MaterialExport materialExport) {
        return materialExportRepository.findById(materialExport.getId()).orElseThrow();
    }

    protected void assertPersistedMaterialExportToMatchAllProperties(MaterialExport expectedMaterialExport) {
        assertMaterialExportAllPropertiesEquals(expectedMaterialExport, getPersistedMaterialExport(expectedMaterialExport));
    }

    protected void assertPersistedMaterialExportToMatchUpdatableProperties(MaterialExport expectedMaterialExport) {
        assertMaterialExportAllUpdatablePropertiesEquals(expectedMaterialExport, getPersistedMaterialExport(expectedMaterialExport));
    }
}
