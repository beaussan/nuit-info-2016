package io.nbe.test.service;

import io.nbe.test.domain.Directory;
import io.nbe.test.repository.DirectoryRepository;
import io.nbe.test.repository.search.DirectorySearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Directory.
 */
@Service
@Transactional
public class DirectoryService {

    private final Logger log = LoggerFactory.getLogger(DirectoryService.class);
    
    @Inject
    private DirectoryRepository directoryRepository;

    @Inject
    private DirectorySearchRepository directorySearchRepository;

    /**
     * Save a directory.
     *
     * @param directory the entity to save
     * @return the persisted entity
     */
    public Directory save(Directory directory) {
        log.debug("Request to save Directory : {}", directory);
        Directory result = directoryRepository.save(directory);
        directorySearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the directories.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Directory> findAll(Pageable pageable) {
        log.debug("Request to get all Directories");
        Page<Directory> result = directoryRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one directory by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Directory findOne(Long id) {
        log.debug("Request to get Directory : {}", id);
        Directory directory = directoryRepository.findOne(id);
        return directory;
    }

    /**
     *  Delete the  directory by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Directory : {}", id);
        directoryRepository.delete(id);
        directorySearchRepository.delete(id);
    }

    /**
     * Search for the directory corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Directory> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Directories for query {}", query);
        Page<Directory> result = directorySearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
