package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ProductOrderDetailAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.domain.ProductOrder;
import com.mycompany.myapp.domain.ProductOrderDetail;
import com.mycompany.myapp.repository.ProductOrderDetailRepository;
import com.mycompany.myapp.service.dto.ProductOrderDetailDTO;
import com.mycompany.myapp.service.mapper.ProductOrderDetailMapper;
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
 * Integration tests for the {@link ProductOrderDetailResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductOrderDetailResourceIT {

    private static final Instant DEFAULT_ORDER_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;
    private static final Integer SMALLER_QUANTITY = 1 - 1;

    private static final Float DEFAULT_UNIT_PRICE = 1F;
    private static final Float UPDATED_UNIT_PRICE = 2F;
    private static final Float SMALLER_UNIT_PRICE = 1F - 1F;

    private static final String ENTITY_API_URL = "/api/product-order-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductOrderDetailRepository productOrderDetailRepository;

    @Autowired
    private ProductOrderDetailMapper productOrderDetailMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductOrderDetailMockMvc;

    private ProductOrderDetail productOrderDetail;

    private ProductOrderDetail insertedProductOrderDetail;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductOrderDetail createEntity(EntityManager em) {
        ProductOrderDetail productOrderDetail = new ProductOrderDetail()
            .orderCreationDate(DEFAULT_ORDER_CREATION_DATE)
            .quantity(DEFAULT_QUANTITY)
            .unitPrice(DEFAULT_UNIT_PRICE);
        return productOrderDetail;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductOrderDetail createUpdatedEntity(EntityManager em) {
        ProductOrderDetail productOrderDetail = new ProductOrderDetail()
            .orderCreationDate(UPDATED_ORDER_CREATION_DATE)
            .quantity(UPDATED_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE);
        return productOrderDetail;
    }

    @BeforeEach
    public void initTest() {
        productOrderDetail = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductOrderDetail != null) {
            productOrderDetailRepository.delete(insertedProductOrderDetail);
            insertedProductOrderDetail = null;
        }
    }

    @Test
    @Transactional
    void createProductOrderDetail() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductOrderDetail
        ProductOrderDetailDTO productOrderDetailDTO = productOrderDetailMapper.toDto(productOrderDetail);
        var returnedProductOrderDetailDTO = om.readValue(
            restProductOrderDetailMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productOrderDetailDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductOrderDetailDTO.class
        );

        // Validate the ProductOrderDetail in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProductOrderDetail = productOrderDetailMapper.toEntity(returnedProductOrderDetailDTO);
        assertProductOrderDetailUpdatableFieldsEquals(
            returnedProductOrderDetail,
            getPersistedProductOrderDetail(returnedProductOrderDetail)
        );

        insertedProductOrderDetail = returnedProductOrderDetail;
    }

    @Test
    @Transactional
    void createProductOrderDetailWithExistingId() throws Exception {
        // Create the ProductOrderDetail with an existing ID
        productOrderDetail.setId(1L);
        ProductOrderDetailDTO productOrderDetailDTO = productOrderDetailMapper.toDto(productOrderDetail);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductOrderDetailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productOrderDetailDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductOrderDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductOrderDetails() throws Exception {
        // Initialize the database
        insertedProductOrderDetail = productOrderDetailRepository.saveAndFlush(productOrderDetail);

        // Get all the productOrderDetailList
        restProductOrderDetailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productOrderDetail.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderCreationDate").value(hasItem(DEFAULT_ORDER_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    void getProductOrderDetail() throws Exception {
        // Initialize the database
        insertedProductOrderDetail = productOrderDetailRepository.saveAndFlush(productOrderDetail);

        // Get the productOrderDetail
        restProductOrderDetailMockMvc
            .perform(get(ENTITY_API_URL_ID, productOrderDetail.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productOrderDetail.getId().intValue()))
            .andExpect(jsonPath("$.orderCreationDate").value(DEFAULT_ORDER_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.unitPrice").value(DEFAULT_UNIT_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    void getProductOrderDetailsByIdFiltering() throws Exception {
        // Initialize the database
        insertedProductOrderDetail = productOrderDetailRepository.saveAndFlush(productOrderDetail);

        Long id = productOrderDetail.getId();

        defaultProductOrderDetailFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductOrderDetailFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductOrderDetailFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductOrderDetailsByOrderCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductOrderDetail = productOrderDetailRepository.saveAndFlush(productOrderDetail);

        // Get all the productOrderDetailList where orderCreationDate equals to
        defaultProductOrderDetailFiltering(
            "orderCreationDate.equals=" + DEFAULT_ORDER_CREATION_DATE,
            "orderCreationDate.equals=" + UPDATED_ORDER_CREATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllProductOrderDetailsByOrderCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductOrderDetail = productOrderDetailRepository.saveAndFlush(productOrderDetail);

        // Get all the productOrderDetailList where orderCreationDate in
        defaultProductOrderDetailFiltering(
            "orderCreationDate.in=" + DEFAULT_ORDER_CREATION_DATE + "," + UPDATED_ORDER_CREATION_DATE,
            "orderCreationDate.in=" + UPDATED_ORDER_CREATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllProductOrderDetailsByOrderCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductOrderDetail = productOrderDetailRepository.saveAndFlush(productOrderDetail);

        // Get all the productOrderDetailList where orderCreationDate is not null
        defaultProductOrderDetailFiltering("orderCreationDate.specified=true", "orderCreationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductOrderDetailsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductOrderDetail = productOrderDetailRepository.saveAndFlush(productOrderDetail);

        // Get all the productOrderDetailList where quantity equals to
        defaultProductOrderDetailFiltering("quantity.equals=" + DEFAULT_QUANTITY, "quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductOrderDetailsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductOrderDetail = productOrderDetailRepository.saveAndFlush(productOrderDetail);

        // Get all the productOrderDetailList where quantity in
        defaultProductOrderDetailFiltering("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY, "quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductOrderDetailsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductOrderDetail = productOrderDetailRepository.saveAndFlush(productOrderDetail);

        // Get all the productOrderDetailList where quantity is not null
        defaultProductOrderDetailFiltering("quantity.specified=true", "quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllProductOrderDetailsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductOrderDetail = productOrderDetailRepository.saveAndFlush(productOrderDetail);

        // Get all the productOrderDetailList where quantity is greater than or equal to
        defaultProductOrderDetailFiltering(
            "quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY,
            "quantity.greaterThanOrEqual=" + UPDATED_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllProductOrderDetailsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductOrderDetail = productOrderDetailRepository.saveAndFlush(productOrderDetail);

        // Get all the productOrderDetailList where quantity is less than or equal to
        defaultProductOrderDetailFiltering("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY, "quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductOrderDetailsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductOrderDetail = productOrderDetailRepository.saveAndFlush(productOrderDetail);

        // Get all the productOrderDetailList where quantity is less than
        defaultProductOrderDetailFiltering("quantity.lessThan=" + UPDATED_QUANTITY, "quantity.lessThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductOrderDetailsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductOrderDetail = productOrderDetailRepository.saveAndFlush(productOrderDetail);

        // Get all the productOrderDetailList where quantity is greater than
        defaultProductOrderDetailFiltering("quantity.greaterThan=" + SMALLER_QUANTITY, "quantity.greaterThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductOrderDetailsByUnitPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductOrderDetail = productOrderDetailRepository.saveAndFlush(productOrderDetail);

        // Get all the productOrderDetailList where unitPrice equals to
        defaultProductOrderDetailFiltering("unitPrice.equals=" + DEFAULT_UNIT_PRICE, "unitPrice.equals=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductOrderDetailsByUnitPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProductOrderDetail = productOrderDetailRepository.saveAndFlush(productOrderDetail);

        // Get all the productOrderDetailList where unitPrice in
        defaultProductOrderDetailFiltering(
            "unitPrice.in=" + DEFAULT_UNIT_PRICE + "," + UPDATED_UNIT_PRICE,
            "unitPrice.in=" + UPDATED_UNIT_PRICE
        );
    }

    @Test
    @Transactional
    void getAllProductOrderDetailsByUnitPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProductOrderDetail = productOrderDetailRepository.saveAndFlush(productOrderDetail);

        // Get all the productOrderDetailList where unitPrice is not null
        defaultProductOrderDetailFiltering("unitPrice.specified=true", "unitPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllProductOrderDetailsByUnitPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductOrderDetail = productOrderDetailRepository.saveAndFlush(productOrderDetail);

        // Get all the productOrderDetailList where unitPrice is greater than or equal to
        defaultProductOrderDetailFiltering(
            "unitPrice.greaterThanOrEqual=" + DEFAULT_UNIT_PRICE,
            "unitPrice.greaterThanOrEqual=" + UPDATED_UNIT_PRICE
        );
    }

    @Test
    @Transactional
    void getAllProductOrderDetailsByUnitPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProductOrderDetail = productOrderDetailRepository.saveAndFlush(productOrderDetail);

        // Get all the productOrderDetailList where unitPrice is less than or equal to
        defaultProductOrderDetailFiltering(
            "unitPrice.lessThanOrEqual=" + DEFAULT_UNIT_PRICE,
            "unitPrice.lessThanOrEqual=" + SMALLER_UNIT_PRICE
        );
    }

    @Test
    @Transactional
    void getAllProductOrderDetailsByUnitPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProductOrderDetail = productOrderDetailRepository.saveAndFlush(productOrderDetail);

        // Get all the productOrderDetailList where unitPrice is less than
        defaultProductOrderDetailFiltering("unitPrice.lessThan=" + UPDATED_UNIT_PRICE, "unitPrice.lessThan=" + DEFAULT_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductOrderDetailsByUnitPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProductOrderDetail = productOrderDetailRepository.saveAndFlush(productOrderDetail);

        // Get all the productOrderDetailList where unitPrice is greater than
        defaultProductOrderDetailFiltering("unitPrice.greaterThan=" + SMALLER_UNIT_PRICE, "unitPrice.greaterThan=" + DEFAULT_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductOrderDetailsByOrderIsEqualToSomething() throws Exception {
        ProductOrder order;
        if (TestUtil.findAll(em, ProductOrder.class).isEmpty()) {
            productOrderDetailRepository.saveAndFlush(productOrderDetail);
            order = ProductOrderResourceIT.createEntity(em);
        } else {
            order = TestUtil.findAll(em, ProductOrder.class).get(0);
        }
        em.persist(order);
        em.flush();
        productOrderDetail.setOrder(order);
        productOrderDetailRepository.saveAndFlush(productOrderDetail);
        Long orderId = order.getId();
        // Get all the productOrderDetailList where order equals to orderId
        defaultProductOrderDetailShouldBeFound("orderId.equals=" + orderId);

        // Get all the productOrderDetailList where order equals to (orderId + 1)
        defaultProductOrderDetailShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllProductOrderDetailsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            productOrderDetailRepository.saveAndFlush(productOrderDetail);
            product = ProductResourceIT.createEntity(em);
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product);
        em.flush();
        productOrderDetail.setProduct(product);
        productOrderDetailRepository.saveAndFlush(productOrderDetail);
        Long productId = product.getId();
        // Get all the productOrderDetailList where product equals to productId
        defaultProductOrderDetailShouldBeFound("productId.equals=" + productId);

        // Get all the productOrderDetailList where product equals to (productId + 1)
        defaultProductOrderDetailShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    private void defaultProductOrderDetailFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductOrderDetailShouldBeFound(shouldBeFound);
        defaultProductOrderDetailShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductOrderDetailShouldBeFound(String filter) throws Exception {
        restProductOrderDetailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productOrderDetail.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderCreationDate").value(hasItem(DEFAULT_ORDER_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.doubleValue())));

        // Check, that the count call also returns 1
        restProductOrderDetailMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductOrderDetailShouldNotBeFound(String filter) throws Exception {
        restProductOrderDetailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductOrderDetailMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductOrderDetail() throws Exception {
        // Get the productOrderDetail
        restProductOrderDetailMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductOrderDetail() throws Exception {
        // Initialize the database
        insertedProductOrderDetail = productOrderDetailRepository.saveAndFlush(productOrderDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productOrderDetail
        ProductOrderDetail updatedProductOrderDetail = productOrderDetailRepository.findById(productOrderDetail.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductOrderDetail are not directly saved in db
        em.detach(updatedProductOrderDetail);
        updatedProductOrderDetail.orderCreationDate(UPDATED_ORDER_CREATION_DATE).quantity(UPDATED_QUANTITY).unitPrice(UPDATED_UNIT_PRICE);
        ProductOrderDetailDTO productOrderDetailDTO = productOrderDetailMapper.toDto(updatedProductOrderDetail);

        restProductOrderDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productOrderDetailDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productOrderDetailDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductOrderDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductOrderDetailToMatchAllProperties(updatedProductOrderDetail);
    }

    @Test
    @Transactional
    void putNonExistingProductOrderDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productOrderDetail.setId(longCount.incrementAndGet());

        // Create the ProductOrderDetail
        ProductOrderDetailDTO productOrderDetailDTO = productOrderDetailMapper.toDto(productOrderDetail);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductOrderDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productOrderDetailDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productOrderDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductOrderDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductOrderDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productOrderDetail.setId(longCount.incrementAndGet());

        // Create the ProductOrderDetail
        ProductOrderDetailDTO productOrderDetailDTO = productOrderDetailMapper.toDto(productOrderDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductOrderDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productOrderDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductOrderDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductOrderDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productOrderDetail.setId(longCount.incrementAndGet());

        // Create the ProductOrderDetail
        ProductOrderDetailDTO productOrderDetailDTO = productOrderDetailMapper.toDto(productOrderDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductOrderDetailMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productOrderDetailDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductOrderDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductOrderDetailWithPatch() throws Exception {
        // Initialize the database
        insertedProductOrderDetail = productOrderDetailRepository.saveAndFlush(productOrderDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productOrderDetail using partial update
        ProductOrderDetail partialUpdatedProductOrderDetail = new ProductOrderDetail();
        partialUpdatedProductOrderDetail.setId(productOrderDetail.getId());

        partialUpdatedProductOrderDetail.quantity(UPDATED_QUANTITY).unitPrice(UPDATED_UNIT_PRICE);

        restProductOrderDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductOrderDetail.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductOrderDetail))
            )
            .andExpect(status().isOk());

        // Validate the ProductOrderDetail in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductOrderDetailUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductOrderDetail, productOrderDetail),
            getPersistedProductOrderDetail(productOrderDetail)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductOrderDetailWithPatch() throws Exception {
        // Initialize the database
        insertedProductOrderDetail = productOrderDetailRepository.saveAndFlush(productOrderDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productOrderDetail using partial update
        ProductOrderDetail partialUpdatedProductOrderDetail = new ProductOrderDetail();
        partialUpdatedProductOrderDetail.setId(productOrderDetail.getId());

        partialUpdatedProductOrderDetail
            .orderCreationDate(UPDATED_ORDER_CREATION_DATE)
            .quantity(UPDATED_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE);

        restProductOrderDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductOrderDetail.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductOrderDetail))
            )
            .andExpect(status().isOk());

        // Validate the ProductOrderDetail in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductOrderDetailUpdatableFieldsEquals(
            partialUpdatedProductOrderDetail,
            getPersistedProductOrderDetail(partialUpdatedProductOrderDetail)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProductOrderDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productOrderDetail.setId(longCount.incrementAndGet());

        // Create the ProductOrderDetail
        ProductOrderDetailDTO productOrderDetailDTO = productOrderDetailMapper.toDto(productOrderDetail);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductOrderDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productOrderDetailDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productOrderDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductOrderDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductOrderDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productOrderDetail.setId(longCount.incrementAndGet());

        // Create the ProductOrderDetail
        ProductOrderDetailDTO productOrderDetailDTO = productOrderDetailMapper.toDto(productOrderDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductOrderDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productOrderDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductOrderDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductOrderDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productOrderDetail.setId(longCount.incrementAndGet());

        // Create the ProductOrderDetail
        ProductOrderDetailDTO productOrderDetailDTO = productOrderDetailMapper.toDto(productOrderDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductOrderDetailMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productOrderDetailDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductOrderDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductOrderDetail() throws Exception {
        // Initialize the database
        insertedProductOrderDetail = productOrderDetailRepository.saveAndFlush(productOrderDetail);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productOrderDetail
        restProductOrderDetailMockMvc
            .perform(delete(ENTITY_API_URL_ID, productOrderDetail.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productOrderDetailRepository.count();
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

    protected ProductOrderDetail getPersistedProductOrderDetail(ProductOrderDetail productOrderDetail) {
        return productOrderDetailRepository.findById(productOrderDetail.getId()).orElseThrow();
    }

    protected void assertPersistedProductOrderDetailToMatchAllProperties(ProductOrderDetail expectedProductOrderDetail) {
        assertProductOrderDetailAllPropertiesEquals(expectedProductOrderDetail, getPersistedProductOrderDetail(expectedProductOrderDetail));
    }

    protected void assertPersistedProductOrderDetailToMatchUpdatableProperties(ProductOrderDetail expectedProductOrderDetail) {
        assertProductOrderDetailAllUpdatablePropertiesEquals(
            expectedProductOrderDetail,
            getPersistedProductOrderDetail(expectedProductOrderDetail)
        );
    }
}
