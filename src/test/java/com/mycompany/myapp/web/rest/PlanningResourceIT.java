package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.PlanningAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Planning;
import com.mycompany.myapp.domain.Quantification;
import com.mycompany.myapp.repository.PlanningRepository;
import com.mycompany.myapp.service.dto.PlanningDTO;
import com.mycompany.myapp.service.mapper.PlanningMapper;
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
 * Integration tests for the {@link PlanningResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PlanningResourceIT {

    private static final Instant DEFAULT_ORDER_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;
    private static final Integer SMALLER_STATUS = 1 - 1;

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/plannings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PlanningRepository planningRepository;

    @Autowired
    private PlanningMapper planningMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlanningMockMvc;

    private Planning planning;

    private Planning insertedPlanning;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Planning createEntity(EntityManager em) {
        Planning planning = new Planning().orderCreationDate(DEFAULT_ORDER_CREATION_DATE).status(DEFAULT_STATUS).code(DEFAULT_CODE);
        return planning;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Planning createUpdatedEntity(EntityManager em) {
        Planning planning = new Planning().orderCreationDate(UPDATED_ORDER_CREATION_DATE).status(UPDATED_STATUS).code(UPDATED_CODE);
        return planning;
    }

    @BeforeEach
    public void initTest() {
        planning = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPlanning != null) {
            planningRepository.delete(insertedPlanning);
            insertedPlanning = null;
        }
    }

    @Test
    @Transactional
    void createPlanning() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Planning
        PlanningDTO planningDTO = planningMapper.toDto(planning);
        var returnedPlanningDTO = om.readValue(
            restPlanningMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planningDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PlanningDTO.class
        );

        // Validate the Planning in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPlanning = planningMapper.toEntity(returnedPlanningDTO);
        assertPlanningUpdatableFieldsEquals(returnedPlanning, getPersistedPlanning(returnedPlanning));

        insertedPlanning = returnedPlanning;
    }

    @Test
    @Transactional
    void createPlanningWithExistingId() throws Exception {
        // Create the Planning with an existing ID
        planning.setId(1L);
        PlanningDTO planningDTO = planningMapper.toDto(planning);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlanningMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planningDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Planning in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPlannings() throws Exception {
        // Initialize the database
        insertedPlanning = planningRepository.saveAndFlush(planning);

        // Get all the planningList
        restPlanningMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planning.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderCreationDate").value(hasItem(DEFAULT_ORDER_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getPlanning() throws Exception {
        // Initialize the database
        insertedPlanning = planningRepository.saveAndFlush(planning);

        // Get the planning
        restPlanningMockMvc
            .perform(get(ENTITY_API_URL_ID, planning.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(planning.getId().intValue()))
            .andExpect(jsonPath("$.orderCreationDate").value(DEFAULT_ORDER_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getPlanningsByIdFiltering() throws Exception {
        // Initialize the database
        insertedPlanning = planningRepository.saveAndFlush(planning);

        Long id = planning.getId();

        defaultPlanningFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPlanningFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPlanningFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPlanningsByOrderCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlanning = planningRepository.saveAndFlush(planning);

        // Get all the planningList where orderCreationDate equals to
        defaultPlanningFiltering(
            "orderCreationDate.equals=" + DEFAULT_ORDER_CREATION_DATE,
            "orderCreationDate.equals=" + UPDATED_ORDER_CREATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllPlanningsByOrderCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPlanning = planningRepository.saveAndFlush(planning);

        // Get all the planningList where orderCreationDate in
        defaultPlanningFiltering(
            "orderCreationDate.in=" + DEFAULT_ORDER_CREATION_DATE + "," + UPDATED_ORDER_CREATION_DATE,
            "orderCreationDate.in=" + UPDATED_ORDER_CREATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllPlanningsByOrderCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPlanning = planningRepository.saveAndFlush(planning);

        // Get all the planningList where orderCreationDate is not null
        defaultPlanningFiltering("orderCreationDate.specified=true", "orderCreationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPlanningsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlanning = planningRepository.saveAndFlush(planning);

        // Get all the planningList where status equals to
        defaultPlanningFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllPlanningsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPlanning = planningRepository.saveAndFlush(planning);

        // Get all the planningList where status in
        defaultPlanningFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllPlanningsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPlanning = planningRepository.saveAndFlush(planning);

        // Get all the planningList where status is not null
        defaultPlanningFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllPlanningsByStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlanning = planningRepository.saveAndFlush(planning);

        // Get all the planningList where status is greater than or equal to
        defaultPlanningFiltering("status.greaterThanOrEqual=" + DEFAULT_STATUS, "status.greaterThanOrEqual=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllPlanningsByStatusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlanning = planningRepository.saveAndFlush(planning);

        // Get all the planningList where status is less than or equal to
        defaultPlanningFiltering("status.lessThanOrEqual=" + DEFAULT_STATUS, "status.lessThanOrEqual=" + SMALLER_STATUS);
    }

    @Test
    @Transactional
    void getAllPlanningsByStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPlanning = planningRepository.saveAndFlush(planning);

        // Get all the planningList where status is less than
        defaultPlanningFiltering("status.lessThan=" + UPDATED_STATUS, "status.lessThan=" + DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void getAllPlanningsByStatusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPlanning = planningRepository.saveAndFlush(planning);

        // Get all the planningList where status is greater than
        defaultPlanningFiltering("status.greaterThan=" + SMALLER_STATUS, "status.greaterThan=" + DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void getAllPlanningsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPlanning = planningRepository.saveAndFlush(planning);

        // Get all the planningList where code equals to
        defaultPlanningFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPlanningsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPlanning = planningRepository.saveAndFlush(planning);

        // Get all the planningList where code in
        defaultPlanningFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPlanningsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPlanning = planningRepository.saveAndFlush(planning);

        // Get all the planningList where code is not null
        defaultPlanningFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllPlanningsByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedPlanning = planningRepository.saveAndFlush(planning);

        // Get all the planningList where code contains
        defaultPlanningFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPlanningsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPlanning = planningRepository.saveAndFlush(planning);

        // Get all the planningList where code does not contain
        defaultPlanningFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllPlanningsByQuantificationIsEqualToSomething() throws Exception {
        Quantification quantification;
        if (TestUtil.findAll(em, Quantification.class).isEmpty()) {
            planningRepository.saveAndFlush(planning);
            quantification = QuantificationResourceIT.createEntity(em);
        } else {
            quantification = TestUtil.findAll(em, Quantification.class).get(0);
        }
        em.persist(quantification);
        em.flush();
        planning.setQuantification(quantification);
        planningRepository.saveAndFlush(planning);
        Long quantificationId = quantification.getId();
        // Get all the planningList where quantification equals to quantificationId
        defaultPlanningShouldBeFound("quantificationId.equals=" + quantificationId);

        // Get all the planningList where quantification equals to (quantificationId + 1)
        defaultPlanningShouldNotBeFound("quantificationId.equals=" + (quantificationId + 1));
    }

    private void defaultPlanningFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPlanningShouldBeFound(shouldBeFound);
        defaultPlanningShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPlanningShouldBeFound(String filter) throws Exception {
        restPlanningMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planning.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderCreationDate").value(hasItem(DEFAULT_ORDER_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));

        // Check, that the count call also returns 1
        restPlanningMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPlanningShouldNotBeFound(String filter) throws Exception {
        restPlanningMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPlanningMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPlanning() throws Exception {
        // Get the planning
        restPlanningMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPlanning() throws Exception {
        // Initialize the database
        insertedPlanning = planningRepository.saveAndFlush(planning);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the planning
        Planning updatedPlanning = planningRepository.findById(planning.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPlanning are not directly saved in db
        em.detach(updatedPlanning);
        updatedPlanning.orderCreationDate(UPDATED_ORDER_CREATION_DATE).status(UPDATED_STATUS).code(UPDATED_CODE);
        PlanningDTO planningDTO = planningMapper.toDto(updatedPlanning);

        restPlanningMockMvc
            .perform(
                put(ENTITY_API_URL_ID, planningDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(planningDTO))
            )
            .andExpect(status().isOk());

        // Validate the Planning in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPlanningToMatchAllProperties(updatedPlanning);
    }

    @Test
    @Transactional
    void putNonExistingPlanning() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planning.setId(longCount.incrementAndGet());

        // Create the Planning
        PlanningDTO planningDTO = planningMapper.toDto(planning);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanningMockMvc
            .perform(
                put(ENTITY_API_URL_ID, planningDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(planningDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Planning in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlanning() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planning.setId(longCount.incrementAndGet());

        // Create the Planning
        PlanningDTO planningDTO = planningMapper.toDto(planning);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanningMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(planningDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Planning in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlanning() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planning.setId(longCount.incrementAndGet());

        // Create the Planning
        PlanningDTO planningDTO = planningMapper.toDto(planning);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanningMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planningDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Planning in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlanningWithPatch() throws Exception {
        // Initialize the database
        insertedPlanning = planningRepository.saveAndFlush(planning);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the planning using partial update
        Planning partialUpdatedPlanning = new Planning();
        partialUpdatedPlanning.setId(planning.getId());

        partialUpdatedPlanning.status(UPDATED_STATUS);

        restPlanningMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlanning.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPlanning))
            )
            .andExpect(status().isOk());

        // Validate the Planning in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlanningUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPlanning, planning), getPersistedPlanning(planning));
    }

    @Test
    @Transactional
    void fullUpdatePlanningWithPatch() throws Exception {
        // Initialize the database
        insertedPlanning = planningRepository.saveAndFlush(planning);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the planning using partial update
        Planning partialUpdatedPlanning = new Planning();
        partialUpdatedPlanning.setId(planning.getId());

        partialUpdatedPlanning.orderCreationDate(UPDATED_ORDER_CREATION_DATE).status(UPDATED_STATUS).code(UPDATED_CODE);

        restPlanningMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlanning.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPlanning))
            )
            .andExpect(status().isOk());

        // Validate the Planning in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlanningUpdatableFieldsEquals(partialUpdatedPlanning, getPersistedPlanning(partialUpdatedPlanning));
    }

    @Test
    @Transactional
    void patchNonExistingPlanning() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planning.setId(longCount.incrementAndGet());

        // Create the Planning
        PlanningDTO planningDTO = planningMapper.toDto(planning);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanningMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, planningDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(planningDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Planning in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlanning() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planning.setId(longCount.incrementAndGet());

        // Create the Planning
        PlanningDTO planningDTO = planningMapper.toDto(planning);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanningMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(planningDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Planning in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlanning() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planning.setId(longCount.incrementAndGet());

        // Create the Planning
        PlanningDTO planningDTO = planningMapper.toDto(planning);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanningMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(planningDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Planning in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlanning() throws Exception {
        // Initialize the database
        insertedPlanning = planningRepository.saveAndFlush(planning);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the planning
        restPlanningMockMvc
            .perform(delete(ENTITY_API_URL_ID, planning.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return planningRepository.count();
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

    protected Planning getPersistedPlanning(Planning planning) {
        return planningRepository.findById(planning.getId()).orElseThrow();
    }

    protected void assertPersistedPlanningToMatchAllProperties(Planning expectedPlanning) {
        assertPlanningAllPropertiesEquals(expectedPlanning, getPersistedPlanning(expectedPlanning));
    }

    protected void assertPersistedPlanningToMatchUpdatableProperties(Planning expectedPlanning) {
        assertPlanningAllUpdatablePropertiesEquals(expectedPlanning, getPersistedPlanning(expectedPlanning));
    }
}
