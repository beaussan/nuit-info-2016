package io.nbe.test.web.rest;

import io.nbe.test.TestJhApp;

import io.nbe.test.domain.ContactRequest;
import io.nbe.test.repository.ContactRequestRepository;
import io.nbe.test.service.ContactRequestService;
import io.nbe.test.repository.search.ContactRequestSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static io.nbe.test.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ContactRequestResource REST controller.
 *
 * @see ContactRequestResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestJhApp.class)
public class ContactRequestResourceIntTest {

    private static final Boolean DEFAULT_IS_ACCEPTED = false;
    private static final Boolean UPDATED_IS_ACCEPTED = true;

    private static final ZonedDateTime DEFAULT_DATE_ACCEPTED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_ACCEPTED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_ASKED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_ASKED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    @Inject
    private ContactRequestRepository contactRequestRepository;

    @Inject
    private ContactRequestService contactRequestService;

    @Inject
    private ContactRequestSearchRepository contactRequestSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restContactRequestMockMvc;

    private ContactRequest contactRequest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ContactRequestResource contactRequestResource = new ContactRequestResource();
        ReflectionTestUtils.setField(contactRequestResource, "contactRequestService", contactRequestService);
        this.restContactRequestMockMvc = MockMvcBuilders.standaloneSetup(contactRequestResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactRequest createEntity(EntityManager em) {
        ContactRequest contactRequest = new ContactRequest()
                .isAccepted(DEFAULT_IS_ACCEPTED)
                .dateAccepted(DEFAULT_DATE_ACCEPTED)
                .dateAsked(DEFAULT_DATE_ASKED)
                .message(DEFAULT_MESSAGE);
        return contactRequest;
    }

    @Before
    public void initTest() {
        contactRequestSearchRepository.deleteAll();
        contactRequest = createEntity(em);
    }

    @Test
    @Transactional
    public void createContactRequest() throws Exception {
        int databaseSizeBeforeCreate = contactRequestRepository.findAll().size();

        // Create the ContactRequest

        restContactRequestMockMvc.perform(post("/api/contact-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactRequest)))
            .andExpect(status().isCreated());

        // Validate the ContactRequest in the database
        List<ContactRequest> contactRequests = contactRequestRepository.findAll();
        assertThat(contactRequests).hasSize(databaseSizeBeforeCreate + 1);
        ContactRequest testContactRequest = contactRequests.get(contactRequests.size() - 1);
        assertThat(testContactRequest.isIsAccepted()).isEqualTo(DEFAULT_IS_ACCEPTED);
        assertThat(testContactRequest.getDateAccepted()).isEqualTo(DEFAULT_DATE_ACCEPTED);
        assertThat(testContactRequest.getDateAsked()).isEqualTo(DEFAULT_DATE_ASKED);
        assertThat(testContactRequest.getMessage()).isEqualTo(DEFAULT_MESSAGE);

        // Validate the ContactRequest in ElasticSearch
        ContactRequest contactRequestEs = contactRequestSearchRepository.findOne(testContactRequest.getId());
        assertThat(contactRequestEs).isEqualToComparingFieldByField(testContactRequest);
    }

    @Test
    @Transactional
    public void getAllContactRequests() throws Exception {
        // Initialize the database
        contactRequestRepository.saveAndFlush(contactRequest);

        // Get all the contactRequests
        restContactRequestMockMvc.perform(get("/api/contact-requests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].isAccepted").value(hasItem(DEFAULT_IS_ACCEPTED.booleanValue())))
            .andExpect(jsonPath("$.[*].dateAccepted").value(hasItem(sameInstant(DEFAULT_DATE_ACCEPTED))))
            .andExpect(jsonPath("$.[*].dateAsked").value(hasItem(sameInstant(DEFAULT_DATE_ASKED))))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())));
    }

    @Test
    @Transactional
    public void getContactRequest() throws Exception {
        // Initialize the database
        contactRequestRepository.saveAndFlush(contactRequest);

        // Get the contactRequest
        restContactRequestMockMvc.perform(get("/api/contact-requests/{id}", contactRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contactRequest.getId().intValue()))
            .andExpect(jsonPath("$.isAccepted").value(DEFAULT_IS_ACCEPTED.booleanValue()))
            .andExpect(jsonPath("$.dateAccepted").value(sameInstant(DEFAULT_DATE_ACCEPTED)))
            .andExpect(jsonPath("$.dateAsked").value(sameInstant(DEFAULT_DATE_ASKED)))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingContactRequest() throws Exception {
        // Get the contactRequest
        restContactRequestMockMvc.perform(get("/api/contact-requests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContactRequest() throws Exception {
        // Initialize the database
        contactRequestService.save(contactRequest);

        int databaseSizeBeforeUpdate = contactRequestRepository.findAll().size();

        // Update the contactRequest
        ContactRequest updatedContactRequest = contactRequestRepository.findOne(contactRequest.getId());
        updatedContactRequest
                .isAccepted(UPDATED_IS_ACCEPTED)
                .dateAccepted(UPDATED_DATE_ACCEPTED)
                .dateAsked(UPDATED_DATE_ASKED)
                .message(UPDATED_MESSAGE);

        restContactRequestMockMvc.perform(put("/api/contact-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedContactRequest)))
            .andExpect(status().isOk());

        // Validate the ContactRequest in the database
        List<ContactRequest> contactRequests = contactRequestRepository.findAll();
        assertThat(contactRequests).hasSize(databaseSizeBeforeUpdate);
        ContactRequest testContactRequest = contactRequests.get(contactRequests.size() - 1);
        assertThat(testContactRequest.isIsAccepted()).isEqualTo(UPDATED_IS_ACCEPTED);
        assertThat(testContactRequest.getDateAccepted()).isEqualTo(UPDATED_DATE_ACCEPTED);
        assertThat(testContactRequest.getDateAsked()).isEqualTo(UPDATED_DATE_ASKED);
        assertThat(testContactRequest.getMessage()).isEqualTo(UPDATED_MESSAGE);

        // Validate the ContactRequest in ElasticSearch
        ContactRequest contactRequestEs = contactRequestSearchRepository.findOne(testContactRequest.getId());
        assertThat(contactRequestEs).isEqualToComparingFieldByField(testContactRequest);
    }

    @Test
    @Transactional
    public void deleteContactRequest() throws Exception {
        // Initialize the database
        contactRequestService.save(contactRequest);

        int databaseSizeBeforeDelete = contactRequestRepository.findAll().size();

        // Get the contactRequest
        restContactRequestMockMvc.perform(delete("/api/contact-requests/{id}", contactRequest.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean contactRequestExistsInEs = contactRequestSearchRepository.exists(contactRequest.getId());
        assertThat(contactRequestExistsInEs).isFalse();

        // Validate the database is empty
        List<ContactRequest> contactRequests = contactRequestRepository.findAll();
        assertThat(contactRequests).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchContactRequest() throws Exception {
        // Initialize the database
        contactRequestService.save(contactRequest);

        // Search the contactRequest
        restContactRequestMockMvc.perform(get("/api/_search/contact-requests?query=id:" + contactRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].isAccepted").value(hasItem(DEFAULT_IS_ACCEPTED.booleanValue())))
            .andExpect(jsonPath("$.[*].dateAccepted").value(hasItem(sameInstant(DEFAULT_DATE_ACCEPTED))))
            .andExpect(jsonPath("$.[*].dateAsked").value(hasItem(sameInstant(DEFAULT_DATE_ASKED))))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())));
    }
}
