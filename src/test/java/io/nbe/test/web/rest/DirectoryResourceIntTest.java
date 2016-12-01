package io.nbe.test.web.rest;

import io.nbe.test.TestJhApp;

import io.nbe.test.domain.Directory;
import io.nbe.test.repository.DirectoryRepository;
import io.nbe.test.service.DirectoryService;
import io.nbe.test.repository.search.DirectorySearchRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DirectoryResource REST controller.
 *
 * @see DirectoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestJhApp.class)
public class DirectoryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LINK = "AAAAAAAAAA";
    private static final String UPDATED_LINK = "BBBBBBBBBB";

    @Inject
    private DirectoryRepository directoryRepository;

    @Inject
    private DirectoryService directoryService;

    @Inject
    private DirectorySearchRepository directorySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDirectoryMockMvc;

    private Directory directory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DirectoryResource directoryResource = new DirectoryResource();
        ReflectionTestUtils.setField(directoryResource, "directoryService", directoryService);
        this.restDirectoryMockMvc = MockMvcBuilders.standaloneSetup(directoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Directory createEntity(EntityManager em) {
        Directory directory = new Directory()
                .name(DEFAULT_NAME)
                .link(DEFAULT_LINK);
        return directory;
    }

    @Before
    public void initTest() {
        directorySearchRepository.deleteAll();
        directory = createEntity(em);
    }

    @Test
    @Transactional
    public void createDirectory() throws Exception {
        int databaseSizeBeforeCreate = directoryRepository.findAll().size();

        // Create the Directory

        restDirectoryMockMvc.perform(post("/api/directories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(directory)))
            .andExpect(status().isCreated());

        // Validate the Directory in the database
        List<Directory> directories = directoryRepository.findAll();
        assertThat(directories).hasSize(databaseSizeBeforeCreate + 1);
        Directory testDirectory = directories.get(directories.size() - 1);
        assertThat(testDirectory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDirectory.getLink()).isEqualTo(DEFAULT_LINK);

        // Validate the Directory in ElasticSearch
        Directory directoryEs = directorySearchRepository.findOne(testDirectory.getId());
        assertThat(directoryEs).isEqualToComparingFieldByField(testDirectory);
    }

    @Test
    @Transactional
    public void getAllDirectories() throws Exception {
        // Initialize the database
        directoryRepository.saveAndFlush(directory);

        // Get all the directories
        restDirectoryMockMvc.perform(get("/api/directories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(directory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK.toString())));
    }

    @Test
    @Transactional
    public void getDirectory() throws Exception {
        // Initialize the database
        directoryRepository.saveAndFlush(directory);

        // Get the directory
        restDirectoryMockMvc.perform(get("/api/directories/{id}", directory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(directory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDirectory() throws Exception {
        // Get the directory
        restDirectoryMockMvc.perform(get("/api/directories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDirectory() throws Exception {
        // Initialize the database
        directoryService.save(directory);

        int databaseSizeBeforeUpdate = directoryRepository.findAll().size();

        // Update the directory
        Directory updatedDirectory = directoryRepository.findOne(directory.getId());
        updatedDirectory
                .name(UPDATED_NAME)
                .link(UPDATED_LINK);

        restDirectoryMockMvc.perform(put("/api/directories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDirectory)))
            .andExpect(status().isOk());

        // Validate the Directory in the database
        List<Directory> directories = directoryRepository.findAll();
        assertThat(directories).hasSize(databaseSizeBeforeUpdate);
        Directory testDirectory = directories.get(directories.size() - 1);
        assertThat(testDirectory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDirectory.getLink()).isEqualTo(UPDATED_LINK);

        // Validate the Directory in ElasticSearch
        Directory directoryEs = directorySearchRepository.findOne(testDirectory.getId());
        assertThat(directoryEs).isEqualToComparingFieldByField(testDirectory);
    }

    @Test
    @Transactional
    public void deleteDirectory() throws Exception {
        // Initialize the database
        directoryService.save(directory);

        int databaseSizeBeforeDelete = directoryRepository.findAll().size();

        // Get the directory
        restDirectoryMockMvc.perform(delete("/api/directories/{id}", directory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean directoryExistsInEs = directorySearchRepository.exists(directory.getId());
        assertThat(directoryExistsInEs).isFalse();

        // Validate the database is empty
        List<Directory> directories = directoryRepository.findAll();
        assertThat(directories).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDirectory() throws Exception {
        // Initialize the database
        directoryService.save(directory);

        // Search the directory
        restDirectoryMockMvc.perform(get("/api/_search/directories?query=id:" + directory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(directory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK.toString())));
    }
}
