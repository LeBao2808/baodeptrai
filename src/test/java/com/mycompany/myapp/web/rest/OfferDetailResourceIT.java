package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.OfferDetailAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Offer;
import com.mycompany.myapp.domain.OfferDetail;
import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.repository.OfferDetailRepository;
import com.mycompany.myapp.service.dto.OfferDetailDTO;
import com.mycompany.myapp.service.mapper.OfferDetailMapper;
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
 * Integration tests for the {@link OfferDetailResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OfferDetailResourceIT {

    private static final String DEFAULT_FEEDBACK = "AAAAAAAAAA";
    private static final String UPDATED_FEEDBACK = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/offer-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OfferDetailRepository offerDetailRepository;

    @Autowired
    private OfferDetailMapper offerDetailMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOfferDetailMockMvc;

    private OfferDetail offerDetail;

    private OfferDetail insertedOfferDetail;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OfferDetail createEntity(EntityManager em) {
        OfferDetail offerDetail = new OfferDetail().feedback(DEFAULT_FEEDBACK);
        return offerDetail;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OfferDetail createUpdatedEntity(EntityManager em) {
        OfferDetail offerDetail = new OfferDetail().feedback(UPDATED_FEEDBACK);
        return offerDetail;
    }

    @BeforeEach
    public void initTest() {
        offerDetail = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedOfferDetail != null) {
            offerDetailRepository.delete(insertedOfferDetail);
            insertedOfferDetail = null;
        }
    }

    @Test
    @Transactional
    void createOfferDetail() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OfferDetail
        OfferDetailDTO offerDetailDTO = offerDetailMapper.toDto(offerDetail);
        var returnedOfferDetailDTO = om.readValue(
            restOfferDetailMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(offerDetailDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OfferDetailDTO.class
        );

        // Validate the OfferDetail in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOfferDetail = offerDetailMapper.toEntity(returnedOfferDetailDTO);
        assertOfferDetailUpdatableFieldsEquals(returnedOfferDetail, getPersistedOfferDetail(returnedOfferDetail));

        insertedOfferDetail = returnedOfferDetail;
    }

    @Test
    @Transactional
    void createOfferDetailWithExistingId() throws Exception {
        // Create the OfferDetail with an existing ID
        offerDetail.setId(1L);
        OfferDetailDTO offerDetailDTO = offerDetailMapper.toDto(offerDetail);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOfferDetailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(offerDetailDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OfferDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOfferDetails() throws Exception {
        // Initialize the database
        insertedOfferDetail = offerDetailRepository.saveAndFlush(offerDetail);

        // Get all the offerDetailList
        restOfferDetailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(offerDetail.getId().intValue())))
            .andExpect(jsonPath("$.[*].feedback").value(hasItem(DEFAULT_FEEDBACK)));
    }

    @Test
    @Transactional
    void getOfferDetail() throws Exception {
        // Initialize the database
        insertedOfferDetail = offerDetailRepository.saveAndFlush(offerDetail);

        // Get the offerDetail
        restOfferDetailMockMvc
            .perform(get(ENTITY_API_URL_ID, offerDetail.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(offerDetail.getId().intValue()))
            .andExpect(jsonPath("$.feedback").value(DEFAULT_FEEDBACK));
    }

    @Test
    @Transactional
    void getOfferDetailsByIdFiltering() throws Exception {
        // Initialize the database
        insertedOfferDetail = offerDetailRepository.saveAndFlush(offerDetail);

        Long id = offerDetail.getId();

        defaultOfferDetailFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOfferDetailFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOfferDetailFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOfferDetailsByFeedbackIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOfferDetail = offerDetailRepository.saveAndFlush(offerDetail);

        // Get all the offerDetailList where feedback equals to
        defaultOfferDetailFiltering("feedback.equals=" + DEFAULT_FEEDBACK, "feedback.equals=" + UPDATED_FEEDBACK);
    }

    @Test
    @Transactional
    void getAllOfferDetailsByFeedbackIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOfferDetail = offerDetailRepository.saveAndFlush(offerDetail);

        // Get all the offerDetailList where feedback in
        defaultOfferDetailFiltering("feedback.in=" + DEFAULT_FEEDBACK + "," + UPDATED_FEEDBACK, "feedback.in=" + UPDATED_FEEDBACK);
    }

    @Test
    @Transactional
    void getAllOfferDetailsByFeedbackIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOfferDetail = offerDetailRepository.saveAndFlush(offerDetail);

        // Get all the offerDetailList where feedback is not null
        defaultOfferDetailFiltering("feedback.specified=true", "feedback.specified=false");
    }

    @Test
    @Transactional
    void getAllOfferDetailsByFeedbackContainsSomething() throws Exception {
        // Initialize the database
        insertedOfferDetail = offerDetailRepository.saveAndFlush(offerDetail);

        // Get all the offerDetailList where feedback contains
        defaultOfferDetailFiltering("feedback.contains=" + DEFAULT_FEEDBACK, "feedback.contains=" + UPDATED_FEEDBACK);
    }

    @Test
    @Transactional
    void getAllOfferDetailsByFeedbackNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOfferDetail = offerDetailRepository.saveAndFlush(offerDetail);

        // Get all the offerDetailList where feedback does not contain
        defaultOfferDetailFiltering("feedback.doesNotContain=" + UPDATED_FEEDBACK, "feedback.doesNotContain=" + DEFAULT_FEEDBACK);
    }

    @Test
    @Transactional
    void getAllOfferDetailsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            offerDetailRepository.saveAndFlush(offerDetail);
            product = ProductResourceIT.createEntity(em);
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product);
        em.flush();
        offerDetail.setProduct(product);
        offerDetailRepository.saveAndFlush(offerDetail);
        Long productId = product.getId();
        // Get all the offerDetailList where product equals to productId
        defaultOfferDetailShouldBeFound("productId.equals=" + productId);

        // Get all the offerDetailList where product equals to (productId + 1)
        defaultOfferDetailShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllOfferDetailsByOfferIsEqualToSomething() throws Exception {
        Offer offer;
        if (TestUtil.findAll(em, Offer.class).isEmpty()) {
            offerDetailRepository.saveAndFlush(offerDetail);
            offer = OfferResourceIT.createEntity(em);
        } else {
            offer = TestUtil.findAll(em, Offer.class).get(0);
        }
        em.persist(offer);
        em.flush();
        offerDetail.setOffer(offer);
        offerDetailRepository.saveAndFlush(offerDetail);
        Long offerId = offer.getId();
        // Get all the offerDetailList where offer equals to offerId
        defaultOfferDetailShouldBeFound("offerId.equals=" + offerId);

        // Get all the offerDetailList where offer equals to (offerId + 1)
        defaultOfferDetailShouldNotBeFound("offerId.equals=" + (offerId + 1));
    }

    private void defaultOfferDetailFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOfferDetailShouldBeFound(shouldBeFound);
        defaultOfferDetailShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOfferDetailShouldBeFound(String filter) throws Exception {
        restOfferDetailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(offerDetail.getId().intValue())))
            .andExpect(jsonPath("$.[*].feedback").value(hasItem(DEFAULT_FEEDBACK)));

        // Check, that the count call also returns 1
        restOfferDetailMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOfferDetailShouldNotBeFound(String filter) throws Exception {
        restOfferDetailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOfferDetailMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOfferDetail() throws Exception {
        // Get the offerDetail
        restOfferDetailMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOfferDetail() throws Exception {
        // Initialize the database
        insertedOfferDetail = offerDetailRepository.saveAndFlush(offerDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the offerDetail
        OfferDetail updatedOfferDetail = offerDetailRepository.findById(offerDetail.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOfferDetail are not directly saved in db
        em.detach(updatedOfferDetail);
        updatedOfferDetail.feedback(UPDATED_FEEDBACK);
        OfferDetailDTO offerDetailDTO = offerDetailMapper.toDto(updatedOfferDetail);

        restOfferDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, offerDetailDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(offerDetailDTO))
            )
            .andExpect(status().isOk());

        // Validate the OfferDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOfferDetailToMatchAllProperties(updatedOfferDetail);
    }

    @Test
    @Transactional
    void putNonExistingOfferDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        offerDetail.setId(longCount.incrementAndGet());

        // Create the OfferDetail
        OfferDetailDTO offerDetailDTO = offerDetailMapper.toDto(offerDetail);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOfferDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, offerDetailDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(offerDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OfferDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOfferDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        offerDetail.setId(longCount.incrementAndGet());

        // Create the OfferDetail
        OfferDetailDTO offerDetailDTO = offerDetailMapper.toDto(offerDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOfferDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(offerDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OfferDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOfferDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        offerDetail.setId(longCount.incrementAndGet());

        // Create the OfferDetail
        OfferDetailDTO offerDetailDTO = offerDetailMapper.toDto(offerDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOfferDetailMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(offerDetailDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OfferDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOfferDetailWithPatch() throws Exception {
        // Initialize the database
        insertedOfferDetail = offerDetailRepository.saveAndFlush(offerDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the offerDetail using partial update
        OfferDetail partialUpdatedOfferDetail = new OfferDetail();
        partialUpdatedOfferDetail.setId(offerDetail.getId());

        partialUpdatedOfferDetail.feedback(UPDATED_FEEDBACK);

        restOfferDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOfferDetail.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOfferDetail))
            )
            .andExpect(status().isOk());

        // Validate the OfferDetail in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOfferDetailUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOfferDetail, offerDetail),
            getPersistedOfferDetail(offerDetail)
        );
    }

    @Test
    @Transactional
    void fullUpdateOfferDetailWithPatch() throws Exception {
        // Initialize the database
        insertedOfferDetail = offerDetailRepository.saveAndFlush(offerDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the offerDetail using partial update
        OfferDetail partialUpdatedOfferDetail = new OfferDetail();
        partialUpdatedOfferDetail.setId(offerDetail.getId());

        partialUpdatedOfferDetail.feedback(UPDATED_FEEDBACK);

        restOfferDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOfferDetail.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOfferDetail))
            )
            .andExpect(status().isOk());

        // Validate the OfferDetail in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOfferDetailUpdatableFieldsEquals(partialUpdatedOfferDetail, getPersistedOfferDetail(partialUpdatedOfferDetail));
    }

    @Test
    @Transactional
    void patchNonExistingOfferDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        offerDetail.setId(longCount.incrementAndGet());

        // Create the OfferDetail
        OfferDetailDTO offerDetailDTO = offerDetailMapper.toDto(offerDetail);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOfferDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, offerDetailDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(offerDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OfferDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOfferDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        offerDetail.setId(longCount.incrementAndGet());

        // Create the OfferDetail
        OfferDetailDTO offerDetailDTO = offerDetailMapper.toDto(offerDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOfferDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(offerDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OfferDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOfferDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        offerDetail.setId(longCount.incrementAndGet());

        // Create the OfferDetail
        OfferDetailDTO offerDetailDTO = offerDetailMapper.toDto(offerDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOfferDetailMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(offerDetailDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OfferDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOfferDetail() throws Exception {
        // Initialize the database
        insertedOfferDetail = offerDetailRepository.saveAndFlush(offerDetail);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the offerDetail
        restOfferDetailMockMvc
            .perform(delete(ENTITY_API_URL_ID, offerDetail.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return offerDetailRepository.count();
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

    protected OfferDetail getPersistedOfferDetail(OfferDetail offerDetail) {
        return offerDetailRepository.findById(offerDetail.getId()).orElseThrow();
    }

    protected void assertPersistedOfferDetailToMatchAllProperties(OfferDetail expectedOfferDetail) {
        assertOfferDetailAllPropertiesEquals(expectedOfferDetail, getPersistedOfferDetail(expectedOfferDetail));
    }

    protected void assertPersistedOfferDetailToMatchUpdatableProperties(OfferDetail expectedOfferDetail) {
        assertOfferDetailAllUpdatablePropertiesEquals(expectedOfferDetail, getPersistedOfferDetail(expectedOfferDetail));
    }
}
