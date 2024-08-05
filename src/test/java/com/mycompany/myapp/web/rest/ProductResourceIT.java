package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ProductAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Material;
import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.repository.ProductRepository;
import com.mycompany.myapp.service.dto.ProductDTO;
import com.mycompany.myapp.service.mapper.ProductMapper;
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
 * Integration tests for the {@link ProductResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Float DEFAULT_WEIGHT = 1F;
    private static final Float UPDATED_WEIGHT = 2F;
    private static final Float SMALLER_WEIGHT = 1F - 1F;

    private static final Float DEFAULT_HEIGHT = 1F;
    private static final Float UPDATED_HEIGHT = 2F;
    private static final Float SMALLER_HEIGHT = 1F - 1F;

    private static final Float DEFAULT_WIDTH = 1F;
    private static final Float UPDATED_WIDTH = 2F;
    private static final Float SMALLER_WIDTH = 1F - 1F;

    private static final Float DEFAULT_LENGTH = 1F;
    private static final Float UPDATED_LENGTH = 2F;
    private static final Float SMALLER_LENGTH = 1F - 1F;

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final Integer DEFAULT_TYPE = 1;
    private static final Integer UPDATED_TYPE = 2;
    private static final Integer SMALLER_TYPE = 1 - 1;

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    private static final Float DEFAULT_CBM = 1F;
    private static final Float UPDATED_CBM = 2F;
    private static final Float SMALLER_CBM = 1F - 1F;

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;
    private static final Float SMALLER_PRICE = 1F - 1F;

    private static final Integer DEFAULT_CONSTRUCTION = 1;
    private static final Integer UPDATED_CONSTRUCTION = 2;
    private static final Integer SMALLER_CONSTRUCTION = 1 - 1;

    private static final Integer DEFAULT_MASTER_PACKING_QTY = 1;
    private static final Integer UPDATED_MASTER_PACKING_QTY = 2;
    private static final Integer SMALLER_MASTER_PACKING_QTY = 1 - 1;

    private static final Integer DEFAULT_MASTER_NEST_CODE = 1;
    private static final Integer UPDATED_MASTER_NEST_CODE = 2;
    private static final Integer SMALLER_MASTER_NEST_CODE = 1 - 1;

    private static final Integer DEFAULT_INNER_QTY = 1;
    private static final Integer UPDATED_INNER_QTY = 2;
    private static final Integer SMALLER_INNER_QTY = 1 - 1;

    private static final Integer DEFAULT_PACK_SIZE = 1;
    private static final Integer UPDATED_PACK_SIZE = 2;
    private static final Integer SMALLER_PACK_SIZE = 1 - 1;

    private static final String DEFAULT_NEST_CODE = "AAAAAAAAAA";
    private static final String UPDATED_NEST_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY_OF_ORIGIN = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY_OF_ORIGIN = "BBBBBBBBBB";

    private static final String DEFAULT_VENDOR_NAME = "AAAAAAAAAA";
    private static final String UPDATED_VENDOR_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMBER_OF_SET = 1;
    private static final Integer UPDATED_NUMBER_OF_SET = 2;
    private static final Integer SMALLER_NUMBER_OF_SET = 1 - 1;

    private static final Float DEFAULT_PRICE_FOB = 1F;
    private static final Float UPDATED_PRICE_FOB = 2F;
    private static final Float SMALLER_PRICE_FOB = 1F - 1F;

    private static final Integer DEFAULT_QTY_40_HC = 1;
    private static final Integer UPDATED_QTY_40_HC = 2;
    private static final Integer SMALLER_QTY_40_HC = 1 - 1;

    private static final Integer DEFAULT_D_57_QTY = 1;
    private static final Integer UPDATED_D_57_QTY = 2;
    private static final Integer SMALLER_D_57_QTY = 1 - 1;

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_LABELS = "AAAAAAAAAA";
    private static final String UPDATED_LABELS = "BBBBBBBBBB";

    private static final String DEFAULT_PLANNING_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_PLANNING_NOTES = "BBBBBBBBBB";

    private static final String DEFAULT_FACT_TAG = "AAAAAAAAAA";
    private static final String UPDATED_FACT_TAG = "BBBBBBBBBB";

    private static final Float DEFAULT_PACKAGING_LENGTH = 1F;
    private static final Float UPDATED_PACKAGING_LENGTH = 2F;
    private static final Float SMALLER_PACKAGING_LENGTH = 1F - 1F;

    private static final Float DEFAULT_PACKAGING_HEIGHT = 1F;
    private static final Float UPDATED_PACKAGING_HEIGHT = 2F;
    private static final Float SMALLER_PACKAGING_HEIGHT = 1F - 1F;

    private static final Float DEFAULT_PACKAGING_WIDTH = 1F;
    private static final Float UPDATED_PACKAGING_WIDTH = 2F;
    private static final Float SMALLER_PACKAGING_WIDTH = 1F - 1F;

    private static final String ENTITY_API_URL = "/api/products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductMockMvc;

    private Product product;

    private Product insertedProduct;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createEntity(EntityManager em) {
        Product product = new Product()
            .name(DEFAULT_NAME)
            .code(DEFAULT_CODE)
            .unit(DEFAULT_UNIT)
            .description(DEFAULT_DESCRIPTION)
            .weight(DEFAULT_WEIGHT)
            .height(DEFAULT_HEIGHT)
            .width(DEFAULT_WIDTH)
            .length(DEFAULT_LENGTH)
            .imageUrl(DEFAULT_IMAGE_URL)
            .type(DEFAULT_TYPE)
            .color(DEFAULT_COLOR)
            .cbm(DEFAULT_CBM)
            .price(DEFAULT_PRICE)
            .construction(DEFAULT_CONSTRUCTION)
            .masterPackingQty(DEFAULT_MASTER_PACKING_QTY)
            .masterNestCode(DEFAULT_MASTER_NEST_CODE)
            .innerQty(DEFAULT_INNER_QTY)
            .packSize(DEFAULT_PACK_SIZE)
            .nestCode(DEFAULT_NEST_CODE)
            .countryOfOrigin(DEFAULT_COUNTRY_OF_ORIGIN)
            .vendorName(DEFAULT_VENDOR_NAME)
            .numberOfSet(DEFAULT_NUMBER_OF_SET)
            .priceFOB(DEFAULT_PRICE_FOB)
            .qty40HC(DEFAULT_QTY_40_HC)
            .d57Qty(DEFAULT_D_57_QTY)
            .category(DEFAULT_CATEGORY)
            .labels(DEFAULT_LABELS)
            .planningNotes(DEFAULT_PLANNING_NOTES)
            .factTag(DEFAULT_FACT_TAG)
            .packagingLength(DEFAULT_PACKAGING_LENGTH)
            .packagingHeight(DEFAULT_PACKAGING_HEIGHT)
            .packagingWidth(DEFAULT_PACKAGING_WIDTH);
        return product;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createUpdatedEntity(EntityManager em) {
        Product product = new Product()
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .unit(UPDATED_UNIT)
            .description(UPDATED_DESCRIPTION)
            .weight(UPDATED_WEIGHT)
            .height(UPDATED_HEIGHT)
            .width(UPDATED_WIDTH)
            .length(UPDATED_LENGTH)
            .imageUrl(UPDATED_IMAGE_URL)
            .type(UPDATED_TYPE)
            .color(UPDATED_COLOR)
            .cbm(UPDATED_CBM)
            .price(UPDATED_PRICE)
            .construction(UPDATED_CONSTRUCTION)
            .masterPackingQty(UPDATED_MASTER_PACKING_QTY)
            .masterNestCode(UPDATED_MASTER_NEST_CODE)
            .innerQty(UPDATED_INNER_QTY)
            .packSize(UPDATED_PACK_SIZE)
            .nestCode(UPDATED_NEST_CODE)
            .countryOfOrigin(UPDATED_COUNTRY_OF_ORIGIN)
            .vendorName(UPDATED_VENDOR_NAME)
            .numberOfSet(UPDATED_NUMBER_OF_SET)
            .priceFOB(UPDATED_PRICE_FOB)
            .qty40HC(UPDATED_QTY_40_HC)
            .d57Qty(UPDATED_D_57_QTY)
            .category(UPDATED_CATEGORY)
            .labels(UPDATED_LABELS)
            .planningNotes(UPDATED_PLANNING_NOTES)
            .factTag(UPDATED_FACT_TAG)
            .packagingLength(UPDATED_PACKAGING_LENGTH)
            .packagingHeight(UPDATED_PACKAGING_HEIGHT)
            .packagingWidth(UPDATED_PACKAGING_WIDTH);
        return product;
    }

    @BeforeEach
    public void initTest() {
        product = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedProduct != null) {
            productRepository.delete(insertedProduct);
            insertedProduct = null;
        }
    }

    @Test
    @Transactional
    void createProduct() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);
        var returnedProductDTO = om.readValue(
            restProductMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductDTO.class
        );

        // Validate the Product in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProduct = productMapper.toEntity(returnedProductDTO);
        assertProductUpdatableFieldsEquals(returnedProduct, getPersistedProduct(returnedProduct));

        insertedProduct = returnedProduct;
    }

    @Test
    @Transactional
    void createProductWithExistingId() throws Exception {
        // Create the Product with an existing ID
        product.setId(1L);
        ProductDTO productDTO = productMapper.toDto(product);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProducts() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH.doubleValue())))
            .andExpect(jsonPath("$.[*].length").value(hasItem(DEFAULT_LENGTH.doubleValue())))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].cbm").value(hasItem(DEFAULT_CBM.doubleValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].construction").value(hasItem(DEFAULT_CONSTRUCTION)))
            .andExpect(jsonPath("$.[*].masterPackingQty").value(hasItem(DEFAULT_MASTER_PACKING_QTY)))
            .andExpect(jsonPath("$.[*].masterNestCode").value(hasItem(DEFAULT_MASTER_NEST_CODE)))
            .andExpect(jsonPath("$.[*].innerQty").value(hasItem(DEFAULT_INNER_QTY)))
            .andExpect(jsonPath("$.[*].packSize").value(hasItem(DEFAULT_PACK_SIZE)))
            .andExpect(jsonPath("$.[*].nestCode").value(hasItem(DEFAULT_NEST_CODE)))
            .andExpect(jsonPath("$.[*].countryOfOrigin").value(hasItem(DEFAULT_COUNTRY_OF_ORIGIN)))
            .andExpect(jsonPath("$.[*].vendorName").value(hasItem(DEFAULT_VENDOR_NAME)))
            .andExpect(jsonPath("$.[*].numberOfSet").value(hasItem(DEFAULT_NUMBER_OF_SET)))
            .andExpect(jsonPath("$.[*].priceFOB").value(hasItem(DEFAULT_PRICE_FOB.doubleValue())))
            .andExpect(jsonPath("$.[*].qty40HC").value(hasItem(DEFAULT_QTY_40_HC)))
            .andExpect(jsonPath("$.[*].d57Qty").value(hasItem(DEFAULT_D_57_QTY)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].labels").value(hasItem(DEFAULT_LABELS)))
            .andExpect(jsonPath("$.[*].planningNotes").value(hasItem(DEFAULT_PLANNING_NOTES)))
            .andExpect(jsonPath("$.[*].factTag").value(hasItem(DEFAULT_FACT_TAG)))
            .andExpect(jsonPath("$.[*].packagingLength").value(hasItem(DEFAULT_PACKAGING_LENGTH.doubleValue())))
            .andExpect(jsonPath("$.[*].packagingHeight").value(hasItem(DEFAULT_PACKAGING_HEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].packagingWidth").value(hasItem(DEFAULT_PACKAGING_WIDTH.doubleValue())));
    }

    @Test
    @Transactional
    void getProduct() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get the product
        restProductMockMvc
            .perform(get(ENTITY_API_URL_ID, product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(product.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT.doubleValue()))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT.doubleValue()))
            .andExpect(jsonPath("$.width").value(DEFAULT_WIDTH.doubleValue()))
            .andExpect(jsonPath("$.length").value(DEFAULT_LENGTH.doubleValue()))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR))
            .andExpect(jsonPath("$.cbm").value(DEFAULT_CBM.doubleValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.construction").value(DEFAULT_CONSTRUCTION))
            .andExpect(jsonPath("$.masterPackingQty").value(DEFAULT_MASTER_PACKING_QTY))
            .andExpect(jsonPath("$.masterNestCode").value(DEFAULT_MASTER_NEST_CODE))
            .andExpect(jsonPath("$.innerQty").value(DEFAULT_INNER_QTY))
            .andExpect(jsonPath("$.packSize").value(DEFAULT_PACK_SIZE))
            .andExpect(jsonPath("$.nestCode").value(DEFAULT_NEST_CODE))
            .andExpect(jsonPath("$.countryOfOrigin").value(DEFAULT_COUNTRY_OF_ORIGIN))
            .andExpect(jsonPath("$.vendorName").value(DEFAULT_VENDOR_NAME))
            .andExpect(jsonPath("$.numberOfSet").value(DEFAULT_NUMBER_OF_SET))
            .andExpect(jsonPath("$.priceFOB").value(DEFAULT_PRICE_FOB.doubleValue()))
            .andExpect(jsonPath("$.qty40HC").value(DEFAULT_QTY_40_HC))
            .andExpect(jsonPath("$.d57Qty").value(DEFAULT_D_57_QTY))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY))
            .andExpect(jsonPath("$.labels").value(DEFAULT_LABELS))
            .andExpect(jsonPath("$.planningNotes").value(DEFAULT_PLANNING_NOTES))
            .andExpect(jsonPath("$.factTag").value(DEFAULT_FACT_TAG))
            .andExpect(jsonPath("$.packagingLength").value(DEFAULT_PACKAGING_LENGTH.doubleValue()))
            .andExpect(jsonPath("$.packagingHeight").value(DEFAULT_PACKAGING_HEIGHT.doubleValue()))
            .andExpect(jsonPath("$.packagingWidth").value(DEFAULT_PACKAGING_WIDTH.doubleValue()));
    }

    @Test
    @Transactional
    void getProductsByIdFiltering() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        Long id = product.getId();

        defaultProductFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where name equals to
        defaultProductFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where name in
        defaultProductFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where name is not null
        defaultProductFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where name contains
        defaultProductFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where name does not contain
        defaultProductFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where code equals to
        defaultProductFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllProductsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where code in
        defaultProductFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllProductsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where code is not null
        defaultProductFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where code contains
        defaultProductFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllProductsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where code does not contain
        defaultProductFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllProductsByUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where unit equals to
        defaultProductFiltering("unit.equals=" + DEFAULT_UNIT, "unit.equals=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllProductsByUnitIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where unit in
        defaultProductFiltering("unit.in=" + DEFAULT_UNIT + "," + UPDATED_UNIT, "unit.in=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllProductsByUnitIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where unit is not null
        defaultProductFiltering("unit.specified=true", "unit.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByUnitContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where unit contains
        defaultProductFiltering("unit.contains=" + DEFAULT_UNIT, "unit.contains=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllProductsByUnitNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where unit does not contain
        defaultProductFiltering("unit.doesNotContain=" + UPDATED_UNIT, "unit.doesNotContain=" + DEFAULT_UNIT);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where description equals to
        defaultProductFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where description in
        defaultProductFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where description is not null
        defaultProductFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where description contains
        defaultProductFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where description does not contain
        defaultProductFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByWeightIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where weight equals to
        defaultProductFiltering("weight.equals=" + DEFAULT_WEIGHT, "weight.equals=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllProductsByWeightIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where weight in
        defaultProductFiltering("weight.in=" + DEFAULT_WEIGHT + "," + UPDATED_WEIGHT, "weight.in=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllProductsByWeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where weight is not null
        defaultProductFiltering("weight.specified=true", "weight.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByWeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where weight is greater than or equal to
        defaultProductFiltering("weight.greaterThanOrEqual=" + DEFAULT_WEIGHT, "weight.greaterThanOrEqual=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllProductsByWeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where weight is less than or equal to
        defaultProductFiltering("weight.lessThanOrEqual=" + DEFAULT_WEIGHT, "weight.lessThanOrEqual=" + SMALLER_WEIGHT);
    }

    @Test
    @Transactional
    void getAllProductsByWeightIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where weight is less than
        defaultProductFiltering("weight.lessThan=" + UPDATED_WEIGHT, "weight.lessThan=" + DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    void getAllProductsByWeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where weight is greater than
        defaultProductFiltering("weight.greaterThan=" + SMALLER_WEIGHT, "weight.greaterThan=" + DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    void getAllProductsByHeightIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where height equals to
        defaultProductFiltering("height.equals=" + DEFAULT_HEIGHT, "height.equals=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllProductsByHeightIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where height in
        defaultProductFiltering("height.in=" + DEFAULT_HEIGHT + "," + UPDATED_HEIGHT, "height.in=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllProductsByHeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where height is not null
        defaultProductFiltering("height.specified=true", "height.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByHeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where height is greater than or equal to
        defaultProductFiltering("height.greaterThanOrEqual=" + DEFAULT_HEIGHT, "height.greaterThanOrEqual=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllProductsByHeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where height is less than or equal to
        defaultProductFiltering("height.lessThanOrEqual=" + DEFAULT_HEIGHT, "height.lessThanOrEqual=" + SMALLER_HEIGHT);
    }

    @Test
    @Transactional
    void getAllProductsByHeightIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where height is less than
        defaultProductFiltering("height.lessThan=" + UPDATED_HEIGHT, "height.lessThan=" + DEFAULT_HEIGHT);
    }

    @Test
    @Transactional
    void getAllProductsByHeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where height is greater than
        defaultProductFiltering("height.greaterThan=" + SMALLER_HEIGHT, "height.greaterThan=" + DEFAULT_HEIGHT);
    }

    @Test
    @Transactional
    void getAllProductsByWidthIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where width equals to
        defaultProductFiltering("width.equals=" + DEFAULT_WIDTH, "width.equals=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllProductsByWidthIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where width in
        defaultProductFiltering("width.in=" + DEFAULT_WIDTH + "," + UPDATED_WIDTH, "width.in=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllProductsByWidthIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where width is not null
        defaultProductFiltering("width.specified=true", "width.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByWidthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where width is greater than or equal to
        defaultProductFiltering("width.greaterThanOrEqual=" + DEFAULT_WIDTH, "width.greaterThanOrEqual=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllProductsByWidthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where width is less than or equal to
        defaultProductFiltering("width.lessThanOrEqual=" + DEFAULT_WIDTH, "width.lessThanOrEqual=" + SMALLER_WIDTH);
    }

    @Test
    @Transactional
    void getAllProductsByWidthIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where width is less than
        defaultProductFiltering("width.lessThan=" + UPDATED_WIDTH, "width.lessThan=" + DEFAULT_WIDTH);
    }

    @Test
    @Transactional
    void getAllProductsByWidthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where width is greater than
        defaultProductFiltering("width.greaterThan=" + SMALLER_WIDTH, "width.greaterThan=" + DEFAULT_WIDTH);
    }

    @Test
    @Transactional
    void getAllProductsByLengthIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where length equals to
        defaultProductFiltering("length.equals=" + DEFAULT_LENGTH, "length.equals=" + UPDATED_LENGTH);
    }

    @Test
    @Transactional
    void getAllProductsByLengthIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where length in
        defaultProductFiltering("length.in=" + DEFAULT_LENGTH + "," + UPDATED_LENGTH, "length.in=" + UPDATED_LENGTH);
    }

    @Test
    @Transactional
    void getAllProductsByLengthIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where length is not null
        defaultProductFiltering("length.specified=true", "length.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByLengthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where length is greater than or equal to
        defaultProductFiltering("length.greaterThanOrEqual=" + DEFAULT_LENGTH, "length.greaterThanOrEqual=" + UPDATED_LENGTH);
    }

    @Test
    @Transactional
    void getAllProductsByLengthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where length is less than or equal to
        defaultProductFiltering("length.lessThanOrEqual=" + DEFAULT_LENGTH, "length.lessThanOrEqual=" + SMALLER_LENGTH);
    }

    @Test
    @Transactional
    void getAllProductsByLengthIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where length is less than
        defaultProductFiltering("length.lessThan=" + UPDATED_LENGTH, "length.lessThan=" + DEFAULT_LENGTH);
    }

    @Test
    @Transactional
    void getAllProductsByLengthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where length is greater than
        defaultProductFiltering("length.greaterThan=" + SMALLER_LENGTH, "length.greaterThan=" + DEFAULT_LENGTH);
    }

    @Test
    @Transactional
    void getAllProductsByImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where imageUrl equals to
        defaultProductFiltering("imageUrl.equals=" + DEFAULT_IMAGE_URL, "imageUrl.equals=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllProductsByImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where imageUrl in
        defaultProductFiltering("imageUrl.in=" + DEFAULT_IMAGE_URL + "," + UPDATED_IMAGE_URL, "imageUrl.in=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllProductsByImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where imageUrl is not null
        defaultProductFiltering("imageUrl.specified=true", "imageUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByImageUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where imageUrl contains
        defaultProductFiltering("imageUrl.contains=" + DEFAULT_IMAGE_URL, "imageUrl.contains=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllProductsByImageUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where imageUrl does not contain
        defaultProductFiltering("imageUrl.doesNotContain=" + UPDATED_IMAGE_URL, "imageUrl.doesNotContain=" + DEFAULT_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllProductsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where type equals to
        defaultProductFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllProductsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where type in
        defaultProductFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllProductsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where type is not null
        defaultProductFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByTypeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where type is greater than or equal to
        defaultProductFiltering("type.greaterThanOrEqual=" + DEFAULT_TYPE, "type.greaterThanOrEqual=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllProductsByTypeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where type is less than or equal to
        defaultProductFiltering("type.lessThanOrEqual=" + DEFAULT_TYPE, "type.lessThanOrEqual=" + SMALLER_TYPE);
    }

    @Test
    @Transactional
    void getAllProductsByTypeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where type is less than
        defaultProductFiltering("type.lessThan=" + UPDATED_TYPE, "type.lessThan=" + DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void getAllProductsByTypeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where type is greater than
        defaultProductFiltering("type.greaterThan=" + SMALLER_TYPE, "type.greaterThan=" + DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void getAllProductsByColorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where color equals to
        defaultProductFiltering("color.equals=" + DEFAULT_COLOR, "color.equals=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllProductsByColorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where color in
        defaultProductFiltering("color.in=" + DEFAULT_COLOR + "," + UPDATED_COLOR, "color.in=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllProductsByColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where color is not null
        defaultProductFiltering("color.specified=true", "color.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByColorContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where color contains
        defaultProductFiltering("color.contains=" + DEFAULT_COLOR, "color.contains=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllProductsByColorNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where color does not contain
        defaultProductFiltering("color.doesNotContain=" + UPDATED_COLOR, "color.doesNotContain=" + DEFAULT_COLOR);
    }

    @Test
    @Transactional
    void getAllProductsByCbmIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where cbm equals to
        defaultProductFiltering("cbm.equals=" + DEFAULT_CBM, "cbm.equals=" + UPDATED_CBM);
    }

    @Test
    @Transactional
    void getAllProductsByCbmIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where cbm in
        defaultProductFiltering("cbm.in=" + DEFAULT_CBM + "," + UPDATED_CBM, "cbm.in=" + UPDATED_CBM);
    }

    @Test
    @Transactional
    void getAllProductsByCbmIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where cbm is not null
        defaultProductFiltering("cbm.specified=true", "cbm.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByCbmIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where cbm is greater than or equal to
        defaultProductFiltering("cbm.greaterThanOrEqual=" + DEFAULT_CBM, "cbm.greaterThanOrEqual=" + UPDATED_CBM);
    }

    @Test
    @Transactional
    void getAllProductsByCbmIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where cbm is less than or equal to
        defaultProductFiltering("cbm.lessThanOrEqual=" + DEFAULT_CBM, "cbm.lessThanOrEqual=" + SMALLER_CBM);
    }

    @Test
    @Transactional
    void getAllProductsByCbmIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where cbm is less than
        defaultProductFiltering("cbm.lessThan=" + UPDATED_CBM, "cbm.lessThan=" + DEFAULT_CBM);
    }

    @Test
    @Transactional
    void getAllProductsByCbmIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where cbm is greater than
        defaultProductFiltering("cbm.greaterThan=" + SMALLER_CBM, "cbm.greaterThan=" + DEFAULT_CBM);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where price equals to
        defaultProductFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where price in
        defaultProductFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where price is not null
        defaultProductFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where price is greater than or equal to
        defaultProductFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where price is less than or equal to
        defaultProductFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where price is less than
        defaultProductFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where price is greater than
        defaultProductFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByConstructionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where construction equals to
        defaultProductFiltering("construction.equals=" + DEFAULT_CONSTRUCTION, "construction.equals=" + UPDATED_CONSTRUCTION);
    }

    @Test
    @Transactional
    void getAllProductsByConstructionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where construction in
        defaultProductFiltering(
            "construction.in=" + DEFAULT_CONSTRUCTION + "," + UPDATED_CONSTRUCTION,
            "construction.in=" + UPDATED_CONSTRUCTION
        );
    }

    @Test
    @Transactional
    void getAllProductsByConstructionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where construction is not null
        defaultProductFiltering("construction.specified=true", "construction.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByConstructionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where construction is greater than or equal to
        defaultProductFiltering(
            "construction.greaterThanOrEqual=" + DEFAULT_CONSTRUCTION,
            "construction.greaterThanOrEqual=" + UPDATED_CONSTRUCTION
        );
    }

    @Test
    @Transactional
    void getAllProductsByConstructionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where construction is less than or equal to
        defaultProductFiltering(
            "construction.lessThanOrEqual=" + DEFAULT_CONSTRUCTION,
            "construction.lessThanOrEqual=" + SMALLER_CONSTRUCTION
        );
    }

    @Test
    @Transactional
    void getAllProductsByConstructionIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where construction is less than
        defaultProductFiltering("construction.lessThan=" + UPDATED_CONSTRUCTION, "construction.lessThan=" + DEFAULT_CONSTRUCTION);
    }

    @Test
    @Transactional
    void getAllProductsByConstructionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where construction is greater than
        defaultProductFiltering("construction.greaterThan=" + SMALLER_CONSTRUCTION, "construction.greaterThan=" + DEFAULT_CONSTRUCTION);
    }

    @Test
    @Transactional
    void getAllProductsByMasterPackingQtyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where masterPackingQty equals to
        defaultProductFiltering(
            "masterPackingQty.equals=" + DEFAULT_MASTER_PACKING_QTY,
            "masterPackingQty.equals=" + UPDATED_MASTER_PACKING_QTY
        );
    }

    @Test
    @Transactional
    void getAllProductsByMasterPackingQtyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where masterPackingQty in
        defaultProductFiltering(
            "masterPackingQty.in=" + DEFAULT_MASTER_PACKING_QTY + "," + UPDATED_MASTER_PACKING_QTY,
            "masterPackingQty.in=" + UPDATED_MASTER_PACKING_QTY
        );
    }

    @Test
    @Transactional
    void getAllProductsByMasterPackingQtyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where masterPackingQty is not null
        defaultProductFiltering("masterPackingQty.specified=true", "masterPackingQty.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByMasterPackingQtyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where masterPackingQty is greater than or equal to
        defaultProductFiltering(
            "masterPackingQty.greaterThanOrEqual=" + DEFAULT_MASTER_PACKING_QTY,
            "masterPackingQty.greaterThanOrEqual=" + UPDATED_MASTER_PACKING_QTY
        );
    }

    @Test
    @Transactional
    void getAllProductsByMasterPackingQtyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where masterPackingQty is less than or equal to
        defaultProductFiltering(
            "masterPackingQty.lessThanOrEqual=" + DEFAULT_MASTER_PACKING_QTY,
            "masterPackingQty.lessThanOrEqual=" + SMALLER_MASTER_PACKING_QTY
        );
    }

    @Test
    @Transactional
    void getAllProductsByMasterPackingQtyIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where masterPackingQty is less than
        defaultProductFiltering(
            "masterPackingQty.lessThan=" + UPDATED_MASTER_PACKING_QTY,
            "masterPackingQty.lessThan=" + DEFAULT_MASTER_PACKING_QTY
        );
    }

    @Test
    @Transactional
    void getAllProductsByMasterPackingQtyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where masterPackingQty is greater than
        defaultProductFiltering(
            "masterPackingQty.greaterThan=" + SMALLER_MASTER_PACKING_QTY,
            "masterPackingQty.greaterThan=" + DEFAULT_MASTER_PACKING_QTY
        );
    }

    @Test
    @Transactional
    void getAllProductsByMasterNestCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where masterNestCode equals to
        defaultProductFiltering("masterNestCode.equals=" + DEFAULT_MASTER_NEST_CODE, "masterNestCode.equals=" + UPDATED_MASTER_NEST_CODE);
    }

    @Test
    @Transactional
    void getAllProductsByMasterNestCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where masterNestCode in
        defaultProductFiltering(
            "masterNestCode.in=" + DEFAULT_MASTER_NEST_CODE + "," + UPDATED_MASTER_NEST_CODE,
            "masterNestCode.in=" + UPDATED_MASTER_NEST_CODE
        );
    }

    @Test
    @Transactional
    void getAllProductsByMasterNestCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where masterNestCode is not null
        defaultProductFiltering("masterNestCode.specified=true", "masterNestCode.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByMasterNestCodeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where masterNestCode is greater than or equal to
        defaultProductFiltering(
            "masterNestCode.greaterThanOrEqual=" + DEFAULT_MASTER_NEST_CODE,
            "masterNestCode.greaterThanOrEqual=" + UPDATED_MASTER_NEST_CODE
        );
    }

    @Test
    @Transactional
    void getAllProductsByMasterNestCodeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where masterNestCode is less than or equal to
        defaultProductFiltering(
            "masterNestCode.lessThanOrEqual=" + DEFAULT_MASTER_NEST_CODE,
            "masterNestCode.lessThanOrEqual=" + SMALLER_MASTER_NEST_CODE
        );
    }

    @Test
    @Transactional
    void getAllProductsByMasterNestCodeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where masterNestCode is less than
        defaultProductFiltering(
            "masterNestCode.lessThan=" + UPDATED_MASTER_NEST_CODE,
            "masterNestCode.lessThan=" + DEFAULT_MASTER_NEST_CODE
        );
    }

    @Test
    @Transactional
    void getAllProductsByMasterNestCodeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where masterNestCode is greater than
        defaultProductFiltering(
            "masterNestCode.greaterThan=" + SMALLER_MASTER_NEST_CODE,
            "masterNestCode.greaterThan=" + DEFAULT_MASTER_NEST_CODE
        );
    }

    @Test
    @Transactional
    void getAllProductsByInnerQtyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where innerQty equals to
        defaultProductFiltering("innerQty.equals=" + DEFAULT_INNER_QTY, "innerQty.equals=" + UPDATED_INNER_QTY);
    }

    @Test
    @Transactional
    void getAllProductsByInnerQtyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where innerQty in
        defaultProductFiltering("innerQty.in=" + DEFAULT_INNER_QTY + "," + UPDATED_INNER_QTY, "innerQty.in=" + UPDATED_INNER_QTY);
    }

    @Test
    @Transactional
    void getAllProductsByInnerQtyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where innerQty is not null
        defaultProductFiltering("innerQty.specified=true", "innerQty.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByInnerQtyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where innerQty is greater than or equal to
        defaultProductFiltering("innerQty.greaterThanOrEqual=" + DEFAULT_INNER_QTY, "innerQty.greaterThanOrEqual=" + UPDATED_INNER_QTY);
    }

    @Test
    @Transactional
    void getAllProductsByInnerQtyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where innerQty is less than or equal to
        defaultProductFiltering("innerQty.lessThanOrEqual=" + DEFAULT_INNER_QTY, "innerQty.lessThanOrEqual=" + SMALLER_INNER_QTY);
    }

    @Test
    @Transactional
    void getAllProductsByInnerQtyIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where innerQty is less than
        defaultProductFiltering("innerQty.lessThan=" + UPDATED_INNER_QTY, "innerQty.lessThan=" + DEFAULT_INNER_QTY);
    }

    @Test
    @Transactional
    void getAllProductsByInnerQtyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where innerQty is greater than
        defaultProductFiltering("innerQty.greaterThan=" + SMALLER_INNER_QTY, "innerQty.greaterThan=" + DEFAULT_INNER_QTY);
    }

    @Test
    @Transactional
    void getAllProductsByPackSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where packSize equals to
        defaultProductFiltering("packSize.equals=" + DEFAULT_PACK_SIZE, "packSize.equals=" + UPDATED_PACK_SIZE);
    }

    @Test
    @Transactional
    void getAllProductsByPackSizeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where packSize in
        defaultProductFiltering("packSize.in=" + DEFAULT_PACK_SIZE + "," + UPDATED_PACK_SIZE, "packSize.in=" + UPDATED_PACK_SIZE);
    }

    @Test
    @Transactional
    void getAllProductsByPackSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where packSize is not null
        defaultProductFiltering("packSize.specified=true", "packSize.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByPackSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where packSize is greater than or equal to
        defaultProductFiltering("packSize.greaterThanOrEqual=" + DEFAULT_PACK_SIZE, "packSize.greaterThanOrEqual=" + UPDATED_PACK_SIZE);
    }

    @Test
    @Transactional
    void getAllProductsByPackSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where packSize is less than or equal to
        defaultProductFiltering("packSize.lessThanOrEqual=" + DEFAULT_PACK_SIZE, "packSize.lessThanOrEqual=" + SMALLER_PACK_SIZE);
    }

    @Test
    @Transactional
    void getAllProductsByPackSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where packSize is less than
        defaultProductFiltering("packSize.lessThan=" + UPDATED_PACK_SIZE, "packSize.lessThan=" + DEFAULT_PACK_SIZE);
    }

    @Test
    @Transactional
    void getAllProductsByPackSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where packSize is greater than
        defaultProductFiltering("packSize.greaterThan=" + SMALLER_PACK_SIZE, "packSize.greaterThan=" + DEFAULT_PACK_SIZE);
    }

    @Test
    @Transactional
    void getAllProductsByNestCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where nestCode equals to
        defaultProductFiltering("nestCode.equals=" + DEFAULT_NEST_CODE, "nestCode.equals=" + UPDATED_NEST_CODE);
    }

    @Test
    @Transactional
    void getAllProductsByNestCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where nestCode in
        defaultProductFiltering("nestCode.in=" + DEFAULT_NEST_CODE + "," + UPDATED_NEST_CODE, "nestCode.in=" + UPDATED_NEST_CODE);
    }

    @Test
    @Transactional
    void getAllProductsByNestCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where nestCode is not null
        defaultProductFiltering("nestCode.specified=true", "nestCode.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByNestCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where nestCode contains
        defaultProductFiltering("nestCode.contains=" + DEFAULT_NEST_CODE, "nestCode.contains=" + UPDATED_NEST_CODE);
    }

    @Test
    @Transactional
    void getAllProductsByNestCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where nestCode does not contain
        defaultProductFiltering("nestCode.doesNotContain=" + UPDATED_NEST_CODE, "nestCode.doesNotContain=" + DEFAULT_NEST_CODE);
    }

    @Test
    @Transactional
    void getAllProductsByCountryOfOriginIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where countryOfOrigin equals to
        defaultProductFiltering(
            "countryOfOrigin.equals=" + DEFAULT_COUNTRY_OF_ORIGIN,
            "countryOfOrigin.equals=" + UPDATED_COUNTRY_OF_ORIGIN
        );
    }

    @Test
    @Transactional
    void getAllProductsByCountryOfOriginIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where countryOfOrigin in
        defaultProductFiltering(
            "countryOfOrigin.in=" + DEFAULT_COUNTRY_OF_ORIGIN + "," + UPDATED_COUNTRY_OF_ORIGIN,
            "countryOfOrigin.in=" + UPDATED_COUNTRY_OF_ORIGIN
        );
    }

    @Test
    @Transactional
    void getAllProductsByCountryOfOriginIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where countryOfOrigin is not null
        defaultProductFiltering("countryOfOrigin.specified=true", "countryOfOrigin.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByCountryOfOriginContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where countryOfOrigin contains
        defaultProductFiltering(
            "countryOfOrigin.contains=" + DEFAULT_COUNTRY_OF_ORIGIN,
            "countryOfOrigin.contains=" + UPDATED_COUNTRY_OF_ORIGIN
        );
    }

    @Test
    @Transactional
    void getAllProductsByCountryOfOriginNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where countryOfOrigin does not contain
        defaultProductFiltering(
            "countryOfOrigin.doesNotContain=" + UPDATED_COUNTRY_OF_ORIGIN,
            "countryOfOrigin.doesNotContain=" + DEFAULT_COUNTRY_OF_ORIGIN
        );
    }

    @Test
    @Transactional
    void getAllProductsByVendorNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where vendorName equals to
        defaultProductFiltering("vendorName.equals=" + DEFAULT_VENDOR_NAME, "vendorName.equals=" + UPDATED_VENDOR_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByVendorNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where vendorName in
        defaultProductFiltering("vendorName.in=" + DEFAULT_VENDOR_NAME + "," + UPDATED_VENDOR_NAME, "vendorName.in=" + UPDATED_VENDOR_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByVendorNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where vendorName is not null
        defaultProductFiltering("vendorName.specified=true", "vendorName.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByVendorNameContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where vendorName contains
        defaultProductFiltering("vendorName.contains=" + DEFAULT_VENDOR_NAME, "vendorName.contains=" + UPDATED_VENDOR_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByVendorNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where vendorName does not contain
        defaultProductFiltering("vendorName.doesNotContain=" + UPDATED_VENDOR_NAME, "vendorName.doesNotContain=" + DEFAULT_VENDOR_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByNumberOfSetIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where numberOfSet equals to
        defaultProductFiltering("numberOfSet.equals=" + DEFAULT_NUMBER_OF_SET, "numberOfSet.equals=" + UPDATED_NUMBER_OF_SET);
    }

    @Test
    @Transactional
    void getAllProductsByNumberOfSetIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where numberOfSet in
        defaultProductFiltering(
            "numberOfSet.in=" + DEFAULT_NUMBER_OF_SET + "," + UPDATED_NUMBER_OF_SET,
            "numberOfSet.in=" + UPDATED_NUMBER_OF_SET
        );
    }

    @Test
    @Transactional
    void getAllProductsByNumberOfSetIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where numberOfSet is not null
        defaultProductFiltering("numberOfSet.specified=true", "numberOfSet.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByNumberOfSetIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where numberOfSet is greater than or equal to
        defaultProductFiltering(
            "numberOfSet.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_SET,
            "numberOfSet.greaterThanOrEqual=" + UPDATED_NUMBER_OF_SET
        );
    }

    @Test
    @Transactional
    void getAllProductsByNumberOfSetIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where numberOfSet is less than or equal to
        defaultProductFiltering(
            "numberOfSet.lessThanOrEqual=" + DEFAULT_NUMBER_OF_SET,
            "numberOfSet.lessThanOrEqual=" + SMALLER_NUMBER_OF_SET
        );
    }

    @Test
    @Transactional
    void getAllProductsByNumberOfSetIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where numberOfSet is less than
        defaultProductFiltering("numberOfSet.lessThan=" + UPDATED_NUMBER_OF_SET, "numberOfSet.lessThan=" + DEFAULT_NUMBER_OF_SET);
    }

    @Test
    @Transactional
    void getAllProductsByNumberOfSetIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where numberOfSet is greater than
        defaultProductFiltering("numberOfSet.greaterThan=" + SMALLER_NUMBER_OF_SET, "numberOfSet.greaterThan=" + DEFAULT_NUMBER_OF_SET);
    }

    @Test
    @Transactional
    void getAllProductsByPriceFOBIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where priceFOB equals to
        defaultProductFiltering("priceFOB.equals=" + DEFAULT_PRICE_FOB, "priceFOB.equals=" + UPDATED_PRICE_FOB);
    }

    @Test
    @Transactional
    void getAllProductsByPriceFOBIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where priceFOB in
        defaultProductFiltering("priceFOB.in=" + DEFAULT_PRICE_FOB + "," + UPDATED_PRICE_FOB, "priceFOB.in=" + UPDATED_PRICE_FOB);
    }

    @Test
    @Transactional
    void getAllProductsByPriceFOBIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where priceFOB is not null
        defaultProductFiltering("priceFOB.specified=true", "priceFOB.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByPriceFOBIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where priceFOB is greater than or equal to
        defaultProductFiltering("priceFOB.greaterThanOrEqual=" + DEFAULT_PRICE_FOB, "priceFOB.greaterThanOrEqual=" + UPDATED_PRICE_FOB);
    }

    @Test
    @Transactional
    void getAllProductsByPriceFOBIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where priceFOB is less than or equal to
        defaultProductFiltering("priceFOB.lessThanOrEqual=" + DEFAULT_PRICE_FOB, "priceFOB.lessThanOrEqual=" + SMALLER_PRICE_FOB);
    }

    @Test
    @Transactional
    void getAllProductsByPriceFOBIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where priceFOB is less than
        defaultProductFiltering("priceFOB.lessThan=" + UPDATED_PRICE_FOB, "priceFOB.lessThan=" + DEFAULT_PRICE_FOB);
    }

    @Test
    @Transactional
    void getAllProductsByPriceFOBIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where priceFOB is greater than
        defaultProductFiltering("priceFOB.greaterThan=" + SMALLER_PRICE_FOB, "priceFOB.greaterThan=" + DEFAULT_PRICE_FOB);
    }

    @Test
    @Transactional
    void getAllProductsByQty40HCIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where qty40HC equals to
        defaultProductFiltering("qty40HC.equals=" + DEFAULT_QTY_40_HC, "qty40HC.equals=" + UPDATED_QTY_40_HC);
    }

    @Test
    @Transactional
    void getAllProductsByQty40HCIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where qty40HC in
        defaultProductFiltering("qty40HC.in=" + DEFAULT_QTY_40_HC + "," + UPDATED_QTY_40_HC, "qty40HC.in=" + UPDATED_QTY_40_HC);
    }

    @Test
    @Transactional
    void getAllProductsByQty40HCIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where qty40HC is not null
        defaultProductFiltering("qty40HC.specified=true", "qty40HC.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByQty40HCIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where qty40HC is greater than or equal to
        defaultProductFiltering("qty40HC.greaterThanOrEqual=" + DEFAULT_QTY_40_HC, "qty40HC.greaterThanOrEqual=" + UPDATED_QTY_40_HC);
    }

    @Test
    @Transactional
    void getAllProductsByQty40HCIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where qty40HC is less than or equal to
        defaultProductFiltering("qty40HC.lessThanOrEqual=" + DEFAULT_QTY_40_HC, "qty40HC.lessThanOrEqual=" + SMALLER_QTY_40_HC);
    }

    @Test
    @Transactional
    void getAllProductsByQty40HCIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where qty40HC is less than
        defaultProductFiltering("qty40HC.lessThan=" + UPDATED_QTY_40_HC, "qty40HC.lessThan=" + DEFAULT_QTY_40_HC);
    }

    @Test
    @Transactional
    void getAllProductsByQty40HCIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where qty40HC is greater than
        defaultProductFiltering("qty40HC.greaterThan=" + SMALLER_QTY_40_HC, "qty40HC.greaterThan=" + DEFAULT_QTY_40_HC);
    }

    @Test
    @Transactional
    void getAllProductsByd57QtyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where d57Qty equals to
        defaultProductFiltering("d57Qty.equals=" + DEFAULT_D_57_QTY, "d57Qty.equals=" + UPDATED_D_57_QTY);
    }

    @Test
    @Transactional
    void getAllProductsByd57QtyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where d57Qty in
        defaultProductFiltering("d57Qty.in=" + DEFAULT_D_57_QTY + "," + UPDATED_D_57_QTY, "d57Qty.in=" + UPDATED_D_57_QTY);
    }

    @Test
    @Transactional
    void getAllProductsByd57QtyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where d57Qty is not null
        defaultProductFiltering("d57Qty.specified=true", "d57Qty.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByd57QtyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where d57Qty is greater than or equal to
        defaultProductFiltering("d57Qty.greaterThanOrEqual=" + DEFAULT_D_57_QTY, "d57Qty.greaterThanOrEqual=" + UPDATED_D_57_QTY);
    }

    @Test
    @Transactional
    void getAllProductsByd57QtyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where d57Qty is less than or equal to
        defaultProductFiltering("d57Qty.lessThanOrEqual=" + DEFAULT_D_57_QTY, "d57Qty.lessThanOrEqual=" + SMALLER_D_57_QTY);
    }

    @Test
    @Transactional
    void getAllProductsByd57QtyIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where d57Qty is less than
        defaultProductFiltering("d57Qty.lessThan=" + UPDATED_D_57_QTY, "d57Qty.lessThan=" + DEFAULT_D_57_QTY);
    }

    @Test
    @Transactional
    void getAllProductsByd57QtyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where d57Qty is greater than
        defaultProductFiltering("d57Qty.greaterThan=" + SMALLER_D_57_QTY, "d57Qty.greaterThan=" + DEFAULT_D_57_QTY);
    }

    @Test
    @Transactional
    void getAllProductsByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where category equals to
        defaultProductFiltering("category.equals=" + DEFAULT_CATEGORY, "category.equals=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllProductsByCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where category in
        defaultProductFiltering("category.in=" + DEFAULT_CATEGORY + "," + UPDATED_CATEGORY, "category.in=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllProductsByCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where category is not null
        defaultProductFiltering("category.specified=true", "category.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByCategoryContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where category contains
        defaultProductFiltering("category.contains=" + DEFAULT_CATEGORY, "category.contains=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllProductsByCategoryNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where category does not contain
        defaultProductFiltering("category.doesNotContain=" + UPDATED_CATEGORY, "category.doesNotContain=" + DEFAULT_CATEGORY);
    }

    @Test
    @Transactional
    void getAllProductsByLabelsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where labels equals to
        defaultProductFiltering("labels.equals=" + DEFAULT_LABELS, "labels.equals=" + UPDATED_LABELS);
    }

    @Test
    @Transactional
    void getAllProductsByLabelsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where labels in
        defaultProductFiltering("labels.in=" + DEFAULT_LABELS + "," + UPDATED_LABELS, "labels.in=" + UPDATED_LABELS);
    }

    @Test
    @Transactional
    void getAllProductsByLabelsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where labels is not null
        defaultProductFiltering("labels.specified=true", "labels.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByLabelsContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where labels contains
        defaultProductFiltering("labels.contains=" + DEFAULT_LABELS, "labels.contains=" + UPDATED_LABELS);
    }

    @Test
    @Transactional
    void getAllProductsByLabelsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where labels does not contain
        defaultProductFiltering("labels.doesNotContain=" + UPDATED_LABELS, "labels.doesNotContain=" + DEFAULT_LABELS);
    }

    @Test
    @Transactional
    void getAllProductsByPlanningNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where planningNotes equals to
        defaultProductFiltering("planningNotes.equals=" + DEFAULT_PLANNING_NOTES, "planningNotes.equals=" + UPDATED_PLANNING_NOTES);
    }

    @Test
    @Transactional
    void getAllProductsByPlanningNotesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where planningNotes in
        defaultProductFiltering(
            "planningNotes.in=" + DEFAULT_PLANNING_NOTES + "," + UPDATED_PLANNING_NOTES,
            "planningNotes.in=" + UPDATED_PLANNING_NOTES
        );
    }

    @Test
    @Transactional
    void getAllProductsByPlanningNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where planningNotes is not null
        defaultProductFiltering("planningNotes.specified=true", "planningNotes.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByPlanningNotesContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where planningNotes contains
        defaultProductFiltering("planningNotes.contains=" + DEFAULT_PLANNING_NOTES, "planningNotes.contains=" + UPDATED_PLANNING_NOTES);
    }

    @Test
    @Transactional
    void getAllProductsByPlanningNotesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where planningNotes does not contain
        defaultProductFiltering(
            "planningNotes.doesNotContain=" + UPDATED_PLANNING_NOTES,
            "planningNotes.doesNotContain=" + DEFAULT_PLANNING_NOTES
        );
    }

    @Test
    @Transactional
    void getAllProductsByFactTagIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where factTag equals to
        defaultProductFiltering("factTag.equals=" + DEFAULT_FACT_TAG, "factTag.equals=" + UPDATED_FACT_TAG);
    }

    @Test
    @Transactional
    void getAllProductsByFactTagIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where factTag in
        defaultProductFiltering("factTag.in=" + DEFAULT_FACT_TAG + "," + UPDATED_FACT_TAG, "factTag.in=" + UPDATED_FACT_TAG);
    }

    @Test
    @Transactional
    void getAllProductsByFactTagIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where factTag is not null
        defaultProductFiltering("factTag.specified=true", "factTag.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByFactTagContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where factTag contains
        defaultProductFiltering("factTag.contains=" + DEFAULT_FACT_TAG, "factTag.contains=" + UPDATED_FACT_TAG);
    }

    @Test
    @Transactional
    void getAllProductsByFactTagNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where factTag does not contain
        defaultProductFiltering("factTag.doesNotContain=" + UPDATED_FACT_TAG, "factTag.doesNotContain=" + DEFAULT_FACT_TAG);
    }

    @Test
    @Transactional
    void getAllProductsByPackagingLengthIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where packagingLength equals to
        defaultProductFiltering("packagingLength.equals=" + DEFAULT_PACKAGING_LENGTH, "packagingLength.equals=" + UPDATED_PACKAGING_LENGTH);
    }

    @Test
    @Transactional
    void getAllProductsByPackagingLengthIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where packagingLength in
        defaultProductFiltering(
            "packagingLength.in=" + DEFAULT_PACKAGING_LENGTH + "," + UPDATED_PACKAGING_LENGTH,
            "packagingLength.in=" + UPDATED_PACKAGING_LENGTH
        );
    }

    @Test
    @Transactional
    void getAllProductsByPackagingLengthIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where packagingLength is not null
        defaultProductFiltering("packagingLength.specified=true", "packagingLength.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByPackagingLengthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where packagingLength is greater than or equal to
        defaultProductFiltering(
            "packagingLength.greaterThanOrEqual=" + DEFAULT_PACKAGING_LENGTH,
            "packagingLength.greaterThanOrEqual=" + UPDATED_PACKAGING_LENGTH
        );
    }

    @Test
    @Transactional
    void getAllProductsByPackagingLengthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where packagingLength is less than or equal to
        defaultProductFiltering(
            "packagingLength.lessThanOrEqual=" + DEFAULT_PACKAGING_LENGTH,
            "packagingLength.lessThanOrEqual=" + SMALLER_PACKAGING_LENGTH
        );
    }

    @Test
    @Transactional
    void getAllProductsByPackagingLengthIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where packagingLength is less than
        defaultProductFiltering(
            "packagingLength.lessThan=" + UPDATED_PACKAGING_LENGTH,
            "packagingLength.lessThan=" + DEFAULT_PACKAGING_LENGTH
        );
    }

    @Test
    @Transactional
    void getAllProductsByPackagingLengthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where packagingLength is greater than
        defaultProductFiltering(
            "packagingLength.greaterThan=" + SMALLER_PACKAGING_LENGTH,
            "packagingLength.greaterThan=" + DEFAULT_PACKAGING_LENGTH
        );
    }

    @Test
    @Transactional
    void getAllProductsByPackagingHeightIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where packagingHeight equals to
        defaultProductFiltering("packagingHeight.equals=" + DEFAULT_PACKAGING_HEIGHT, "packagingHeight.equals=" + UPDATED_PACKAGING_HEIGHT);
    }

    @Test
    @Transactional
    void getAllProductsByPackagingHeightIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where packagingHeight in
        defaultProductFiltering(
            "packagingHeight.in=" + DEFAULT_PACKAGING_HEIGHT + "," + UPDATED_PACKAGING_HEIGHT,
            "packagingHeight.in=" + UPDATED_PACKAGING_HEIGHT
        );
    }

    @Test
    @Transactional
    void getAllProductsByPackagingHeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where packagingHeight is not null
        defaultProductFiltering("packagingHeight.specified=true", "packagingHeight.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByPackagingHeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where packagingHeight is greater than or equal to
        defaultProductFiltering(
            "packagingHeight.greaterThanOrEqual=" + DEFAULT_PACKAGING_HEIGHT,
            "packagingHeight.greaterThanOrEqual=" + UPDATED_PACKAGING_HEIGHT
        );
    }

    @Test
    @Transactional
    void getAllProductsByPackagingHeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where packagingHeight is less than or equal to
        defaultProductFiltering(
            "packagingHeight.lessThanOrEqual=" + DEFAULT_PACKAGING_HEIGHT,
            "packagingHeight.lessThanOrEqual=" + SMALLER_PACKAGING_HEIGHT
        );
    }

    @Test
    @Transactional
    void getAllProductsByPackagingHeightIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where packagingHeight is less than
        defaultProductFiltering(
            "packagingHeight.lessThan=" + UPDATED_PACKAGING_HEIGHT,
            "packagingHeight.lessThan=" + DEFAULT_PACKAGING_HEIGHT
        );
    }

    @Test
    @Transactional
    void getAllProductsByPackagingHeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where packagingHeight is greater than
        defaultProductFiltering(
            "packagingHeight.greaterThan=" + SMALLER_PACKAGING_HEIGHT,
            "packagingHeight.greaterThan=" + DEFAULT_PACKAGING_HEIGHT
        );
    }

    @Test
    @Transactional
    void getAllProductsByPackagingWidthIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where packagingWidth equals to
        defaultProductFiltering("packagingWidth.equals=" + DEFAULT_PACKAGING_WIDTH, "packagingWidth.equals=" + UPDATED_PACKAGING_WIDTH);
    }

    @Test
    @Transactional
    void getAllProductsByPackagingWidthIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where packagingWidth in
        defaultProductFiltering(
            "packagingWidth.in=" + DEFAULT_PACKAGING_WIDTH + "," + UPDATED_PACKAGING_WIDTH,
            "packagingWidth.in=" + UPDATED_PACKAGING_WIDTH
        );
    }

    @Test
    @Transactional
    void getAllProductsByPackagingWidthIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where packagingWidth is not null
        defaultProductFiltering("packagingWidth.specified=true", "packagingWidth.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByPackagingWidthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where packagingWidth is greater than or equal to
        defaultProductFiltering(
            "packagingWidth.greaterThanOrEqual=" + DEFAULT_PACKAGING_WIDTH,
            "packagingWidth.greaterThanOrEqual=" + UPDATED_PACKAGING_WIDTH
        );
    }

    @Test
    @Transactional
    void getAllProductsByPackagingWidthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where packagingWidth is less than or equal to
        defaultProductFiltering(
            "packagingWidth.lessThanOrEqual=" + DEFAULT_PACKAGING_WIDTH,
            "packagingWidth.lessThanOrEqual=" + SMALLER_PACKAGING_WIDTH
        );
    }

    @Test
    @Transactional
    void getAllProductsByPackagingWidthIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where packagingWidth is less than
        defaultProductFiltering("packagingWidth.lessThan=" + UPDATED_PACKAGING_WIDTH, "packagingWidth.lessThan=" + DEFAULT_PACKAGING_WIDTH);
    }

    @Test
    @Transactional
    void getAllProductsByPackagingWidthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where packagingWidth is greater than
        defaultProductFiltering(
            "packagingWidth.greaterThan=" + SMALLER_PACKAGING_WIDTH,
            "packagingWidth.greaterThan=" + DEFAULT_PACKAGING_WIDTH
        );
    }

    @Test
    @Transactional
    void getAllProductsBySetIdProductIsEqualToSomething() throws Exception {
        Product setIdProduct;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            setIdProduct = ProductResourceIT.createEntity(em);
        } else {
            setIdProduct = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(setIdProduct);
        em.flush();
        product.setSetIdProduct(setIdProduct);
        productRepository.saveAndFlush(product);
        Long setIdProductId = setIdProduct.getId();
        // Get all the productList where setIdProduct equals to setIdProductId
        defaultProductShouldBeFound("setIdProductId.equals=" + setIdProductId);

        // Get all the productList where setIdProduct equals to (setIdProductId + 1)
        defaultProductShouldNotBeFound("setIdProductId.equals=" + (setIdProductId + 1));
    }

    @Test
    @Transactional
    void getAllProductsByParentProductIsEqualToSomething() throws Exception {
        Product parentProduct;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            parentProduct = ProductResourceIT.createEntity(em);
        } else {
            parentProduct = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(parentProduct);
        em.flush();
        product.setParentProduct(parentProduct);
        productRepository.saveAndFlush(product);
        Long parentProductId = parentProduct.getId();
        // Get all the productList where parentProduct equals to parentProductId
        defaultProductShouldBeFound("parentProductId.equals=" + parentProductId);

        // Get all the productList where parentProduct equals to (parentProductId + 1)
        defaultProductShouldNotBeFound("parentProductId.equals=" + (parentProductId + 1));
    }

    @Test
    @Transactional
    void getAllProductsByMaterialIsEqualToSomething() throws Exception {
        Material material;
        if (TestUtil.findAll(em, Material.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            material = MaterialResourceIT.createEntity(em);
        } else {
            material = TestUtil.findAll(em, Material.class).get(0);
        }
        em.persist(material);
        em.flush();
        product.setMaterial(material);
        productRepository.saveAndFlush(product);
        Long materialId = material.getId();
        // Get all the productList where material equals to materialId
        defaultProductShouldBeFound("materialId.equals=" + materialId);

        // Get all the productList where material equals to (materialId + 1)
        defaultProductShouldNotBeFound("materialId.equals=" + (materialId + 1));
    }

    private void defaultProductFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductShouldBeFound(shouldBeFound);
        defaultProductShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductShouldBeFound(String filter) throws Exception {
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH.doubleValue())))
            .andExpect(jsonPath("$.[*].length").value(hasItem(DEFAULT_LENGTH.doubleValue())))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].cbm").value(hasItem(DEFAULT_CBM.doubleValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].construction").value(hasItem(DEFAULT_CONSTRUCTION)))
            .andExpect(jsonPath("$.[*].masterPackingQty").value(hasItem(DEFAULT_MASTER_PACKING_QTY)))
            .andExpect(jsonPath("$.[*].masterNestCode").value(hasItem(DEFAULT_MASTER_NEST_CODE)))
            .andExpect(jsonPath("$.[*].innerQty").value(hasItem(DEFAULT_INNER_QTY)))
            .andExpect(jsonPath("$.[*].packSize").value(hasItem(DEFAULT_PACK_SIZE)))
            .andExpect(jsonPath("$.[*].nestCode").value(hasItem(DEFAULT_NEST_CODE)))
            .andExpect(jsonPath("$.[*].countryOfOrigin").value(hasItem(DEFAULT_COUNTRY_OF_ORIGIN)))
            .andExpect(jsonPath("$.[*].vendorName").value(hasItem(DEFAULT_VENDOR_NAME)))
            .andExpect(jsonPath("$.[*].numberOfSet").value(hasItem(DEFAULT_NUMBER_OF_SET)))
            .andExpect(jsonPath("$.[*].priceFOB").value(hasItem(DEFAULT_PRICE_FOB.doubleValue())))
            .andExpect(jsonPath("$.[*].qty40HC").value(hasItem(DEFAULT_QTY_40_HC)))
            .andExpect(jsonPath("$.[*].d57Qty").value(hasItem(DEFAULT_D_57_QTY)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].labels").value(hasItem(DEFAULT_LABELS)))
            .andExpect(jsonPath("$.[*].planningNotes").value(hasItem(DEFAULT_PLANNING_NOTES)))
            .andExpect(jsonPath("$.[*].factTag").value(hasItem(DEFAULT_FACT_TAG)))
            .andExpect(jsonPath("$.[*].packagingLength").value(hasItem(DEFAULT_PACKAGING_LENGTH.doubleValue())))
            .andExpect(jsonPath("$.[*].packagingHeight").value(hasItem(DEFAULT_PACKAGING_HEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].packagingWidth").value(hasItem(DEFAULT_PACKAGING_WIDTH.doubleValue())));

        // Check, that the count call also returns 1
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductShouldNotBeFound(String filter) throws Exception {
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProduct() throws Exception {
        // Get the product
        restProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProduct() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the product
        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProduct are not directly saved in db
        em.detach(updatedProduct);
        updatedProduct
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .unit(UPDATED_UNIT)
            .description(UPDATED_DESCRIPTION)
            .weight(UPDATED_WEIGHT)
            .height(UPDATED_HEIGHT)
            .width(UPDATED_WIDTH)
            .length(UPDATED_LENGTH)
            .imageUrl(UPDATED_IMAGE_URL)
            .type(UPDATED_TYPE)
            .color(UPDATED_COLOR)
            .cbm(UPDATED_CBM)
            .price(UPDATED_PRICE)
            .construction(UPDATED_CONSTRUCTION)
            .masterPackingQty(UPDATED_MASTER_PACKING_QTY)
            .masterNestCode(UPDATED_MASTER_NEST_CODE)
            .innerQty(UPDATED_INNER_QTY)
            .packSize(UPDATED_PACK_SIZE)
            .nestCode(UPDATED_NEST_CODE)
            .countryOfOrigin(UPDATED_COUNTRY_OF_ORIGIN)
            .vendorName(UPDATED_VENDOR_NAME)
            .numberOfSet(UPDATED_NUMBER_OF_SET)
            .priceFOB(UPDATED_PRICE_FOB)
            .qty40HC(UPDATED_QTY_40_HC)
            .d57Qty(UPDATED_D_57_QTY)
            .category(UPDATED_CATEGORY)
            .labels(UPDATED_LABELS)
            .planningNotes(UPDATED_PLANNING_NOTES)
            .factTag(UPDATED_FACT_TAG)
            .packagingLength(UPDATED_PACKAGING_LENGTH)
            .packagingHeight(UPDATED_PACKAGING_HEIGHT)
            .packagingWidth(UPDATED_PACKAGING_WIDTH);
        ProductDTO productDTO = productMapper.toDto(updatedProduct);

        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductToMatchAllProperties(updatedProduct);
    }

    @Test
    @Transactional
    void putNonExistingProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductWithPatch() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .height(UPDATED_HEIGHT)
            .imageUrl(UPDATED_IMAGE_URL)
            .color(UPDATED_COLOR)
            .cbm(UPDATED_CBM)
            .price(UPDATED_PRICE)
            .construction(UPDATED_CONSTRUCTION)
            .packSize(UPDATED_PACK_SIZE)
            .nestCode(UPDATED_NEST_CODE)
            .countryOfOrigin(UPDATED_COUNTRY_OF_ORIGIN)
            .vendorName(UPDATED_VENDOR_NAME)
            .qty40HC(UPDATED_QTY_40_HC)
            .labels(UPDATED_LABELS)
            .packagingWidth(UPDATED_PACKAGING_WIDTH);

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedProduct, product), getPersistedProduct(product));
    }

    @Test
    @Transactional
    void fullUpdateProductWithPatch() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .unit(UPDATED_UNIT)
            .description(UPDATED_DESCRIPTION)
            .weight(UPDATED_WEIGHT)
            .height(UPDATED_HEIGHT)
            .width(UPDATED_WIDTH)
            .length(UPDATED_LENGTH)
            .imageUrl(UPDATED_IMAGE_URL)
            .type(UPDATED_TYPE)
            .color(UPDATED_COLOR)
            .cbm(UPDATED_CBM)
            .price(UPDATED_PRICE)
            .construction(UPDATED_CONSTRUCTION)
            .masterPackingQty(UPDATED_MASTER_PACKING_QTY)
            .masterNestCode(UPDATED_MASTER_NEST_CODE)
            .innerQty(UPDATED_INNER_QTY)
            .packSize(UPDATED_PACK_SIZE)
            .nestCode(UPDATED_NEST_CODE)
            .countryOfOrigin(UPDATED_COUNTRY_OF_ORIGIN)
            .vendorName(UPDATED_VENDOR_NAME)
            .numberOfSet(UPDATED_NUMBER_OF_SET)
            .priceFOB(UPDATED_PRICE_FOB)
            .qty40HC(UPDATED_QTY_40_HC)
            .d57Qty(UPDATED_D_57_QTY)
            .category(UPDATED_CATEGORY)
            .labels(UPDATED_LABELS)
            .planningNotes(UPDATED_PLANNING_NOTES)
            .factTag(UPDATED_FACT_TAG)
            .packagingLength(UPDATED_PACKAGING_LENGTH)
            .packagingHeight(UPDATED_PACKAGING_HEIGHT)
            .packagingWidth(UPDATED_PACKAGING_WIDTH);

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductUpdatableFieldsEquals(partialUpdatedProduct, getPersistedProduct(partialUpdatedProduct));
    }

    @Test
    @Transactional
    void patchNonExistingProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProduct() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the product
        restProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, product.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productRepository.count();
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

    protected Product getPersistedProduct(Product product) {
        return productRepository.findById(product.getId()).orElseThrow();
    }

    protected void assertPersistedProductToMatchAllProperties(Product expectedProduct) {
        assertProductAllPropertiesEquals(expectedProduct, getPersistedProduct(expectedProduct));
    }

    protected void assertPersistedProductToMatchUpdatableProperties(Product expectedProduct) {
        assertProductAllUpdatablePropertiesEquals(expectedProduct, getPersistedProduct(expectedProduct));
    }
}
