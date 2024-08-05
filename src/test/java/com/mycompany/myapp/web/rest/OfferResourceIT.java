package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.OfferAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Customer;
import com.mycompany.myapp.domain.Offer;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.OfferRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.service.dto.OfferDTO;
import com.mycompany.myapp.service.mapper.OfferMapper;
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
 * Integration tests for the {@link OfferResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OfferResourceIT {

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;
    private static final Integer SMALLER_STATUS = 1 - 1;

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/offers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OfferMapper offerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOfferMockMvc;

    private Offer offer;

    private Offer insertedOffer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Offer createEntity(EntityManager em) {
        Offer offer = new Offer().date(DEFAULT_DATE).status(DEFAULT_STATUS).code(DEFAULT_CODE);
        return offer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Offer createUpdatedEntity(EntityManager em) {
        Offer offer = new Offer().date(UPDATED_DATE).status(UPDATED_STATUS).code(UPDATED_CODE);
        return offer;
    }

    @BeforeEach
    public void initTest() {
        offer = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedOffer != null) {
            offerRepository.delete(insertedOffer);
            insertedOffer = null;
        }
    }

    @Test
    @Transactional
    void createOffer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Offer
        OfferDTO offerDTO = offerMapper.toDto(offer);
        var returnedOfferDTO = om.readValue(
            restOfferMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(offerDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OfferDTO.class
        );

        // Validate the Offer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOffer = offerMapper.toEntity(returnedOfferDTO);
        assertOfferUpdatableFieldsEquals(returnedOffer, getPersistedOffer(returnedOffer));

        insertedOffer = returnedOffer;
    }

    @Test
    @Transactional
    void createOfferWithExistingId() throws Exception {
        // Create the Offer with an existing ID
        offer.setId(1L);
        OfferDTO offerDTO = offerMapper.toDto(offer);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOfferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(offerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Offer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOffers() throws Exception {
        // Initialize the database
        insertedOffer = offerRepository.saveAndFlush(offer);

        // Get all the offerList
        restOfferMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(offer.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getOffer() throws Exception {
        // Initialize the database
        insertedOffer = offerRepository.saveAndFlush(offer);

        // Get the offer
        restOfferMockMvc
            .perform(get(ENTITY_API_URL_ID, offer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(offer.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getOffersByIdFiltering() throws Exception {
        // Initialize the database
        insertedOffer = offerRepository.saveAndFlush(offer);

        Long id = offer.getId();

        defaultOfferFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOfferFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOfferFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOffersByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOffer = offerRepository.saveAndFlush(offer);

        // Get all the offerList where date equals to
        defaultOfferFiltering("date.equals=" + DEFAULT_DATE, "date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllOffersByDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOffer = offerRepository.saveAndFlush(offer);

        // Get all the offerList where date in
        defaultOfferFiltering("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE, "date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllOffersByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOffer = offerRepository.saveAndFlush(offer);

        // Get all the offerList where date is not null
        defaultOfferFiltering("date.specified=true", "date.specified=false");
    }

    @Test
    @Transactional
    void getAllOffersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOffer = offerRepository.saveAndFlush(offer);

        // Get all the offerList where status equals to
        defaultOfferFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOffersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOffer = offerRepository.saveAndFlush(offer);

        // Get all the offerList where status in
        defaultOfferFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOffersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOffer = offerRepository.saveAndFlush(offer);

        // Get all the offerList where status is not null
        defaultOfferFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllOffersByStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOffer = offerRepository.saveAndFlush(offer);

        // Get all the offerList where status is greater than or equal to
        defaultOfferFiltering("status.greaterThanOrEqual=" + DEFAULT_STATUS, "status.greaterThanOrEqual=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOffersByStatusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOffer = offerRepository.saveAndFlush(offer);

        // Get all the offerList where status is less than or equal to
        defaultOfferFiltering("status.lessThanOrEqual=" + DEFAULT_STATUS, "status.lessThanOrEqual=" + SMALLER_STATUS);
    }

    @Test
    @Transactional
    void getAllOffersByStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOffer = offerRepository.saveAndFlush(offer);

        // Get all the offerList where status is less than
        defaultOfferFiltering("status.lessThan=" + UPDATED_STATUS, "status.lessThan=" + DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void getAllOffersByStatusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOffer = offerRepository.saveAndFlush(offer);

        // Get all the offerList where status is greater than
        defaultOfferFiltering("status.greaterThan=" + SMALLER_STATUS, "status.greaterThan=" + DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void getAllOffersByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOffer = offerRepository.saveAndFlush(offer);

        // Get all the offerList where code equals to
        defaultOfferFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllOffersByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOffer = offerRepository.saveAndFlush(offer);

        // Get all the offerList where code in
        defaultOfferFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllOffersByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOffer = offerRepository.saveAndFlush(offer);

        // Get all the offerList where code is not null
        defaultOfferFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllOffersByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedOffer = offerRepository.saveAndFlush(offer);

        // Get all the offerList where code contains
        defaultOfferFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllOffersByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOffer = offerRepository.saveAndFlush(offer);

        // Get all the offerList where code does not contain
        defaultOfferFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllOffersByCustomerIsEqualToSomething() throws Exception {
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            offerRepository.saveAndFlush(offer);
            customer = CustomerResourceIT.createEntity(em);
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        em.persist(customer);
        em.flush();
        offer.setCustomer(customer);
        offerRepository.saveAndFlush(offer);
        Long customerId = customer.getId();
        // Get all the offerList where customer equals to customerId
        defaultOfferShouldBeFound("customerId.equals=" + customerId);

        // Get all the offerList where customer equals to (customerId + 1)
        defaultOfferShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    @Test
    @Transactional
    void getAllOffersByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            offerRepository.saveAndFlush(offer);
            user = UserResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        offer.setUser(user);
        offerRepository.saveAndFlush(offer);
        Long userId = user.getId();
        // Get all the offerList where user equals to userId
        defaultOfferShouldBeFound("userId.equals=" + userId);

        // Get all the offerList where user equals to (userId + 1)
        defaultOfferShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    private void defaultOfferFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOfferShouldBeFound(shouldBeFound);
        defaultOfferShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOfferShouldBeFound(String filter) throws Exception {
        restOfferMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(offer.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));

        // Check, that the count call also returns 1
        restOfferMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOfferShouldNotBeFound(String filter) throws Exception {
        restOfferMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOfferMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOffer() throws Exception {
        // Get the offer
        restOfferMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOffer() throws Exception {
        // Initialize the database
        insertedOffer = offerRepository.saveAndFlush(offer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the offer
        Offer updatedOffer = offerRepository.findById(offer.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOffer are not directly saved in db
        em.detach(updatedOffer);
        updatedOffer.date(UPDATED_DATE).status(UPDATED_STATUS).code(UPDATED_CODE);
        OfferDTO offerDTO = offerMapper.toDto(updatedOffer);

        restOfferMockMvc
            .perform(
                put(ENTITY_API_URL_ID, offerDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(offerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Offer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOfferToMatchAllProperties(updatedOffer);
    }

    @Test
    @Transactional
    void putNonExistingOffer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        offer.setId(longCount.incrementAndGet());

        // Create the Offer
        OfferDTO offerDTO = offerMapper.toDto(offer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOfferMockMvc
            .perform(
                put(ENTITY_API_URL_ID, offerDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(offerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Offer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOffer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        offer.setId(longCount.incrementAndGet());

        // Create the Offer
        OfferDTO offerDTO = offerMapper.toDto(offer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOfferMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(offerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Offer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOffer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        offer.setId(longCount.incrementAndGet());

        // Create the Offer
        OfferDTO offerDTO = offerMapper.toDto(offer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOfferMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(offerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Offer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOfferWithPatch() throws Exception {
        // Initialize the database
        insertedOffer = offerRepository.saveAndFlush(offer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the offer using partial update
        Offer partialUpdatedOffer = new Offer();
        partialUpdatedOffer.setId(offer.getId());

        partialUpdatedOffer.code(UPDATED_CODE);

        restOfferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOffer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOffer))
            )
            .andExpect(status().isOk());

        // Validate the Offer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOfferUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedOffer, offer), getPersistedOffer(offer));
    }

    @Test
    @Transactional
    void fullUpdateOfferWithPatch() throws Exception {
        // Initialize the database
        insertedOffer = offerRepository.saveAndFlush(offer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the offer using partial update
        Offer partialUpdatedOffer = new Offer();
        partialUpdatedOffer.setId(offer.getId());

        partialUpdatedOffer.date(UPDATED_DATE).status(UPDATED_STATUS).code(UPDATED_CODE);

        restOfferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOffer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOffer))
            )
            .andExpect(status().isOk());

        // Validate the Offer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOfferUpdatableFieldsEquals(partialUpdatedOffer, getPersistedOffer(partialUpdatedOffer));
    }

    @Test
    @Transactional
    void patchNonExistingOffer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        offer.setId(longCount.incrementAndGet());

        // Create the Offer
        OfferDTO offerDTO = offerMapper.toDto(offer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOfferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, offerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(offerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Offer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOffer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        offer.setId(longCount.incrementAndGet());

        // Create the Offer
        OfferDTO offerDTO = offerMapper.toDto(offer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOfferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(offerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Offer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOffer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        offer.setId(longCount.incrementAndGet());

        // Create the Offer
        OfferDTO offerDTO = offerMapper.toDto(offer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOfferMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(offerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Offer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOffer() throws Exception {
        // Initialize the database
        insertedOffer = offerRepository.saveAndFlush(offer);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the offer
        restOfferMockMvc
            .perform(delete(ENTITY_API_URL_ID, offer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return offerRepository.count();
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

    protected Offer getPersistedOffer(Offer offer) {
        return offerRepository.findById(offer.getId()).orElseThrow();
    }

    protected void assertPersistedOfferToMatchAllProperties(Offer expectedOffer) {
        assertOfferAllPropertiesEquals(expectedOffer, getPersistedOffer(expectedOffer));
    }

    protected void assertPersistedOfferToMatchUpdatableProperties(Offer expectedOffer) {
        assertOfferAllUpdatablePropertiesEquals(expectedOffer, getPersistedOffer(expectedOffer));
    }
}
