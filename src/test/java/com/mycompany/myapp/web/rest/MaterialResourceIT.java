package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.MaterialAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Material;
import com.mycompany.myapp.repository.MaterialRepository;
import com.mycompany.myapp.service.dto.MaterialDTO;
import com.mycompany.myapp.service.mapper.MaterialMapper;
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
 * Integration tests for the {@link MaterialResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MaterialResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_IMG_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMG_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/materials";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMaterialMockMvc;

    private Material material;

    private Material insertedMaterial;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Material createEntity(EntityManager em) {
        Material material = new Material()
            .name(DEFAULT_NAME)
            .unit(DEFAULT_UNIT)
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION)
            .imgUrl(DEFAULT_IMG_URL);
        return material;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Material createUpdatedEntity(EntityManager em) {
        Material material = new Material()
            .name(UPDATED_NAME)
            .unit(UPDATED_UNIT)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .imgUrl(UPDATED_IMG_URL);
        return material;
    }

    @BeforeEach
    public void initTest() {
        material = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedMaterial != null) {
            materialRepository.delete(insertedMaterial);
            insertedMaterial = null;
        }
    }

    @Test
    @Transactional
    void createMaterial() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Material
        MaterialDTO materialDTO = materialMapper.toDto(material);
        var returnedMaterialDTO = om.readValue(
            restMaterialMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materialDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MaterialDTO.class
        );

        // Validate the Material in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMaterial = materialMapper.toEntity(returnedMaterialDTO);
        assertMaterialUpdatableFieldsEquals(returnedMaterial, getPersistedMaterial(returnedMaterial));

        insertedMaterial = returnedMaterial;
    }

    @Test
    @Transactional
    void createMaterialWithExistingId() throws Exception {
        // Create the Material with an existing ID
        material.setId(1L);
        MaterialDTO materialDTO = materialMapper.toDto(material);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaterialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materialDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Material in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMaterials() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        // Get all the materialList
        restMaterialMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(material.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].imgUrl").value(hasItem(DEFAULT_IMG_URL)));
    }

    @Test
    @Transactional
    void getMaterial() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        // Get the material
        restMaterialMockMvc
            .perform(get(ENTITY_API_URL_ID, material.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(material.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.imgUrl").value(DEFAULT_IMG_URL));
    }

    @Test
    @Transactional
    void getMaterialsByIdFiltering() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        Long id = material.getId();

        defaultMaterialFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMaterialFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMaterialFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMaterialsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        // Get all the materialList where name equals to
        defaultMaterialFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMaterialsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        // Get all the materialList where name in
        defaultMaterialFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMaterialsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        // Get all the materialList where name is not null
        defaultMaterialFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllMaterialsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        // Get all the materialList where name contains
        defaultMaterialFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMaterialsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        // Get all the materialList where name does not contain
        defaultMaterialFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllMaterialsByUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        // Get all the materialList where unit equals to
        defaultMaterialFiltering("unit.equals=" + DEFAULT_UNIT, "unit.equals=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllMaterialsByUnitIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        // Get all the materialList where unit in
        defaultMaterialFiltering("unit.in=" + DEFAULT_UNIT + "," + UPDATED_UNIT, "unit.in=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllMaterialsByUnitIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        // Get all the materialList where unit is not null
        defaultMaterialFiltering("unit.specified=true", "unit.specified=false");
    }

    @Test
    @Transactional
    void getAllMaterialsByUnitContainsSomething() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        // Get all the materialList where unit contains
        defaultMaterialFiltering("unit.contains=" + DEFAULT_UNIT, "unit.contains=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllMaterialsByUnitNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        // Get all the materialList where unit does not contain
        defaultMaterialFiltering("unit.doesNotContain=" + UPDATED_UNIT, "unit.doesNotContain=" + DEFAULT_UNIT);
    }

    @Test
    @Transactional
    void getAllMaterialsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        // Get all the materialList where code equals to
        defaultMaterialFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMaterialsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        // Get all the materialList where code in
        defaultMaterialFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMaterialsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        // Get all the materialList where code is not null
        defaultMaterialFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllMaterialsByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        // Get all the materialList where code contains
        defaultMaterialFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMaterialsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        // Get all the materialList where code does not contain
        defaultMaterialFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllMaterialsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        // Get all the materialList where description equals to
        defaultMaterialFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMaterialsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        // Get all the materialList where description in
        defaultMaterialFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllMaterialsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        // Get all the materialList where description is not null
        defaultMaterialFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllMaterialsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        // Get all the materialList where description contains
        defaultMaterialFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMaterialsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        // Get all the materialList where description does not contain
        defaultMaterialFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMaterialsByImgUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        // Get all the materialList where imgUrl equals to
        defaultMaterialFiltering("imgUrl.equals=" + DEFAULT_IMG_URL, "imgUrl.equals=" + UPDATED_IMG_URL);
    }

    @Test
    @Transactional
    void getAllMaterialsByImgUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        // Get all the materialList where imgUrl in
        defaultMaterialFiltering("imgUrl.in=" + DEFAULT_IMG_URL + "," + UPDATED_IMG_URL, "imgUrl.in=" + UPDATED_IMG_URL);
    }

    @Test
    @Transactional
    void getAllMaterialsByImgUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        // Get all the materialList where imgUrl is not null
        defaultMaterialFiltering("imgUrl.specified=true", "imgUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllMaterialsByImgUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        // Get all the materialList where imgUrl contains
        defaultMaterialFiltering("imgUrl.contains=" + DEFAULT_IMG_URL, "imgUrl.contains=" + UPDATED_IMG_URL);
    }

    @Test
    @Transactional
    void getAllMaterialsByImgUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        // Get all the materialList where imgUrl does not contain
        defaultMaterialFiltering("imgUrl.doesNotContain=" + UPDATED_IMG_URL, "imgUrl.doesNotContain=" + DEFAULT_IMG_URL);
    }

    private void defaultMaterialFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMaterialShouldBeFound(shouldBeFound);
        defaultMaterialShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMaterialShouldBeFound(String filter) throws Exception {
        restMaterialMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(material.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].imgUrl").value(hasItem(DEFAULT_IMG_URL)));

        // Check, that the count call also returns 1
        restMaterialMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMaterialShouldNotBeFound(String filter) throws Exception {
        restMaterialMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMaterialMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMaterial() throws Exception {
        // Get the material
        restMaterialMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMaterial() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the material
        Material updatedMaterial = materialRepository.findById(material.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMaterial are not directly saved in db
        em.detach(updatedMaterial);
        updatedMaterial.name(UPDATED_NAME).unit(UPDATED_UNIT).code(UPDATED_CODE).description(UPDATED_DESCRIPTION).imgUrl(UPDATED_IMG_URL);
        MaterialDTO materialDTO = materialMapper.toDto(updatedMaterial);

        restMaterialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, materialDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materialDTO))
            )
            .andExpect(status().isOk());

        // Validate the Material in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMaterialToMatchAllProperties(updatedMaterial);
    }

    @Test
    @Transactional
    void putNonExistingMaterial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        material.setId(longCount.incrementAndGet());

        // Create the Material
        MaterialDTO materialDTO = materialMapper.toDto(material);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, materialDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Material in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMaterial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        material.setId(longCount.incrementAndGet());

        // Create the Material
        MaterialDTO materialDTO = materialMapper.toDto(material);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Material in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMaterial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        material.setId(longCount.incrementAndGet());

        // Create the Material
        MaterialDTO materialDTO = materialMapper.toDto(material);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materialDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Material in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMaterialWithPatch() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the material using partial update
        Material partialUpdatedMaterial = new Material();
        partialUpdatedMaterial.setId(material.getId());

        partialUpdatedMaterial.name(UPDATED_NAME).code(UPDATED_CODE);

        restMaterialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaterial.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMaterial))
            )
            .andExpect(status().isOk());

        // Validate the Material in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMaterialUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMaterial, material), getPersistedMaterial(material));
    }

    @Test
    @Transactional
    void fullUpdateMaterialWithPatch() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the material using partial update
        Material partialUpdatedMaterial = new Material();
        partialUpdatedMaterial.setId(material.getId());

        partialUpdatedMaterial
            .name(UPDATED_NAME)
            .unit(UPDATED_UNIT)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .imgUrl(UPDATED_IMG_URL);

        restMaterialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaterial.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMaterial))
            )
            .andExpect(status().isOk());

        // Validate the Material in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMaterialUpdatableFieldsEquals(partialUpdatedMaterial, getPersistedMaterial(partialUpdatedMaterial));
    }

    @Test
    @Transactional
    void patchNonExistingMaterial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        material.setId(longCount.incrementAndGet());

        // Create the Material
        MaterialDTO materialDTO = materialMapper.toDto(material);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, materialDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(materialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Material in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMaterial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        material.setId(longCount.incrementAndGet());

        // Create the Material
        MaterialDTO materialDTO = materialMapper.toDto(material);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(materialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Material in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMaterial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        material.setId(longCount.incrementAndGet());

        // Create the Material
        MaterialDTO materialDTO = materialMapper.toDto(material);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(materialDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Material in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMaterial() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the material
        restMaterialMockMvc
            .perform(delete(ENTITY_API_URL_ID, material.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return materialRepository.count();
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

    protected Material getPersistedMaterial(Material material) {
        return materialRepository.findById(material.getId()).orElseThrow();
    }

    protected void assertPersistedMaterialToMatchAllProperties(Material expectedMaterial) {
        assertMaterialAllPropertiesEquals(expectedMaterial, getPersistedMaterial(expectedMaterial));
    }

    protected void assertPersistedMaterialToMatchUpdatableProperties(Material expectedMaterial) {
        assertMaterialAllUpdatablePropertiesEquals(expectedMaterial, getPersistedMaterial(expectedMaterial));
    }
}
