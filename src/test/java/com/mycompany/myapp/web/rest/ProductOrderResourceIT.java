package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ProductOrderAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Customer;
import com.mycompany.myapp.domain.Planning;
import com.mycompany.myapp.domain.ProductOrder;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.ProductOrderRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.service.dto.ProductOrderDTO;
import com.mycompany.myapp.service.mapper.ProductOrderMapper;
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
 * Integration tests for the {@link ProductOrderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductOrderResourceIT {

    private static final String DEFAULT_PAYMENT_METHOD = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_METHOD = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;
    private static final Integer SMALLER_STATUS = 1 - 1;

    private static final Instant DEFAULT_ORDER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_PAYMENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAYMENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_WAREHOUSE_RELEASE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_WAREHOUSE_RELEASE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductOrderRepository productOrderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductOrderMapper productOrderMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductOrderMockMvc;

    private ProductOrder productOrder;

    private ProductOrder insertedProductOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductOrder createEntity(EntityManager em) {
        ProductOrder productOrder = new ProductOrder()
            .paymentMethod(DEFAULT_PAYMENT_METHOD)
            .note(DEFAULT_NOTE)
            .status(DEFAULT_STATUS)
            .orderDate(DEFAULT_ORDER_DATE)
            .paymentDate(DEFAULT_PAYMENT_DATE)
            .warehouseReleaseDate(DEFAULT_WAREHOUSE_RELEASE_DATE)
            .code(DEFAULT_CODE);
        return productOrder;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductOrder createUpdatedEntity(EntityManager em) {
        ProductOrder productOrder = new ProductOrder()
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .note(UPDATED_NOTE)
            .status(UPDATED_STATUS)
            .orderDate(UPDATED_ORDER_DATE)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .warehouseReleaseDate(UPDATED_WAREHOUSE_RELEASE_DATE)
            .code(UPDATED_CODE);
        return productOrder;
    }

    @BeforeEach
    public void initTest() {
        productOrder = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductOrder != null) {
            productOrderRepository.delete(insertedProductOrder);
            insertedProductOrder = null;
        }
    }

    @Test
    @Transactional
    void createProductOrder() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductOrder
        ProductOrderDTO productOrderDTO = productOrderMapper.toDto(productOrder);
        var returnedProductOrderDTO = om.readValue(
            restProductOrderMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productOrderDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductOrderDTO.class
        );

        // Validate the ProductOrder in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProductOrder = productOrderMapper.toEntity(returnedProductOrderDTO);
        assertProductOrderUpdatableFieldsEquals(returnedProductOrder, getPersistedProductOrder(returnedProductOrder));

        insertedProductOrder = returnedProductOrder;
    }

    @Test
    @Transactional
    void createProductOrderWithExistingId() throws Exception {
        // Create the ProductOrder with an existing ID
        productOrder.setId(1L);
        ProductOrderDTO productOrderDTO = productOrderMapper.toDto(productOrder);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productOrderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductOrders() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList
        restProductOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].warehouseReleaseDate").value(hasItem(DEFAULT_WAREHOUSE_RELEASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getProductOrder() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        // Get the productOrder
        restProductOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, productOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productOrder.getId().intValue()))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.warehouseReleaseDate").value(DEFAULT_WAREHOUSE_RELEASE_DATE.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getProductOrdersByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        Long id = productOrder.getId();

        defaultProductOrderFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductOrderFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductOrderFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductOrdersByPaymentMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where paymentMethod equals to
        defaultProductOrderFiltering("paymentMethod.equals=" + DEFAULT_PAYMENT_METHOD, "paymentMethod.equals=" + UPDATED_PAYMENT_METHOD);
    }

    @Test
    @Transactional
    void getAllProductOrdersByPaymentMethodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where paymentMethod in
        defaultProductOrderFiltering(
            "paymentMethod.in=" + DEFAULT_PAYMENT_METHOD + "," + UPDATED_PAYMENT_METHOD,
            "paymentMethod.in=" + UPDATED_PAYMENT_METHOD
        );
    }

    @Test
    @Transactional
    void getAllProductOrdersByPaymentMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where paymentMethod is not null
        defaultProductOrderFiltering("paymentMethod.specified=true", "paymentMethod.specified=false");
    }

    @Test
    @Transactional
    void getAllProductOrdersByPaymentMethodContainsSomething() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where paymentMethod contains
        defaultProductOrderFiltering(
            "paymentMethod.contains=" + DEFAULT_PAYMENT_METHOD,
            "paymentMethod.contains=" + UPDATED_PAYMENT_METHOD
        );
    }

    @Test
    @Transactional
    void getAllProductOrdersByPaymentMethodNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where paymentMethod does not contain
        defaultProductOrderFiltering(
            "paymentMethod.doesNotContain=" + UPDATED_PAYMENT_METHOD,
            "paymentMethod.doesNotContain=" + DEFAULT_PAYMENT_METHOD
        );
    }

    @Test
    @Transactional
    void getAllProductOrdersByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where note equals to
        defaultProductOrderFiltering("note.equals=" + DEFAULT_NOTE, "note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllProductOrdersByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where note in
        defaultProductOrderFiltering("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE, "note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllProductOrdersByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where note is not null
        defaultProductOrderFiltering("note.specified=true", "note.specified=false");
    }

    @Test
    @Transactional
    void getAllProductOrdersByNoteContainsSomething() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where note contains
        defaultProductOrderFiltering("note.contains=" + DEFAULT_NOTE, "note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllProductOrdersByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where note does not contain
        defaultProductOrderFiltering("note.doesNotContain=" + UPDATED_NOTE, "note.doesNotContain=" + DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void getAllProductOrdersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where status equals to
        defaultProductOrderFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllProductOrdersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where status in
        defaultProductOrderFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllProductOrdersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where status is not null
        defaultProductOrderFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllProductOrdersByStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where status is greater than or equal to
        defaultProductOrderFiltering("status.greaterThanOrEqual=" + DEFAULT_STATUS, "status.greaterThanOrEqual=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllProductOrdersByStatusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where status is less than or equal to
        defaultProductOrderFiltering("status.lessThanOrEqual=" + DEFAULT_STATUS, "status.lessThanOrEqual=" + SMALLER_STATUS);
    }

    @Test
    @Transactional
    void getAllProductOrdersByStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where status is less than
        defaultProductOrderFiltering("status.lessThan=" + UPDATED_STATUS, "status.lessThan=" + DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void getAllProductOrdersByStatusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where status is greater than
        defaultProductOrderFiltering("status.greaterThan=" + SMALLER_STATUS, "status.greaterThan=" + DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void getAllProductOrdersByOrderDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where orderDate equals to
        defaultProductOrderFiltering("orderDate.equals=" + DEFAULT_ORDER_DATE, "orderDate.equals=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllProductOrdersByOrderDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where orderDate in
        defaultProductOrderFiltering("orderDate.in=" + DEFAULT_ORDER_DATE + "," + UPDATED_ORDER_DATE, "orderDate.in=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllProductOrdersByOrderDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where orderDate is not null
        defaultProductOrderFiltering("orderDate.specified=true", "orderDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductOrdersByPaymentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where paymentDate equals to
        defaultProductOrderFiltering("paymentDate.equals=" + DEFAULT_PAYMENT_DATE, "paymentDate.equals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllProductOrdersByPaymentDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where paymentDate in
        defaultProductOrderFiltering(
            "paymentDate.in=" + DEFAULT_PAYMENT_DATE + "," + UPDATED_PAYMENT_DATE,
            "paymentDate.in=" + UPDATED_PAYMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllProductOrdersByPaymentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where paymentDate is not null
        defaultProductOrderFiltering("paymentDate.specified=true", "paymentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductOrdersByWarehouseReleaseDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where warehouseReleaseDate equals to
        defaultProductOrderFiltering(
            "warehouseReleaseDate.equals=" + DEFAULT_WAREHOUSE_RELEASE_DATE,
            "warehouseReleaseDate.equals=" + UPDATED_WAREHOUSE_RELEASE_DATE
        );
    }

    @Test
    @Transactional
    void getAllProductOrdersByWarehouseReleaseDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where warehouseReleaseDate in
        defaultProductOrderFiltering(
            "warehouseReleaseDate.in=" + DEFAULT_WAREHOUSE_RELEASE_DATE + "," + UPDATED_WAREHOUSE_RELEASE_DATE,
            "warehouseReleaseDate.in=" + UPDATED_WAREHOUSE_RELEASE_DATE
        );
    }

    @Test
    @Transactional
    void getAllProductOrdersByWarehouseReleaseDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where warehouseReleaseDate is not null
        defaultProductOrderFiltering("warehouseReleaseDate.specified=true", "warehouseReleaseDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductOrdersByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where code equals to
        defaultProductOrderFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllProductOrdersByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where code in
        defaultProductOrderFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllProductOrdersByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where code is not null
        defaultProductOrderFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllProductOrdersByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where code contains
        defaultProductOrderFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllProductOrdersByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList where code does not contain
        defaultProductOrderFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllProductOrdersByQuantificationOrderIsEqualToSomething() throws Exception {
        Planning quantificationOrder;
        if (TestUtil.findAll(em, Planning.class).isEmpty()) {
            productOrderRepository.saveAndFlush(productOrder);
            quantificationOrder = PlanningResourceIT.createEntity(em);
        } else {
            quantificationOrder = TestUtil.findAll(em, Planning.class).get(0);
        }
        em.persist(quantificationOrder);
        em.flush();
        productOrder.setQuantificationOrder(quantificationOrder);
        productOrderRepository.saveAndFlush(productOrder);
        Long quantificationOrderId = quantificationOrder.getId();
        // Get all the productOrderList where quantificationOrder equals to quantificationOrderId
        defaultProductOrderShouldBeFound("quantificationOrderId.equals=" + quantificationOrderId);

        // Get all the productOrderList where quantificationOrder equals to (quantificationOrderId + 1)
        defaultProductOrderShouldNotBeFound("quantificationOrderId.equals=" + (quantificationOrderId + 1));
    }

    @Test
    @Transactional
    void getAllProductOrdersByCustomerIsEqualToSomething() throws Exception {
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            productOrderRepository.saveAndFlush(productOrder);
            customer = CustomerResourceIT.createEntity(em);
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        em.persist(customer);
        em.flush();
        productOrder.setCustomer(customer);
        productOrderRepository.saveAndFlush(productOrder);
        Long customerId = customer.getId();
        // Get all the productOrderList where customer equals to customerId
        defaultProductOrderShouldBeFound("customerId.equals=" + customerId);

        // Get all the productOrderList where customer equals to (customerId + 1)
        defaultProductOrderShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    @Test
    @Transactional
    void getAllProductOrdersByCreatedByUserIsEqualToSomething() throws Exception {
        User createdByUser;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            productOrderRepository.saveAndFlush(productOrder);
            createdByUser = UserResourceIT.createEntity(em);
        } else {
            createdByUser = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(createdByUser);
        em.flush();
        productOrder.setCreatedByUser(createdByUser);
        productOrderRepository.saveAndFlush(productOrder);
        Long createdByUserId = createdByUser.getId();
        // Get all the productOrderList where createdByUser equals to createdByUserId
        defaultProductOrderShouldBeFound("createdByUserId.equals=" + createdByUserId);

        // Get all the productOrderList where createdByUser equals to (createdByUserId + 1)
        defaultProductOrderShouldNotBeFound("createdByUserId.equals=" + (createdByUserId + 1));
    }

    private void defaultProductOrderFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductOrderShouldBeFound(shouldBeFound);
        defaultProductOrderShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductOrderShouldBeFound(String filter) throws Exception {
        restProductOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].warehouseReleaseDate").value(hasItem(DEFAULT_WAREHOUSE_RELEASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));

        // Check, that the count call also returns 1
        restProductOrderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductOrderShouldNotBeFound(String filter) throws Exception {
        restProductOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductOrderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductOrder() throws Exception {
        // Get the productOrder
        restProductOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductOrder() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productOrder
        ProductOrder updatedProductOrder = productOrderRepository.findById(productOrder.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductOrder are not directly saved in db
        em.detach(updatedProductOrder);
        updatedProductOrder
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .note(UPDATED_NOTE)
            .status(UPDATED_STATUS)
            .orderDate(UPDATED_ORDER_DATE)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .warehouseReleaseDate(UPDATED_WAREHOUSE_RELEASE_DATE)
            .code(UPDATED_CODE);
        ProductOrderDTO productOrderDTO = productOrderMapper.toDto(updatedProductOrder);

        restProductOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productOrderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productOrderDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductOrderToMatchAllProperties(updatedProductOrder);
    }

    @Test
    @Transactional
    void putNonExistingProductOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productOrder.setId(longCount.incrementAndGet());

        // Create the ProductOrder
        ProductOrderDTO productOrderDTO = productOrderMapper.toDto(productOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productOrderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productOrder.setId(longCount.incrementAndGet());

        // Create the ProductOrder
        ProductOrderDTO productOrderDTO = productOrderMapper.toDto(productOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productOrder.setId(longCount.incrementAndGet());

        // Create the ProductOrder
        ProductOrderDTO productOrderDTO = productOrderMapper.toDto(productOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductOrderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productOrderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductOrderWithPatch() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productOrder using partial update
        ProductOrder partialUpdatedProductOrder = new ProductOrder();
        partialUpdatedProductOrder.setId(productOrder.getId());

        partialUpdatedProductOrder.status(UPDATED_STATUS).paymentDate(UPDATED_PAYMENT_DATE);

        restProductOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductOrder))
            )
            .andExpect(status().isOk());

        // Validate the ProductOrder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductOrderUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductOrder, productOrder),
            getPersistedProductOrder(productOrder)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductOrderWithPatch() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productOrder using partial update
        ProductOrder partialUpdatedProductOrder = new ProductOrder();
        partialUpdatedProductOrder.setId(productOrder.getId());

        partialUpdatedProductOrder
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .note(UPDATED_NOTE)
            .status(UPDATED_STATUS)
            .orderDate(UPDATED_ORDER_DATE)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .warehouseReleaseDate(UPDATED_WAREHOUSE_RELEASE_DATE)
            .code(UPDATED_CODE);

        restProductOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductOrder))
            )
            .andExpect(status().isOk());

        // Validate the ProductOrder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductOrderUpdatableFieldsEquals(partialUpdatedProductOrder, getPersistedProductOrder(partialUpdatedProductOrder));
    }

    @Test
    @Transactional
    void patchNonExistingProductOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productOrder.setId(longCount.incrementAndGet());

        // Create the ProductOrder
        ProductOrderDTO productOrderDTO = productOrderMapper.toDto(productOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productOrderDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productOrder.setId(longCount.incrementAndGet());

        // Create the ProductOrder
        ProductOrderDTO productOrderDTO = productOrderMapper.toDto(productOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productOrder.setId(longCount.incrementAndGet());

        // Create the ProductOrder
        ProductOrderDTO productOrderDTO = productOrderMapper.toDto(productOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductOrderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productOrderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductOrder() throws Exception {
        // Initialize the database
        insertedProductOrder = productOrderRepository.saveAndFlush(productOrder);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productOrder
        restProductOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, productOrder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productOrderRepository.count();
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

    protected ProductOrder getPersistedProductOrder(ProductOrder productOrder) {
        return productOrderRepository.findById(productOrder.getId()).orElseThrow();
    }

    protected void assertPersistedProductOrderToMatchAllProperties(ProductOrder expectedProductOrder) {
        assertProductOrderAllPropertiesEquals(expectedProductOrder, getPersistedProductOrder(expectedProductOrder));
    }

    protected void assertPersistedProductOrderToMatchUpdatableProperties(ProductOrder expectedProductOrder) {
        assertProductOrderAllUpdatablePropertiesEquals(expectedProductOrder, getPersistedProductOrder(expectedProductOrder));
    }
}
