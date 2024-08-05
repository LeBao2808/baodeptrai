package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.QuantificationAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Material;
import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.domain.Quantification;
import com.mycompany.myapp.repository.QuantificationRepository;
import com.mycompany.myapp.service.dto.QuantificationDTO;
import com.mycompany.myapp.service.mapper.QuantificationMapper;
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
 * Integration tests for the {@link QuantificationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuantificationResourceIT {

    private static final Float DEFAULT_QUANTITY = 1F;
    private static final Float UPDATED_QUANTITY = 2F;
    private static final Float SMALLER_QUANTITY = 1F - 1F;

    private static final String ENTITY_API_URL = "/api/quantifications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuantificationRepository quantificationRepository;

    @Autowired
    private QuantificationMapper quantificationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuantificationMockMvc;

    private Quantification quantification;

    private Quantification insertedQuantification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quantification createEntity(EntityManager em) {
        Quantification quantification = new Quantification().quantity(DEFAULT_QUANTITY);
        return quantification;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quantification createUpdatedEntity(EntityManager em) {
        Quantification quantification = new Quantification().quantity(UPDATED_QUANTITY);
        return quantification;
    }

    @BeforeEach
    public void initTest() {
        quantification = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedQuantification != null) {
            quantificationRepository.delete(insertedQuantification);
            insertedQuantification = null;
        }
    }

    @Test
    @Transactional
    void createQuantification() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Quantification
        QuantificationDTO quantificationDTO = quantificationMapper.toDto(quantification);
        var returnedQuantificationDTO = om.readValue(
            restQuantificationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quantificationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            QuantificationDTO.class
        );

        // Validate the Quantification in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedQuantification = quantificationMapper.toEntity(returnedQuantificationDTO);
        assertQuantificationUpdatableFieldsEquals(returnedQuantification, getPersistedQuantification(returnedQuantification));

        insertedQuantification = returnedQuantification;
    }

    @Test
    @Transactional
    void createQuantificationWithExistingId() throws Exception {
        // Create the Quantification with an existing ID
        quantification.setId(1L);
        QuantificationDTO quantificationDTO = quantificationMapper.toDto(quantification);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuantificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quantificationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Quantification in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuantifications() throws Exception {
        // Initialize the database
        insertedQuantification = quantificationRepository.saveAndFlush(quantification);

        // Get all the quantificationList
        restQuantificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quantification.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.doubleValue())));
    }

    @Test
    @Transactional
    void getQuantification() throws Exception {
        // Initialize the database
        insertedQuantification = quantificationRepository.saveAndFlush(quantification);

        // Get the quantification
        restQuantificationMockMvc
            .perform(get(ENTITY_API_URL_ID, quantification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quantification.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.doubleValue()));
    }

    @Test
    @Transactional
    void getQuantificationsByIdFiltering() throws Exception {
        // Initialize the database
        insertedQuantification = quantificationRepository.saveAndFlush(quantification);

        Long id = quantification.getId();

        defaultQuantificationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultQuantificationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultQuantificationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllQuantificationsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuantification = quantificationRepository.saveAndFlush(quantification);

        // Get all the quantificationList where quantity equals to
        defaultQuantificationFiltering("quantity.equals=" + DEFAULT_QUANTITY, "quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllQuantificationsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuantification = quantificationRepository.saveAndFlush(quantification);

        // Get all the quantificationList where quantity in
        defaultQuantificationFiltering("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY, "quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllQuantificationsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuantification = quantificationRepository.saveAndFlush(quantification);

        // Get all the quantificationList where quantity is not null
        defaultQuantificationFiltering("quantity.specified=true", "quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllQuantificationsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuantification = quantificationRepository.saveAndFlush(quantification);

        // Get all the quantificationList where quantity is greater than or equal to
        defaultQuantificationFiltering(
            "quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY,
            "quantity.greaterThanOrEqual=" + UPDATED_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllQuantificationsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuantification = quantificationRepository.saveAndFlush(quantification);

        // Get all the quantificationList where quantity is less than or equal to
        defaultQuantificationFiltering("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY, "quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllQuantificationsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuantification = quantificationRepository.saveAndFlush(quantification);

        // Get all the quantificationList where quantity is less than
        defaultQuantificationFiltering("quantity.lessThan=" + UPDATED_QUANTITY, "quantity.lessThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllQuantificationsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuantification = quantificationRepository.saveAndFlush(quantification);

        // Get all the quantificationList where quantity is greater than
        defaultQuantificationFiltering("quantity.greaterThan=" + SMALLER_QUANTITY, "quantity.greaterThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllQuantificationsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            quantificationRepository.saveAndFlush(quantification);
            product = ProductResourceIT.createEntity(em);
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product);
        em.flush();
        quantification.setProduct(product);
        quantificationRepository.saveAndFlush(quantification);
        Long productId = product.getId();
        // Get all the quantificationList where product equals to productId
        defaultQuantificationShouldBeFound("productId.equals=" + productId);

        // Get all the quantificationList where product equals to (productId + 1)
        defaultQuantificationShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllQuantificationsByMaterialIsEqualToSomething() throws Exception {
        Material material;
        if (TestUtil.findAll(em, Material.class).isEmpty()) {
            quantificationRepository.saveAndFlush(quantification);
            material = MaterialResourceIT.createEntity(em);
        } else {
            material = TestUtil.findAll(em, Material.class).get(0);
        }
        em.persist(material);
        em.flush();
        quantification.setMaterial(material);
        quantificationRepository.saveAndFlush(quantification);
        Long materialId = material.getId();
        // Get all the quantificationList where material equals to materialId
        defaultQuantificationShouldBeFound("materialId.equals=" + materialId);

        // Get all the quantificationList where material equals to (materialId + 1)
        defaultQuantificationShouldNotBeFound("materialId.equals=" + (materialId + 1));
    }

    private void defaultQuantificationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultQuantificationShouldBeFound(shouldBeFound);
        defaultQuantificationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQuantificationShouldBeFound(String filter) throws Exception {
        restQuantificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quantification.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.doubleValue())));

        // Check, that the count call also returns 1
        restQuantificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQuantificationShouldNotBeFound(String filter) throws Exception {
        restQuantificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuantificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingQuantification() throws Exception {
        // Get the quantification
        restQuantificationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuantification() throws Exception {
        // Initialize the database
        insertedQuantification = quantificationRepository.saveAndFlush(quantification);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quantification
        Quantification updatedQuantification = quantificationRepository.findById(quantification.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuantification are not directly saved in db
        em.detach(updatedQuantification);
        updatedQuantification.quantity(UPDATED_QUANTITY);
        QuantificationDTO quantificationDTO = quantificationMapper.toDto(updatedQuantification);

        restQuantificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quantificationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quantificationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Quantification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQuantificationToMatchAllProperties(updatedQuantification);
    }

    @Test
    @Transactional
    void putNonExistingQuantification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quantification.setId(longCount.incrementAndGet());

        // Create the Quantification
        QuantificationDTO quantificationDTO = quantificationMapper.toDto(quantification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuantificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quantificationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quantificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quantification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuantification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quantification.setId(longCount.incrementAndGet());

        // Create the Quantification
        QuantificationDTO quantificationDTO = quantificationMapper.toDto(quantification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuantificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quantificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quantification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuantification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quantification.setId(longCount.incrementAndGet());

        // Create the Quantification
        QuantificationDTO quantificationDTO = quantificationMapper.toDto(quantification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuantificationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quantificationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Quantification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuantificationWithPatch() throws Exception {
        // Initialize the database
        insertedQuantification = quantificationRepository.saveAndFlush(quantification);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quantification using partial update
        Quantification partialUpdatedQuantification = new Quantification();
        partialUpdatedQuantification.setId(quantification.getId());

        partialUpdatedQuantification.quantity(UPDATED_QUANTITY);

        restQuantificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuantification.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuantification))
            )
            .andExpect(status().isOk());

        // Validate the Quantification in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuantificationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedQuantification, quantification),
            getPersistedQuantification(quantification)
        );
    }

    @Test
    @Transactional
    void fullUpdateQuantificationWithPatch() throws Exception {
        // Initialize the database
        insertedQuantification = quantificationRepository.saveAndFlush(quantification);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quantification using partial update
        Quantification partialUpdatedQuantification = new Quantification();
        partialUpdatedQuantification.setId(quantification.getId());

        partialUpdatedQuantification.quantity(UPDATED_QUANTITY);

        restQuantificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuantification.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuantification))
            )
            .andExpect(status().isOk());

        // Validate the Quantification in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuantificationUpdatableFieldsEquals(partialUpdatedQuantification, getPersistedQuantification(partialUpdatedQuantification));
    }

    @Test
    @Transactional
    void patchNonExistingQuantification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quantification.setId(longCount.incrementAndGet());

        // Create the Quantification
        QuantificationDTO quantificationDTO = quantificationMapper.toDto(quantification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuantificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quantificationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quantificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quantification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuantification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quantification.setId(longCount.incrementAndGet());

        // Create the Quantification
        QuantificationDTO quantificationDTO = quantificationMapper.toDto(quantification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuantificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quantificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quantification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuantification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quantification.setId(longCount.incrementAndGet());

        // Create the Quantification
        QuantificationDTO quantificationDTO = quantificationMapper.toDto(quantification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuantificationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(quantificationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Quantification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuantification() throws Exception {
        // Initialize the database
        insertedQuantification = quantificationRepository.saveAndFlush(quantification);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the quantification
        restQuantificationMockMvc
            .perform(delete(ENTITY_API_URL_ID, quantification.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return quantificationRepository.count();
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

    protected Quantification getPersistedQuantification(Quantification quantification) {
        return quantificationRepository.findById(quantification.getId()).orElseThrow();
    }

    protected void assertPersistedQuantificationToMatchAllProperties(Quantification expectedQuantification) {
        assertQuantificationAllPropertiesEquals(expectedQuantification, getPersistedQuantification(expectedQuantification));
    }

    protected void assertPersistedQuantificationToMatchUpdatableProperties(Quantification expectedQuantification) {
        assertQuantificationAllUpdatablePropertiesEquals(expectedQuantification, getPersistedQuantification(expectedQuantification));
    }
}
