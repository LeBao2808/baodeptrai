package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.MaterialInventoryAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Material;
import com.mycompany.myapp.domain.MaterialInventory;
import com.mycompany.myapp.repository.MaterialInventoryRepository;
import com.mycompany.myapp.service.dto.MaterialInventoryDTO;
import com.mycompany.myapp.service.mapper.MaterialInventoryMapper;
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
 * Integration tests for the {@link MaterialInventoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MaterialInventoryResourceIT {

    private static final Float DEFAULT_QUANTITY_ON_HAND = 1F;
    private static final Float UPDATED_QUANTITY_ON_HAND = 2F;
    private static final Float SMALLER_QUANTITY_ON_HAND = 1F - 1F;

    private static final Integer DEFAULT_INVENTORY_MONTH = 1;
    private static final Integer UPDATED_INVENTORY_MONTH = 2;
    private static final Integer SMALLER_INVENTORY_MONTH = 1 - 1;

    private static final Integer DEFAULT_INVENTORY_YEAR = 1;
    private static final Integer UPDATED_INVENTORY_YEAR = 2;
    private static final Integer SMALLER_INVENTORY_YEAR = 1 - 1;

    private static final Integer DEFAULT_TYPE = 1;
    private static final Integer UPDATED_TYPE = 2;
    private static final Integer SMALLER_TYPE = 1 - 1;

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_PRICE = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/material-inventories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MaterialInventoryRepository materialInventoryRepository;

    @Autowired
    private MaterialInventoryMapper materialInventoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMaterialInventoryMockMvc;

    private MaterialInventory materialInventory;

    private MaterialInventory insertedMaterialInventory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaterialInventory createEntity(EntityManager em) {
        MaterialInventory materialInventory = new MaterialInventory()
            .quantityOnHand(DEFAULT_QUANTITY_ON_HAND)
            .inventoryMonth(DEFAULT_INVENTORY_MONTH)
            .inventoryYear(DEFAULT_INVENTORY_YEAR)
            .type(DEFAULT_TYPE)
            .price(DEFAULT_PRICE);
        return materialInventory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaterialInventory createUpdatedEntity(EntityManager em) {
        MaterialInventory materialInventory = new MaterialInventory()
            .quantityOnHand(UPDATED_QUANTITY_ON_HAND)
            .inventoryMonth(UPDATED_INVENTORY_MONTH)
            .inventoryYear(UPDATED_INVENTORY_YEAR)
            .type(UPDATED_TYPE)
            .price(UPDATED_PRICE);
        return materialInventory;
    }

    @BeforeEach
    public void initTest() {
        materialInventory = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedMaterialInventory != null) {
            materialInventoryRepository.delete(insertedMaterialInventory);
            insertedMaterialInventory = null;
        }
    }

    @Test
    @Transactional
    void createMaterialInventory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MaterialInventory
        MaterialInventoryDTO materialInventoryDTO = materialInventoryMapper.toDto(materialInventory);
        var returnedMaterialInventoryDTO = om.readValue(
            restMaterialInventoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materialInventoryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MaterialInventoryDTO.class
        );

        // Validate the MaterialInventory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMaterialInventory = materialInventoryMapper.toEntity(returnedMaterialInventoryDTO);
        assertMaterialInventoryUpdatableFieldsEquals(returnedMaterialInventory, getPersistedMaterialInventory(returnedMaterialInventory));

        insertedMaterialInventory = returnedMaterialInventory;
    }

    @Test
    @Transactional
    void createMaterialInventoryWithExistingId() throws Exception {
        // Create the MaterialInventory with an existing ID
        materialInventory.setId(1L);
        MaterialInventoryDTO materialInventoryDTO = materialInventoryMapper.toDto(materialInventory);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaterialInventoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materialInventoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MaterialInventory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMaterialInventories() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        // Get all the materialInventoryList
        restMaterialInventoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(materialInventory.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantityOnHand").value(hasItem(DEFAULT_QUANTITY_ON_HAND.doubleValue())))
            .andExpect(jsonPath("$.[*].inventoryMonth").value(hasItem(DEFAULT_INVENTORY_MONTH)))
            .andExpect(jsonPath("$.[*].inventoryYear").value(hasItem(DEFAULT_INVENTORY_YEAR)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))));
    }

    @Test
    @Transactional
    void getMaterialInventory() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        // Get the materialInventory
        restMaterialInventoryMockMvc
            .perform(get(ENTITY_API_URL_ID, materialInventory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(materialInventory.getId().intValue()))
            .andExpect(jsonPath("$.quantityOnHand").value(DEFAULT_QUANTITY_ON_HAND.doubleValue()))
            .andExpect(jsonPath("$.inventoryMonth").value(DEFAULT_INVENTORY_MONTH))
            .andExpect(jsonPath("$.inventoryYear").value(DEFAULT_INVENTORY_YEAR))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)));
    }

    @Test
    @Transactional
    void getMaterialInventoriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        Long id = materialInventory.getId();

        defaultMaterialInventoryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMaterialInventoryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMaterialInventoryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMaterialInventoriesByQuantityOnHandIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        // Get all the materialInventoryList where quantityOnHand equals to
        defaultMaterialInventoryFiltering(
            "quantityOnHand.equals=" + DEFAULT_QUANTITY_ON_HAND,
            "quantityOnHand.equals=" + UPDATED_QUANTITY_ON_HAND
        );
    }

    @Test
    @Transactional
    void getAllMaterialInventoriesByQuantityOnHandIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        // Get all the materialInventoryList where quantityOnHand in
        defaultMaterialInventoryFiltering(
            "quantityOnHand.in=" + DEFAULT_QUANTITY_ON_HAND + "," + UPDATED_QUANTITY_ON_HAND,
            "quantityOnHand.in=" + UPDATED_QUANTITY_ON_HAND
        );
    }

    @Test
    @Transactional
    void getAllMaterialInventoriesByQuantityOnHandIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        // Get all the materialInventoryList where quantityOnHand is not null
        defaultMaterialInventoryFiltering("quantityOnHand.specified=true", "quantityOnHand.specified=false");
    }

    @Test
    @Transactional
    void getAllMaterialInventoriesByQuantityOnHandIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        // Get all the materialInventoryList where quantityOnHand is greater than or equal to
        defaultMaterialInventoryFiltering(
            "quantityOnHand.greaterThanOrEqual=" + DEFAULT_QUANTITY_ON_HAND,
            "quantityOnHand.greaterThanOrEqual=" + UPDATED_QUANTITY_ON_HAND
        );
    }

    @Test
    @Transactional
    void getAllMaterialInventoriesByQuantityOnHandIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        // Get all the materialInventoryList where quantityOnHand is less than or equal to
        defaultMaterialInventoryFiltering(
            "quantityOnHand.lessThanOrEqual=" + DEFAULT_QUANTITY_ON_HAND,
            "quantityOnHand.lessThanOrEqual=" + SMALLER_QUANTITY_ON_HAND
        );
    }

    @Test
    @Transactional
    void getAllMaterialInventoriesByQuantityOnHandIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        // Get all the materialInventoryList where quantityOnHand is less than
        defaultMaterialInventoryFiltering(
            "quantityOnHand.lessThan=" + UPDATED_QUANTITY_ON_HAND,
            "quantityOnHand.lessThan=" + DEFAULT_QUANTITY_ON_HAND
        );
    }

    @Test
    @Transactional
    void getAllMaterialInventoriesByQuantityOnHandIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        // Get all the materialInventoryList where quantityOnHand is greater than
        defaultMaterialInventoryFiltering(
            "quantityOnHand.greaterThan=" + SMALLER_QUANTITY_ON_HAND,
            "quantityOnHand.greaterThan=" + DEFAULT_QUANTITY_ON_HAND
        );
    }

    @Test
    @Transactional
    void getAllMaterialInventoriesByInventoryMonthIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        // Get all the materialInventoryList where inventoryMonth equals to
        defaultMaterialInventoryFiltering(
            "inventoryMonth.equals=" + DEFAULT_INVENTORY_MONTH,
            "inventoryMonth.equals=" + UPDATED_INVENTORY_MONTH
        );
    }

    @Test
    @Transactional
    void getAllMaterialInventoriesByInventoryMonthIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        // Get all the materialInventoryList where inventoryMonth in
        defaultMaterialInventoryFiltering(
            "inventoryMonth.in=" + DEFAULT_INVENTORY_MONTH + "," + UPDATED_INVENTORY_MONTH,
            "inventoryMonth.in=" + UPDATED_INVENTORY_MONTH
        );
    }

    @Test
    @Transactional
    void getAllMaterialInventoriesByInventoryMonthIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        // Get all the materialInventoryList where inventoryMonth is not null
        defaultMaterialInventoryFiltering("inventoryMonth.specified=true", "inventoryMonth.specified=false");
    }

    @Test
    @Transactional
    void getAllMaterialInventoriesByInventoryMonthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        // Get all the materialInventoryList where inventoryMonth is greater than or equal to
        defaultMaterialInventoryFiltering(
            "inventoryMonth.greaterThanOrEqual=" + DEFAULT_INVENTORY_MONTH,
            "inventoryMonth.greaterThanOrEqual=" + UPDATED_INVENTORY_MONTH
        );
    }

    @Test
    @Transactional
    void getAllMaterialInventoriesByInventoryMonthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        // Get all the materialInventoryList where inventoryMonth is less than or equal to
        defaultMaterialInventoryFiltering(
            "inventoryMonth.lessThanOrEqual=" + DEFAULT_INVENTORY_MONTH,
            "inventoryMonth.lessThanOrEqual=" + SMALLER_INVENTORY_MONTH
        );
    }

    @Test
    @Transactional
    void getAllMaterialInventoriesByInventoryMonthIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        // Get all the materialInventoryList where inventoryMonth is less than
        defaultMaterialInventoryFiltering(
            "inventoryMonth.lessThan=" + UPDATED_INVENTORY_MONTH,
            "inventoryMonth.lessThan=" + DEFAULT_INVENTORY_MONTH
        );
    }

    @Test
    @Transactional
    void getAllMaterialInventoriesByInventoryMonthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        // Get all the materialInventoryList where inventoryMonth is greater than
        defaultMaterialInventoryFiltering(
            "inventoryMonth.greaterThan=" + SMALLER_INVENTORY_MONTH,
            "inventoryMonth.greaterThan=" + DEFAULT_INVENTORY_MONTH
        );
    }

    @Test
    @Transactional
    void getAllMaterialInventoriesByInventoryYearIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        // Get all the materialInventoryList where inventoryYear equals to
        defaultMaterialInventoryFiltering(
            "inventoryYear.equals=" + DEFAULT_INVENTORY_YEAR,
            "inventoryYear.equals=" + UPDATED_INVENTORY_YEAR
        );
    }

    @Test
    @Transactional
    void getAllMaterialInventoriesByInventoryYearIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        // Get all the materialInventoryList where inventoryYear in
        defaultMaterialInventoryFiltering(
            "inventoryYear.in=" + DEFAULT_INVENTORY_YEAR + "," + UPDATED_INVENTORY_YEAR,
            "inventoryYear.in=" + UPDATED_INVENTORY_YEAR
        );
    }

    @Test
    @Transactional
    void getAllMaterialInventoriesByInventoryYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        // Get all the materialInventoryList where inventoryYear is not null
        defaultMaterialInventoryFiltering("inventoryYear.specified=true", "inventoryYear.specified=false");
    }

    @Test
    @Transactional
    void getAllMaterialInventoriesByInventoryYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        // Get all the materialInventoryList where inventoryYear is greater than or equal to
        defaultMaterialInventoryFiltering(
            "inventoryYear.greaterThanOrEqual=" + DEFAULT_INVENTORY_YEAR,
            "inventoryYear.greaterThanOrEqual=" + UPDATED_INVENTORY_YEAR
        );
    }

    @Test
    @Transactional
    void getAllMaterialInventoriesByInventoryYearIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        // Get all the materialInventoryList where inventoryYear is less than or equal to
        defaultMaterialInventoryFiltering(
            "inventoryYear.lessThanOrEqual=" + DEFAULT_INVENTORY_YEAR,
            "inventoryYear.lessThanOrEqual=" + SMALLER_INVENTORY_YEAR
        );
    }

    @Test
    @Transactional
    void getAllMaterialInventoriesByInventoryYearIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        // Get all the materialInventoryList where inventoryYear is less than
        defaultMaterialInventoryFiltering(
            "inventoryYear.lessThan=" + UPDATED_INVENTORY_YEAR,
            "inventoryYear.lessThan=" + DEFAULT_INVENTORY_YEAR
        );
    }

    @Test
    @Transactional
    void getAllMaterialInventoriesByInventoryYearIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        // Get all the materialInventoryList where inventoryYear is greater than
        defaultMaterialInventoryFiltering(
            "inventoryYear.greaterThan=" + SMALLER_INVENTORY_YEAR,
            "inventoryYear.greaterThan=" + DEFAULT_INVENTORY_YEAR
        );
    }

    @Test
    @Transactional
    void getAllMaterialInventoriesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        // Get all the materialInventoryList where type equals to
        defaultMaterialInventoryFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllMaterialInventoriesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        // Get all the materialInventoryList where type in
        defaultMaterialInventoryFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllMaterialInventoriesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        // Get all the materialInventoryList where type is not null
        defaultMaterialInventoryFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllMaterialInventoriesByTypeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        // Get all the materialInventoryList where type is greater than or equal to
        defaultMaterialInventoryFiltering("type.greaterThanOrEqual=" + DEFAULT_TYPE, "type.greaterThanOrEqual=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllMaterialInventoriesByTypeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        // Get all the materialInventoryList where type is less than or equal to
        defaultMaterialInventoryFiltering("type.lessThanOrEqual=" + DEFAULT_TYPE, "type.lessThanOrEqual=" + SMALLER_TYPE);
    }

    @Test
    @Transactional
    void getAllMaterialInventoriesByTypeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        // Get all the materialInventoryList where type is less than
        defaultMaterialInventoryFiltering("type.lessThan=" + UPDATED_TYPE, "type.lessThan=" + DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void getAllMaterialInventoriesByTypeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        // Get all the materialInventoryList where type is greater than
        defaultMaterialInventoryFiltering("type.greaterThan=" + SMALLER_TYPE, "type.greaterThan=" + DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void getAllMaterialInventoriesByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        // Get all the materialInventoryList where price equals to
        defaultMaterialInventoryFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllMaterialInventoriesByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        // Get all the materialInventoryList where price in
        defaultMaterialInventoryFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllMaterialInventoriesByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        // Get all the materialInventoryList where price is not null
        defaultMaterialInventoryFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllMaterialInventoriesByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        // Get all the materialInventoryList where price is greater than or equal to
        defaultMaterialInventoryFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllMaterialInventoriesByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        // Get all the materialInventoryList where price is less than or equal to
        defaultMaterialInventoryFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllMaterialInventoriesByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        // Get all the materialInventoryList where price is less than
        defaultMaterialInventoryFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllMaterialInventoriesByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        // Get all the materialInventoryList where price is greater than
        defaultMaterialInventoryFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllMaterialInventoriesByMaterialIsEqualToSomething() throws Exception {
        Material material;
        if (TestUtil.findAll(em, Material.class).isEmpty()) {
            materialInventoryRepository.saveAndFlush(materialInventory);
            material = MaterialResourceIT.createEntity(em);
        } else {
            material = TestUtil.findAll(em, Material.class).get(0);
        }
        em.persist(material);
        em.flush();
        materialInventory.setMaterial(material);
        materialInventoryRepository.saveAndFlush(materialInventory);
        Long materialId = material.getId();
        // Get all the materialInventoryList where material equals to materialId
        defaultMaterialInventoryShouldBeFound("materialId.equals=" + materialId);

        // Get all the materialInventoryList where material equals to (materialId + 1)
        defaultMaterialInventoryShouldNotBeFound("materialId.equals=" + (materialId + 1));
    }

    private void defaultMaterialInventoryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMaterialInventoryShouldBeFound(shouldBeFound);
        defaultMaterialInventoryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMaterialInventoryShouldBeFound(String filter) throws Exception {
        restMaterialInventoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(materialInventory.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantityOnHand").value(hasItem(DEFAULT_QUANTITY_ON_HAND.doubleValue())))
            .andExpect(jsonPath("$.[*].inventoryMonth").value(hasItem(DEFAULT_INVENTORY_MONTH)))
            .andExpect(jsonPath("$.[*].inventoryYear").value(hasItem(DEFAULT_INVENTORY_YEAR)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))));

        // Check, that the count call also returns 1
        restMaterialInventoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMaterialInventoryShouldNotBeFound(String filter) throws Exception {
        restMaterialInventoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMaterialInventoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMaterialInventory() throws Exception {
        // Get the materialInventory
        restMaterialInventoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMaterialInventory() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the materialInventory
        MaterialInventory updatedMaterialInventory = materialInventoryRepository.findById(materialInventory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMaterialInventory are not directly saved in db
        em.detach(updatedMaterialInventory);
        updatedMaterialInventory
            .quantityOnHand(UPDATED_QUANTITY_ON_HAND)
            .inventoryMonth(UPDATED_INVENTORY_MONTH)
            .inventoryYear(UPDATED_INVENTORY_YEAR)
            .type(UPDATED_TYPE)
            .price(UPDATED_PRICE);
        MaterialInventoryDTO materialInventoryDTO = materialInventoryMapper.toDto(updatedMaterialInventory);

        restMaterialInventoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, materialInventoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materialInventoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the MaterialInventory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMaterialInventoryToMatchAllProperties(updatedMaterialInventory);
    }

    @Test
    @Transactional
    void putNonExistingMaterialInventory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materialInventory.setId(longCount.incrementAndGet());

        // Create the MaterialInventory
        MaterialInventoryDTO materialInventoryDTO = materialInventoryMapper.toDto(materialInventory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterialInventoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, materialInventoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materialInventoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaterialInventory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMaterialInventory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materialInventory.setId(longCount.incrementAndGet());

        // Create the MaterialInventory
        MaterialInventoryDTO materialInventoryDTO = materialInventoryMapper.toDto(materialInventory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialInventoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materialInventoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaterialInventory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMaterialInventory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materialInventory.setId(longCount.incrementAndGet());

        // Create the MaterialInventory
        MaterialInventoryDTO materialInventoryDTO = materialInventoryMapper.toDto(materialInventory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialInventoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materialInventoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MaterialInventory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMaterialInventoryWithPatch() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the materialInventory using partial update
        MaterialInventory partialUpdatedMaterialInventory = new MaterialInventory();
        partialUpdatedMaterialInventory.setId(materialInventory.getId());

        partialUpdatedMaterialInventory.quantityOnHand(UPDATED_QUANTITY_ON_HAND).inventoryYear(UPDATED_INVENTORY_YEAR);

        restMaterialInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaterialInventory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMaterialInventory))
            )
            .andExpect(status().isOk());

        // Validate the MaterialInventory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMaterialInventoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMaterialInventory, materialInventory),
            getPersistedMaterialInventory(materialInventory)
        );
    }

    @Test
    @Transactional
    void fullUpdateMaterialInventoryWithPatch() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the materialInventory using partial update
        MaterialInventory partialUpdatedMaterialInventory = new MaterialInventory();
        partialUpdatedMaterialInventory.setId(materialInventory.getId());

        partialUpdatedMaterialInventory
            .quantityOnHand(UPDATED_QUANTITY_ON_HAND)
            .inventoryMonth(UPDATED_INVENTORY_MONTH)
            .inventoryYear(UPDATED_INVENTORY_YEAR)
            .type(UPDATED_TYPE)
            .price(UPDATED_PRICE);

        restMaterialInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaterialInventory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMaterialInventory))
            )
            .andExpect(status().isOk());

        // Validate the MaterialInventory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMaterialInventoryUpdatableFieldsEquals(
            partialUpdatedMaterialInventory,
            getPersistedMaterialInventory(partialUpdatedMaterialInventory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingMaterialInventory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materialInventory.setId(longCount.incrementAndGet());

        // Create the MaterialInventory
        MaterialInventoryDTO materialInventoryDTO = materialInventoryMapper.toDto(materialInventory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterialInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, materialInventoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(materialInventoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaterialInventory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMaterialInventory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materialInventory.setId(longCount.incrementAndGet());

        // Create the MaterialInventory
        MaterialInventoryDTO materialInventoryDTO = materialInventoryMapper.toDto(materialInventory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(materialInventoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaterialInventory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMaterialInventory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materialInventory.setId(longCount.incrementAndGet());

        // Create the MaterialInventory
        MaterialInventoryDTO materialInventoryDTO = materialInventoryMapper.toDto(materialInventory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialInventoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(materialInventoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MaterialInventory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMaterialInventory() throws Exception {
        // Initialize the database
        insertedMaterialInventory = materialInventoryRepository.saveAndFlush(materialInventory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the materialInventory
        restMaterialInventoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, materialInventory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return materialInventoryRepository.count();
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

    protected MaterialInventory getPersistedMaterialInventory(MaterialInventory materialInventory) {
        return materialInventoryRepository.findById(materialInventory.getId()).orElseThrow();
    }

    protected void assertPersistedMaterialInventoryToMatchAllProperties(MaterialInventory expectedMaterialInventory) {
        assertMaterialInventoryAllPropertiesEquals(expectedMaterialInventory, getPersistedMaterialInventory(expectedMaterialInventory));
    }

    protected void assertPersistedMaterialInventoryToMatchUpdatableProperties(MaterialInventory expectedMaterialInventory) {
        assertMaterialInventoryAllUpdatablePropertiesEquals(
            expectedMaterialInventory,
            getPersistedMaterialInventory(expectedMaterialInventory)
        );
    }
}
