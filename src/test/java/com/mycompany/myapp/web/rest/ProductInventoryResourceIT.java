package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ProductInventoryAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.domain.ProductInventory;
import com.mycompany.myapp.repository.ProductInventoryRepository;
import com.mycompany.myapp.service.dto.ProductInventoryDTO;
import com.mycompany.myapp.service.mapper.ProductInventoryMapper;
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
 * Integration tests for the {@link ProductInventoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductInventoryResourceIT {

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

    private static final String ENTITY_API_URL = "/api/product-inventories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductInventoryRepository productInventoryRepository;

    @Autowired
    private ProductInventoryMapper productInventoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductInventoryMockMvc;

    private ProductInventory productInventory;

    private ProductInventory insertedProductInventory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductInventory createEntity(EntityManager em) {
        ProductInventory productInventory = new ProductInventory()
            .quantityOnHand(DEFAULT_QUANTITY_ON_HAND)
            .inventoryMonth(DEFAULT_INVENTORY_MONTH)
            .inventoryYear(DEFAULT_INVENTORY_YEAR)
            .type(DEFAULT_TYPE)
            .price(DEFAULT_PRICE);
        return productInventory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductInventory createUpdatedEntity(EntityManager em) {
        ProductInventory productInventory = new ProductInventory()
            .quantityOnHand(UPDATED_QUANTITY_ON_HAND)
            .inventoryMonth(UPDATED_INVENTORY_MONTH)
            .inventoryYear(UPDATED_INVENTORY_YEAR)
            .type(UPDATED_TYPE)
            .price(UPDATED_PRICE);
        return productInventory;
    }

    @BeforeEach
    public void initTest() {
        productInventory = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductInventory != null) {
            productInventoryRepository.delete(insertedProductInventory);
            insertedProductInventory = null;
        }
    }

    @Test
    @Transactional
    void createProductInventory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductInventory
        ProductInventoryDTO productInventoryDTO = productInventoryMapper.toDto(productInventory);
        var returnedProductInventoryDTO = om.readValue(
            restProductInventoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productInventoryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductInventoryDTO.class
        );

        // Validate the ProductInventory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProductInventory = productInventoryMapper.toEntity(returnedProductInventoryDTO);
        assertProductInventoryUpdatableFieldsEquals(returnedProductInventory, getPersistedProductInventory(returnedProductInventory));

        insertedProductInventory = returnedProductInventory;
    }

    @Test
    @Transactional
    void createProductInventoryWithExistingId() throws Exception {
        // Create the ProductInventory with an existing ID
        productInventory.setId(1L);
        ProductInventoryDTO productInventoryDTO = productInventoryMapper.toDto(productInventory);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductInventoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productInventoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductInventory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductInventories() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList
        restProductInventoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productInventory.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantityOnHand").value(hasItem(DEFAULT_QUANTITY_ON_HAND.doubleValue())))
            .andExpect(jsonPath("$.[*].inventoryMonth").value(hasItem(DEFAULT_INVENTORY_MONTH)))
            .andExpect(jsonPath("$.[*].inventoryYear").value(hasItem(DEFAULT_INVENTORY_YEAR)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))));
    }

    @Test
    @Transactional
    void getProductInventory() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        // Get the productInventory
        restProductInventoryMockMvc
            .perform(get(ENTITY_API_URL_ID, productInventory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productInventory.getId().intValue()))
            .andExpect(jsonPath("$.quantityOnHand").value(DEFAULT_QUANTITY_ON_HAND.doubleValue()))
            .andExpect(jsonPath("$.inventoryMonth").value(DEFAULT_INVENTORY_MONTH))
            .andExpect(jsonPath("$.inventoryYear").value(DEFAULT_INVENTORY_YEAR))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)));
    }

    @Test
    @Transactional
    void getProductInventoriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        Long id = productInventory.getId();

        defaultProductInventoryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductInventoryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductInventoryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductInventoriesByQuantityOnHandIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where quantityOnHand equals to
        defaultProductInventoryFiltering(
            "quantityOnHand.equals=" + DEFAULT_QUANTITY_ON_HAND,
            "quantityOnHand.equals=" + UPDATED_QUANTITY_ON_HAND
        );
    }

    @Test
    @Transactional
    void getAllProductInventoriesByQuantityOnHandIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where quantityOnHand in
        defaultProductInventoryFiltering(
            "quantityOnHand.in=" + DEFAULT_QUANTITY_ON_HAND + "," + UPDATED_QUANTITY_ON_HAND,
            "quantityOnHand.in=" + UPDATED_QUANTITY_ON_HAND
        );
    }

    @Test
    @Transactional
    void getAllProductInventoriesByQuantityOnHandIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where quantityOnHand is not null
        defaultProductInventoryFiltering("quantityOnHand.specified=true", "quantityOnHand.specified=false");
    }

    @Test
    @Transactional
    void getAllProductInventoriesByQuantityOnHandIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where quantityOnHand is greater than or equal to
        defaultProductInventoryFiltering(
            "quantityOnHand.greaterThanOrEqual=" + DEFAULT_QUANTITY_ON_HAND,
            "quantityOnHand.greaterThanOrEqual=" + UPDATED_QUANTITY_ON_HAND
        );
    }

    @Test
    @Transactional
    void getAllProductInventoriesByQuantityOnHandIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where quantityOnHand is less than or equal to
        defaultProductInventoryFiltering(
            "quantityOnHand.lessThanOrEqual=" + DEFAULT_QUANTITY_ON_HAND,
            "quantityOnHand.lessThanOrEqual=" + SMALLER_QUANTITY_ON_HAND
        );
    }

    @Test
    @Transactional
    void getAllProductInventoriesByQuantityOnHandIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where quantityOnHand is less than
        defaultProductInventoryFiltering(
            "quantityOnHand.lessThan=" + UPDATED_QUANTITY_ON_HAND,
            "quantityOnHand.lessThan=" + DEFAULT_QUANTITY_ON_HAND
        );
    }

    @Test
    @Transactional
    void getAllProductInventoriesByQuantityOnHandIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where quantityOnHand is greater than
        defaultProductInventoryFiltering(
            "quantityOnHand.greaterThan=" + SMALLER_QUANTITY_ON_HAND,
            "quantityOnHand.greaterThan=" + DEFAULT_QUANTITY_ON_HAND
        );
    }

    @Test
    @Transactional
    void getAllProductInventoriesByInventoryMonthIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where inventoryMonth equals to
        defaultProductInventoryFiltering(
            "inventoryMonth.equals=" + DEFAULT_INVENTORY_MONTH,
            "inventoryMonth.equals=" + UPDATED_INVENTORY_MONTH
        );
    }

    @Test
    @Transactional
    void getAllProductInventoriesByInventoryMonthIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where inventoryMonth in
        defaultProductInventoryFiltering(
            "inventoryMonth.in=" + DEFAULT_INVENTORY_MONTH + "," + UPDATED_INVENTORY_MONTH,
            "inventoryMonth.in=" + UPDATED_INVENTORY_MONTH
        );
    }

    @Test
    @Transactional
    void getAllProductInventoriesByInventoryMonthIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where inventoryMonth is not null
        defaultProductInventoryFiltering("inventoryMonth.specified=true", "inventoryMonth.specified=false");
    }

    @Test
    @Transactional
    void getAllProductInventoriesByInventoryMonthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where inventoryMonth is greater than or equal to
        defaultProductInventoryFiltering(
            "inventoryMonth.greaterThanOrEqual=" + DEFAULT_INVENTORY_MONTH,
            "inventoryMonth.greaterThanOrEqual=" + UPDATED_INVENTORY_MONTH
        );
    }

    @Test
    @Transactional
    void getAllProductInventoriesByInventoryMonthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where inventoryMonth is less than or equal to
        defaultProductInventoryFiltering(
            "inventoryMonth.lessThanOrEqual=" + DEFAULT_INVENTORY_MONTH,
            "inventoryMonth.lessThanOrEqual=" + SMALLER_INVENTORY_MONTH
        );
    }

    @Test
    @Transactional
    void getAllProductInventoriesByInventoryMonthIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where inventoryMonth is less than
        defaultProductInventoryFiltering(
            "inventoryMonth.lessThan=" + UPDATED_INVENTORY_MONTH,
            "inventoryMonth.lessThan=" + DEFAULT_INVENTORY_MONTH
        );
    }

    @Test
    @Transactional
    void getAllProductInventoriesByInventoryMonthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where inventoryMonth is greater than
        defaultProductInventoryFiltering(
            "inventoryMonth.greaterThan=" + SMALLER_INVENTORY_MONTH,
            "inventoryMonth.greaterThan=" + DEFAULT_INVENTORY_MONTH
        );
    }

    @Test
    @Transactional
    void getAllProductInventoriesByInventoryYearIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where inventoryYear equals to
        defaultProductInventoryFiltering(
            "inventoryYear.equals=" + DEFAULT_INVENTORY_YEAR,
            "inventoryYear.equals=" + UPDATED_INVENTORY_YEAR
        );
    }

    @Test
    @Transactional
    void getAllProductInventoriesByInventoryYearIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where inventoryYear in
        defaultProductInventoryFiltering(
            "inventoryYear.in=" + DEFAULT_INVENTORY_YEAR + "," + UPDATED_INVENTORY_YEAR,
            "inventoryYear.in=" + UPDATED_INVENTORY_YEAR
        );
    }

    @Test
    @Transactional
    void getAllProductInventoriesByInventoryYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where inventoryYear is not null
        defaultProductInventoryFiltering("inventoryYear.specified=true", "inventoryYear.specified=false");
    }

    @Test
    @Transactional
    void getAllProductInventoriesByInventoryYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where inventoryYear is greater than or equal to
        defaultProductInventoryFiltering(
            "inventoryYear.greaterThanOrEqual=" + DEFAULT_INVENTORY_YEAR,
            "inventoryYear.greaterThanOrEqual=" + UPDATED_INVENTORY_YEAR
        );
    }

    @Test
    @Transactional
    void getAllProductInventoriesByInventoryYearIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where inventoryYear is less than or equal to
        defaultProductInventoryFiltering(
            "inventoryYear.lessThanOrEqual=" + DEFAULT_INVENTORY_YEAR,
            "inventoryYear.lessThanOrEqual=" + SMALLER_INVENTORY_YEAR
        );
    }

    @Test
    @Transactional
    void getAllProductInventoriesByInventoryYearIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where inventoryYear is less than
        defaultProductInventoryFiltering(
            "inventoryYear.lessThan=" + UPDATED_INVENTORY_YEAR,
            "inventoryYear.lessThan=" + DEFAULT_INVENTORY_YEAR
        );
    }

    @Test
    @Transactional
    void getAllProductInventoriesByInventoryYearIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where inventoryYear is greater than
        defaultProductInventoryFiltering(
            "inventoryYear.greaterThan=" + SMALLER_INVENTORY_YEAR,
            "inventoryYear.greaterThan=" + DEFAULT_INVENTORY_YEAR
        );
    }

    @Test
    @Transactional
    void getAllProductInventoriesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where type equals to
        defaultProductInventoryFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllProductInventoriesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where type in
        defaultProductInventoryFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllProductInventoriesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where type is not null
        defaultProductInventoryFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllProductInventoriesByTypeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where type is greater than or equal to
        defaultProductInventoryFiltering("type.greaterThanOrEqual=" + DEFAULT_TYPE, "type.greaterThanOrEqual=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllProductInventoriesByTypeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where type is less than or equal to
        defaultProductInventoryFiltering("type.lessThanOrEqual=" + DEFAULT_TYPE, "type.lessThanOrEqual=" + SMALLER_TYPE);
    }

    @Test
    @Transactional
    void getAllProductInventoriesByTypeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where type is less than
        defaultProductInventoryFiltering("type.lessThan=" + UPDATED_TYPE, "type.lessThan=" + DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void getAllProductInventoriesByTypeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where type is greater than
        defaultProductInventoryFiltering("type.greaterThan=" + SMALLER_TYPE, "type.greaterThan=" + DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void getAllProductInventoriesByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where price equals to
        defaultProductInventoryFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductInventoriesByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where price in
        defaultProductInventoryFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductInventoriesByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where price is not null
        defaultProductInventoryFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllProductInventoriesByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where price is greater than or equal to
        defaultProductInventoryFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductInventoriesByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where price is less than or equal to
        defaultProductInventoryFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllProductInventoriesByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where price is less than
        defaultProductInventoryFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductInventoriesByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where price is greater than
        defaultProductInventoryFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductInventoriesByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            productInventoryRepository.saveAndFlush(productInventory);
            product = ProductResourceIT.createEntity(em);
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product);
        em.flush();
        productInventory.setProduct(product);
        productInventoryRepository.saveAndFlush(productInventory);
        Long productId = product.getId();
        // Get all the productInventoryList where product equals to productId
        defaultProductInventoryShouldBeFound("productId.equals=" + productId);

        // Get all the productInventoryList where product equals to (productId + 1)
        defaultProductInventoryShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    private void defaultProductInventoryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductInventoryShouldBeFound(shouldBeFound);
        defaultProductInventoryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductInventoryShouldBeFound(String filter) throws Exception {
        restProductInventoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productInventory.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantityOnHand").value(hasItem(DEFAULT_QUANTITY_ON_HAND.doubleValue())))
            .andExpect(jsonPath("$.[*].inventoryMonth").value(hasItem(DEFAULT_INVENTORY_MONTH)))
            .andExpect(jsonPath("$.[*].inventoryYear").value(hasItem(DEFAULT_INVENTORY_YEAR)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))));

        // Check, that the count call also returns 1
        restProductInventoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductInventoryShouldNotBeFound(String filter) throws Exception {
        restProductInventoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductInventoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductInventory() throws Exception {
        // Get the productInventory
        restProductInventoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductInventory() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productInventory
        ProductInventory updatedProductInventory = productInventoryRepository.findById(productInventory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductInventory are not directly saved in db
        em.detach(updatedProductInventory);
        updatedProductInventory
            .quantityOnHand(UPDATED_QUANTITY_ON_HAND)
            .inventoryMonth(UPDATED_INVENTORY_MONTH)
            .inventoryYear(UPDATED_INVENTORY_YEAR)
            .type(UPDATED_TYPE)
            .price(UPDATED_PRICE);
        ProductInventoryDTO productInventoryDTO = productInventoryMapper.toDto(updatedProductInventory);

        restProductInventoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productInventoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productInventoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductInventory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductInventoryToMatchAllProperties(updatedProductInventory);
    }

    @Test
    @Transactional
    void putNonExistingProductInventory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productInventory.setId(longCount.incrementAndGet());

        // Create the ProductInventory
        ProductInventoryDTO productInventoryDTO = productInventoryMapper.toDto(productInventory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductInventoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productInventoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productInventoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductInventory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductInventory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productInventory.setId(longCount.incrementAndGet());

        // Create the ProductInventory
        ProductInventoryDTO productInventoryDTO = productInventoryMapper.toDto(productInventory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductInventoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productInventoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductInventory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductInventory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productInventory.setId(longCount.incrementAndGet());

        // Create the ProductInventory
        ProductInventoryDTO productInventoryDTO = productInventoryMapper.toDto(productInventory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductInventoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productInventoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductInventory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductInventoryWithPatch() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productInventory using partial update
        ProductInventory partialUpdatedProductInventory = new ProductInventory();
        partialUpdatedProductInventory.setId(productInventory.getId());

        partialUpdatedProductInventory.inventoryMonth(UPDATED_INVENTORY_MONTH).type(UPDATED_TYPE).price(UPDATED_PRICE);

        restProductInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductInventory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductInventory))
            )
            .andExpect(status().isOk());

        // Validate the ProductInventory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductInventoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductInventory, productInventory),
            getPersistedProductInventory(productInventory)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductInventoryWithPatch() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productInventory using partial update
        ProductInventory partialUpdatedProductInventory = new ProductInventory();
        partialUpdatedProductInventory.setId(productInventory.getId());

        partialUpdatedProductInventory
            .quantityOnHand(UPDATED_QUANTITY_ON_HAND)
            .inventoryMonth(UPDATED_INVENTORY_MONTH)
            .inventoryYear(UPDATED_INVENTORY_YEAR)
            .type(UPDATED_TYPE)
            .price(UPDATED_PRICE);

        restProductInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductInventory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductInventory))
            )
            .andExpect(status().isOk());

        // Validate the ProductInventory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductInventoryUpdatableFieldsEquals(
            partialUpdatedProductInventory,
            getPersistedProductInventory(partialUpdatedProductInventory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProductInventory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productInventory.setId(longCount.incrementAndGet());

        // Create the ProductInventory
        ProductInventoryDTO productInventoryDTO = productInventoryMapper.toDto(productInventory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productInventoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productInventoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductInventory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductInventory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productInventory.setId(longCount.incrementAndGet());

        // Create the ProductInventory
        ProductInventoryDTO productInventoryDTO = productInventoryMapper.toDto(productInventory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productInventoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductInventory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductInventory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productInventory.setId(longCount.incrementAndGet());

        // Create the ProductInventory
        ProductInventoryDTO productInventoryDTO = productInventoryMapper.toDto(productInventory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductInventoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productInventoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductInventory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductInventory() throws Exception {
        // Initialize the database
        insertedProductInventory = productInventoryRepository.saveAndFlush(productInventory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productInventory
        restProductInventoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, productInventory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productInventoryRepository.count();
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

    protected ProductInventory getPersistedProductInventory(ProductInventory productInventory) {
        return productInventoryRepository.findById(productInventory.getId()).orElseThrow();
    }

    protected void assertPersistedProductInventoryToMatchAllProperties(ProductInventory expectedProductInventory) {
        assertProductInventoryAllPropertiesEquals(expectedProductInventory, getPersistedProductInventory(expectedProductInventory));
    }

    protected void assertPersistedProductInventoryToMatchUpdatableProperties(ProductInventory expectedProductInventory) {
        assertProductInventoryAllUpdatablePropertiesEquals(
            expectedProductInventory,
            getPersistedProductInventory(expectedProductInventory)
        );
    }
}
