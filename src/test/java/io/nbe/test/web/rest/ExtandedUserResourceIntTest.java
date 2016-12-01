package io.nbe.test.web.rest;

import io.nbe.test.TestJhApp;

import io.nbe.test.domain.ExtandedUser;
import io.nbe.test.repository.ExtandedUserRepository;
import io.nbe.test.service.ExtandedUserService;
import io.nbe.test.repository.search.ExtandedUserSearchRepository;

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
 * Test class for the ExtandedUserResource REST controller.
 *
 * @see ExtandedUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestJhApp.class)
public class ExtandedUserResourceIntTest {

    private static final ZonedDateTime DEFAULT_LAST_CONNECTION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_CONNECTION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Inject
    private ExtandedUserRepository extandedUserRepository;

    @Inject
    private ExtandedUserService extandedUserService;

    @Inject
    private ExtandedUserSearchRepository extandedUserSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restExtandedUserMockMvc;

    private ExtandedUser extandedUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ExtandedUserResource extandedUserResource = new ExtandedUserResource();
        ReflectionTestUtils.setField(extandedUserResource, "extandedUserService", extandedUserService);
        this.restExtandedUserMockMvc = MockMvcBuilders.standaloneSetup(extandedUserResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExtandedUser createEntity(EntityManager em) {
        ExtandedUser extandedUser = new ExtandedUser()
                .lastConnection(DEFAULT_LAST_CONNECTION);
        return extandedUser;
    }

    @Before
    public void initTest() {
        extandedUserSearchRepository.deleteAll();
        extandedUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createExtandedUser() throws Exception {
        int databaseSizeBeforeCreate = extandedUserRepository.findAll().size();

        // Create the ExtandedUser

        restExtandedUserMockMvc.perform(post("/api/extanded-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extandedUser)))
            .andExpect(status().isCreated());

        // Validate the ExtandedUser in the database
        List<ExtandedUser> extandedUsers = extandedUserRepository.findAll();
        assertThat(extandedUsers).hasSize(databaseSizeBeforeCreate + 1);
        ExtandedUser testExtandedUser = extandedUsers.get(extandedUsers.size() - 1);
        assertThat(testExtandedUser.getLastConnection()).isEqualTo(DEFAULT_LAST_CONNECTION);

        // Validate the ExtandedUser in ElasticSearch
        ExtandedUser extandedUserEs = extandedUserSearchRepository.findOne(testExtandedUser.getId());
        assertThat(extandedUserEs).isEqualToComparingFieldByField(testExtandedUser);
    }

    @Test
    @Transactional
    public void getAllExtandedUsers() throws Exception {
        // Initialize the database
        extandedUserRepository.saveAndFlush(extandedUser);

        // Get all the extandedUsers
        restExtandedUserMockMvc.perform(get("/api/extanded-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extandedUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].lastConnection").value(hasItem(sameInstant(DEFAULT_LAST_CONNECTION))));
    }

    @Test
    @Transactional
    public void getExtandedUser() throws Exception {
        // Initialize the database
        extandedUserRepository.saveAndFlush(extandedUser);

        // Get the extandedUser
        restExtandedUserMockMvc.perform(get("/api/extanded-users/{id}", extandedUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(extandedUser.getId().intValue()))
            .andExpect(jsonPath("$.lastConnection").value(sameInstant(DEFAULT_LAST_CONNECTION)));
    }

    @Test
    @Transactional
    public void getNonExistingExtandedUser() throws Exception {
        // Get the extandedUser
        restExtandedUserMockMvc.perform(get("/api/extanded-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExtandedUser() throws Exception {
        // Initialize the database
        extandedUserService.save(extandedUser);

        int databaseSizeBeforeUpdate = extandedUserRepository.findAll().size();

        // Update the extandedUser
        ExtandedUser updatedExtandedUser = extandedUserRepository.findOne(extandedUser.getId());
        updatedExtandedUser
                .lastConnection(UPDATED_LAST_CONNECTION);

        restExtandedUserMockMvc.perform(put("/api/extanded-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedExtandedUser)))
            .andExpect(status().isOk());

        // Validate the ExtandedUser in the database
        List<ExtandedUser> extandedUsers = extandedUserRepository.findAll();
        assertThat(extandedUsers).hasSize(databaseSizeBeforeUpdate);
        ExtandedUser testExtandedUser = extandedUsers.get(extandedUsers.size() - 1);
        assertThat(testExtandedUser.getLastConnection()).isEqualTo(UPDATED_LAST_CONNECTION);

        // Validate the ExtandedUser in ElasticSearch
        ExtandedUser extandedUserEs = extandedUserSearchRepository.findOne(testExtandedUser.getId());
        assertThat(extandedUserEs).isEqualToComparingFieldByField(testExtandedUser);
    }

    @Test
    @Transactional
    public void deleteExtandedUser() throws Exception {
        // Initialize the database
        extandedUserService.save(extandedUser);

        int databaseSizeBeforeDelete = extandedUserRepository.findAll().size();

        // Get the extandedUser
        restExtandedUserMockMvc.perform(delete("/api/extanded-users/{id}", extandedUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean extandedUserExistsInEs = extandedUserSearchRepository.exists(extandedUser.getId());
        assertThat(extandedUserExistsInEs).isFalse();

        // Validate the database is empty
        List<ExtandedUser> extandedUsers = extandedUserRepository.findAll();
        assertThat(extandedUsers).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchExtandedUser() throws Exception {
        // Initialize the database
        extandedUserService.save(extandedUser);

        // Search the extandedUser
        restExtandedUserMockMvc.perform(get("/api/_search/extanded-users?query=id:" + extandedUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extandedUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].lastConnection").value(hasItem(sameInstant(DEFAULT_LAST_CONNECTION))));
    }
}
