package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ConfigAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Config;
import com.mycompany.myapp.repository.ConfigRepository;
import com.mycompany.myapp.service.dto.ConfigDTO;
import com.mycompany.myapp.service.mapper.ConfigMapper;
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
 * Integration tests for the {@link ConfigResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConfigResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConfigMockMvc;

    private Config config;

    private Config insertedConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Config createEntity(EntityManager em) {
        Config config = new Config().key(DEFAULT_KEY).value(DEFAULT_VALUE);
        return config;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Config createUpdatedEntity(EntityManager em) {
        Config config = new Config().key(UPDATED_KEY).value(UPDATED_VALUE);
        return config;
    }

    @BeforeEach
    public void initTest() {
        config = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedConfig != null) {
            configRepository.delete(insertedConfig);
            insertedConfig = null;
        }
    }

    @Test
    @Transactional
    void createConfig() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Config
        ConfigDTO configDTO = configMapper.toDto(config);
        var returnedConfigDTO = om.readValue(
            restConfigMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(configDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ConfigDTO.class
        );

        // Validate the Config in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedConfig = configMapper.toEntity(returnedConfigDTO);
        assertConfigUpdatableFieldsEquals(returnedConfig, getPersistedConfig(returnedConfig));

        insertedConfig = returnedConfig;
    }

    @Test
    @Transactional
    void createConfigWithExistingId() throws Exception {
        // Create the Config with an existing ID
        config.setId(1L);
        ConfigDTO configDTO = configMapper.toDto(config);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(configDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Config in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllConfigs() throws Exception {
        // Initialize the database
        insertedConfig = configRepository.saveAndFlush(config);

        // Get all the configList
        restConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(config.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getConfig() throws Exception {
        // Initialize the database
        insertedConfig = configRepository.saveAndFlush(config);

        // Get the config
        restConfigMockMvc
            .perform(get(ENTITY_API_URL_ID, config.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(config.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getConfigsByIdFiltering() throws Exception {
        // Initialize the database
        insertedConfig = configRepository.saveAndFlush(config);

        Long id = config.getId();

        defaultConfigFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultConfigFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultConfigFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllConfigsByKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedConfig = configRepository.saveAndFlush(config);

        // Get all the configList where key equals to
        defaultConfigFiltering("key.equals=" + DEFAULT_KEY, "key.equals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllConfigsByKeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedConfig = configRepository.saveAndFlush(config);

        // Get all the configList where key in
        defaultConfigFiltering("key.in=" + DEFAULT_KEY + "," + UPDATED_KEY, "key.in=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllConfigsByKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedConfig = configRepository.saveAndFlush(config);

        // Get all the configList where key is not null
        defaultConfigFiltering("key.specified=true", "key.specified=false");
    }

    @Test
    @Transactional
    void getAllConfigsByKeyContainsSomething() throws Exception {
        // Initialize the database
        insertedConfig = configRepository.saveAndFlush(config);

        // Get all the configList where key contains
        defaultConfigFiltering("key.contains=" + DEFAULT_KEY, "key.contains=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllConfigsByKeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedConfig = configRepository.saveAndFlush(config);

        // Get all the configList where key does not contain
        defaultConfigFiltering("key.doesNotContain=" + UPDATED_KEY, "key.doesNotContain=" + DEFAULT_KEY);
    }

    @Test
    @Transactional
    void getAllConfigsByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedConfig = configRepository.saveAndFlush(config);

        // Get all the configList where value equals to
        defaultConfigFiltering("value.equals=" + DEFAULT_VALUE, "value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllConfigsByValueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedConfig = configRepository.saveAndFlush(config);

        // Get all the configList where value in
        defaultConfigFiltering("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE, "value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllConfigsByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedConfig = configRepository.saveAndFlush(config);

        // Get all the configList where value is not null
        defaultConfigFiltering("value.specified=true", "value.specified=false");
    }

    @Test
    @Transactional
    void getAllConfigsByValueContainsSomething() throws Exception {
        // Initialize the database
        insertedConfig = configRepository.saveAndFlush(config);

        // Get all the configList where value contains
        defaultConfigFiltering("value.contains=" + DEFAULT_VALUE, "value.contains=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllConfigsByValueNotContainsSomething() throws Exception {
        // Initialize the database
        insertedConfig = configRepository.saveAndFlush(config);

        // Get all the configList where value does not contain
        defaultConfigFiltering("value.doesNotContain=" + UPDATED_VALUE, "value.doesNotContain=" + DEFAULT_VALUE);
    }

    private void defaultConfigFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultConfigShouldBeFound(shouldBeFound);
        defaultConfigShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultConfigShouldBeFound(String filter) throws Exception {
        restConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(config.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));

        // Check, that the count call also returns 1
        restConfigMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultConfigShouldNotBeFound(String filter) throws Exception {
        restConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restConfigMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingConfig() throws Exception {
        // Get the config
        restConfigMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConfig() throws Exception {
        // Initialize the database
        insertedConfig = configRepository.saveAndFlush(config);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the config
        Config updatedConfig = configRepository.findById(config.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedConfig are not directly saved in db
        em.detach(updatedConfig);
        updatedConfig.key(UPDATED_KEY).value(UPDATED_VALUE);
        ConfigDTO configDTO = configMapper.toDto(updatedConfig);

        restConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, configDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(configDTO))
            )
            .andExpect(status().isOk());

        // Validate the Config in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedConfigToMatchAllProperties(updatedConfig);
    }

    @Test
    @Transactional
    void putNonExistingConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        config.setId(longCount.incrementAndGet());

        // Create the Config
        ConfigDTO configDTO = configMapper.toDto(config);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, configDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(configDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Config in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        config.setId(longCount.incrementAndGet());

        // Create the Config
        ConfigDTO configDTO = configMapper.toDto(config);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(configDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Config in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        config.setId(longCount.incrementAndGet());

        // Create the Config
        ConfigDTO configDTO = configMapper.toDto(config);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(configDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Config in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConfigWithPatch() throws Exception {
        // Initialize the database
        insertedConfig = configRepository.saveAndFlush(config);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the config using partial update
        Config partialUpdatedConfig = new Config();
        partialUpdatedConfig.setId(config.getId());

        partialUpdatedConfig.value(UPDATED_VALUE);

        restConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConfig))
            )
            .andExpect(status().isOk());

        // Validate the Config in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConfigUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedConfig, config), getPersistedConfig(config));
    }

    @Test
    @Transactional
    void fullUpdateConfigWithPatch() throws Exception {
        // Initialize the database
        insertedConfig = configRepository.saveAndFlush(config);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the config using partial update
        Config partialUpdatedConfig = new Config();
        partialUpdatedConfig.setId(config.getId());

        partialUpdatedConfig.key(UPDATED_KEY).value(UPDATED_VALUE);

        restConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConfig))
            )
            .andExpect(status().isOk());

        // Validate the Config in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConfigUpdatableFieldsEquals(partialUpdatedConfig, getPersistedConfig(partialUpdatedConfig));
    }

    @Test
    @Transactional
    void patchNonExistingConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        config.setId(longCount.incrementAndGet());

        // Create the Config
        ConfigDTO configDTO = configMapper.toDto(config);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, configDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(configDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Config in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        config.setId(longCount.incrementAndGet());

        // Create the Config
        ConfigDTO configDTO = configMapper.toDto(config);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(configDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Config in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        config.setId(longCount.incrementAndGet());

        // Create the Config
        ConfigDTO configDTO = configMapper.toDto(config);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(configDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Config in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConfig() throws Exception {
        // Initialize the database
        insertedConfig = configRepository.saveAndFlush(config);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the config
        restConfigMockMvc
            .perform(delete(ENTITY_API_URL_ID, config.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return configRepository.count();
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

    protected Config getPersistedConfig(Config config) {
        return configRepository.findById(config.getId()).orElseThrow();
    }

    protected void assertPersistedConfigToMatchAllProperties(Config expectedConfig) {
        assertConfigAllPropertiesEquals(expectedConfig, getPersistedConfig(expectedConfig));
    }

    protected void assertPersistedConfigToMatchUpdatableProperties(Config expectedConfig) {
        assertConfigAllUpdatablePropertiesEquals(expectedConfig, getPersistedConfig(expectedConfig));
    }
}
