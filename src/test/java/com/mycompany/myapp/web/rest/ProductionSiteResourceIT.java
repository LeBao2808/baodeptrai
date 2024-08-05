package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ProductionSiteAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.domain.ProductionSite;
import com.mycompany.myapp.repository.ProductionSiteRepository;
import com.mycompany.myapp.service.dto.ProductionSiteDTO;
import com.mycompany.myapp.service.mapper.ProductionSiteMapper;
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
 * Integration tests for the {@link ProductionSiteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductionSiteResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/production-sites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductionSiteRepository productionSiteRepository;

    @Autowired
    private ProductionSiteMapper productionSiteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductionSiteMockMvc;

    private ProductionSite productionSite;

    private ProductionSite insertedProductionSite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductionSite createEntity(EntityManager em) {
        ProductionSite productionSite = new ProductionSite().name(DEFAULT_NAME).address(DEFAULT_ADDRESS).phone(DEFAULT_PHONE);
        return productionSite;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductionSite createUpdatedEntity(EntityManager em) {
        ProductionSite productionSite = new ProductionSite().name(UPDATED_NAME).address(UPDATED_ADDRESS).phone(UPDATED_PHONE);
        return productionSite;
    }

    @BeforeEach
    public void initTest() {
        productionSite = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductionSite != null) {
            productionSiteRepository.delete(insertedProductionSite);
            insertedProductionSite = null;
        }
    }

    @Test
    @Transactional
    void createProductionSite() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductionSite
        ProductionSiteDTO productionSiteDTO = productionSiteMapper.toDto(productionSite);
        var returnedProductionSiteDTO = om.readValue(
            restProductionSiteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productionSiteDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductionSiteDTO.class
        );

        // Validate the ProductionSite in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProductionSite = productionSiteMapper.toEntity(returnedProductionSiteDTO);
        assertProductionSiteUpdatableFieldsEquals(returnedProductionSite, getPersistedProductionSite(returnedProductionSite));

        insertedProductionSite = returnedProductionSite;
    }

    @Test
    @Transactional
    void createProductionSiteWithExistingId() throws Exception {
        // Create the ProductionSite with an existing ID
        productionSite.setId(1L);
        ProductionSiteDTO productionSiteDTO = productionSiteMapper.toDto(productionSite);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductionSiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productionSiteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductionSite in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductionSites() throws Exception {
        // Initialize the database
        insertedProductionSite = productionSiteRepository.saveAndFlush(productionSite);

        // Get all the productionSiteList
        restProductionSiteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productionSite.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));
    }

    @Test
    @Transactional
    void getProductionSite() throws Exception {
        // Initialize the database
        insertedProductionSite = productionSiteRepository.saveAndFlush(productionSite);

        // Get the productionSite
        restProductionSiteMockMvc
            .perform(get(ENTITY_API_URL_ID, productionSite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productionSite.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE));
    }

    @Test
    @Transactional
    void getProductionSitesByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductionSite = productionSiteRepository.saveAndFlush(productionSite);

        Long id = productionSite.getId();

        defaultProductionSiteFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductionSiteFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductionSiteFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductionSitesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductionSite = productionSiteRepository.saveAndFlush(productionSite);

        // Get all the productionSiteList where name equals to
        defaultProductionSiteFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductionSitesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductionSite = productionSiteRepository.saveAndFlush(productionSite);

        // Get all the productionSiteList where name in
        defaultProductionSiteFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductionSitesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductionSite = productionSiteRepository.saveAndFlush(productionSite);

        // Get all the productionSiteList where name is not null
        defaultProductionSiteFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllProductionSitesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedProductionSite = productionSiteRepository.saveAndFlush(productionSite);

        // Get all the productionSiteList where name contains
        defaultProductionSiteFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductionSitesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductionSite = productionSiteRepository.saveAndFlush(productionSite);

        // Get all the productionSiteList where name does not contain
        defaultProductionSiteFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllProductionSitesByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductionSite = productionSiteRepository.saveAndFlush(productionSite);

        // Get all the productionSiteList where address equals to
        defaultProductionSiteFiltering("address.equals=" + DEFAULT_ADDRESS, "address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllProductionSitesByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductionSite = productionSiteRepository.saveAndFlush(productionSite);

        // Get all the productionSiteList where address in
        defaultProductionSiteFiltering("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS, "address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllProductionSitesByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductionSite = productionSiteRepository.saveAndFlush(productionSite);

        // Get all the productionSiteList where address is not null
        defaultProductionSiteFiltering("address.specified=true", "address.specified=false");
    }

    @Test
    @Transactional
    void getAllProductionSitesByAddressContainsSomething() throws Exception {
        // Initialize the database
        insertedProductionSite = productionSiteRepository.saveAndFlush(productionSite);

        // Get all the productionSiteList where address contains
        defaultProductionSiteFiltering("address.contains=" + DEFAULT_ADDRESS, "address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllProductionSitesByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductionSite = productionSiteRepository.saveAndFlush(productionSite);

        // Get all the productionSiteList where address does not contain
        defaultProductionSiteFiltering("address.doesNotContain=" + UPDATED_ADDRESS, "address.doesNotContain=" + DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void getAllProductionSitesByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductionSite = productionSiteRepository.saveAndFlush(productionSite);

        // Get all the productionSiteList where phone equals to
        defaultProductionSiteFiltering("phone.equals=" + DEFAULT_PHONE, "phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllProductionSitesByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductionSite = productionSiteRepository.saveAndFlush(productionSite);

        // Get all the productionSiteList where phone in
        defaultProductionSiteFiltering("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE, "phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllProductionSitesByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductionSite = productionSiteRepository.saveAndFlush(productionSite);

        // Get all the productionSiteList where phone is not null
        defaultProductionSiteFiltering("phone.specified=true", "phone.specified=false");
    }

    @Test
    @Transactional
    void getAllProductionSitesByPhoneContainsSomething() throws Exception {
        // Initialize the database
        insertedProductionSite = productionSiteRepository.saveAndFlush(productionSite);

        // Get all the productionSiteList where phone contains
        defaultProductionSiteFiltering("phone.contains=" + DEFAULT_PHONE, "phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllProductionSitesByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductionSite = productionSiteRepository.saveAndFlush(productionSite);

        // Get all the productionSiteList where phone does not contain
        defaultProductionSiteFiltering("phone.doesNotContain=" + UPDATED_PHONE, "phone.doesNotContain=" + DEFAULT_PHONE);
    }

    @Test
    @Transactional
    void getAllProductionSitesByProductIdIsEqualToSomething() throws Exception {
        Product productId;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            productionSiteRepository.saveAndFlush(productionSite);
            productId = ProductResourceIT.createEntity(em);
        } else {
            productId = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(productId);
        em.flush();
        productionSite.setProductId(productId);
        productionSiteRepository.saveAndFlush(productionSite);
        Long productIdId = productId.getId();
        // Get all the productionSiteList where productId equals to productIdId
        defaultProductionSiteShouldBeFound("productIdId.equals=" + productIdId);

        // Get all the productionSiteList where productId equals to (productIdId + 1)
        defaultProductionSiteShouldNotBeFound("productIdId.equals=" + (productIdId + 1));
    }

    private void defaultProductionSiteFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductionSiteShouldBeFound(shouldBeFound);
        defaultProductionSiteShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductionSiteShouldBeFound(String filter) throws Exception {
        restProductionSiteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productionSite.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));

        // Check, that the count call also returns 1
        restProductionSiteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductionSiteShouldNotBeFound(String filter) throws Exception {
        restProductionSiteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductionSiteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductionSite() throws Exception {
        // Get the productionSite
        restProductionSiteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductionSite() throws Exception {
        // Initialize the database
        insertedProductionSite = productionSiteRepository.saveAndFlush(productionSite);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productionSite
        ProductionSite updatedProductionSite = productionSiteRepository.findById(productionSite.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductionSite are not directly saved in db
        em.detach(updatedProductionSite);
        updatedProductionSite.name(UPDATED_NAME).address(UPDATED_ADDRESS).phone(UPDATED_PHONE);
        ProductionSiteDTO productionSiteDTO = productionSiteMapper.toDto(updatedProductionSite);

        restProductionSiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productionSiteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productionSiteDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductionSite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductionSiteToMatchAllProperties(updatedProductionSite);
    }

    @Test
    @Transactional
    void putNonExistingProductionSite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productionSite.setId(longCount.incrementAndGet());

        // Create the ProductionSite
        ProductionSiteDTO productionSiteDTO = productionSiteMapper.toDto(productionSite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductionSiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productionSiteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productionSiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductionSite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductionSite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productionSite.setId(longCount.incrementAndGet());

        // Create the ProductionSite
        ProductionSiteDTO productionSiteDTO = productionSiteMapper.toDto(productionSite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductionSiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productionSiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductionSite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductionSite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productionSite.setId(longCount.incrementAndGet());

        // Create the ProductionSite
        ProductionSiteDTO productionSiteDTO = productionSiteMapper.toDto(productionSite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductionSiteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productionSiteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductionSite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductionSiteWithPatch() throws Exception {
        // Initialize the database
        insertedProductionSite = productionSiteRepository.saveAndFlush(productionSite);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productionSite using partial update
        ProductionSite partialUpdatedProductionSite = new ProductionSite();
        partialUpdatedProductionSite.setId(productionSite.getId());

        partialUpdatedProductionSite.address(UPDATED_ADDRESS).phone(UPDATED_PHONE);

        restProductionSiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductionSite.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductionSite))
            )
            .andExpect(status().isOk());

        // Validate the ProductionSite in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductionSiteUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductionSite, productionSite),
            getPersistedProductionSite(productionSite)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductionSiteWithPatch() throws Exception {
        // Initialize the database
        insertedProductionSite = productionSiteRepository.saveAndFlush(productionSite);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productionSite using partial update
        ProductionSite partialUpdatedProductionSite = new ProductionSite();
        partialUpdatedProductionSite.setId(productionSite.getId());

        partialUpdatedProductionSite.name(UPDATED_NAME).address(UPDATED_ADDRESS).phone(UPDATED_PHONE);

        restProductionSiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductionSite.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductionSite))
            )
            .andExpect(status().isOk());

        // Validate the ProductionSite in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductionSiteUpdatableFieldsEquals(partialUpdatedProductionSite, getPersistedProductionSite(partialUpdatedProductionSite));
    }

    @Test
    @Transactional
    void patchNonExistingProductionSite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productionSite.setId(longCount.incrementAndGet());

        // Create the ProductionSite
        ProductionSiteDTO productionSiteDTO = productionSiteMapper.toDto(productionSite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductionSiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productionSiteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productionSiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductionSite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductionSite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productionSite.setId(longCount.incrementAndGet());

        // Create the ProductionSite
        ProductionSiteDTO productionSiteDTO = productionSiteMapper.toDto(productionSite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductionSiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productionSiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductionSite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductionSite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productionSite.setId(longCount.incrementAndGet());

        // Create the ProductionSite
        ProductionSiteDTO productionSiteDTO = productionSiteMapper.toDto(productionSite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductionSiteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productionSiteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductionSite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductionSite() throws Exception {
        // Initialize the database
        insertedProductionSite = productionSiteRepository.saveAndFlush(productionSite);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productionSite
        restProductionSiteMockMvc
            .perform(delete(ENTITY_API_URL_ID, productionSite.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productionSiteRepository.count();
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

    protected ProductionSite getPersistedProductionSite(ProductionSite productionSite) {
        return productionSiteRepository.findById(productionSite.getId()).orElseThrow();
    }

    protected void assertPersistedProductionSiteToMatchAllProperties(ProductionSite expectedProductionSite) {
        assertProductionSiteAllPropertiesEquals(expectedProductionSite, getPersistedProductionSite(expectedProductionSite));
    }

    protected void assertPersistedProductionSiteToMatchUpdatableProperties(ProductionSite expectedProductionSite) {
        assertProductionSiteAllUpdatablePropertiesEquals(expectedProductionSite, getPersistedProductionSite(expectedProductionSite));
    }
}
