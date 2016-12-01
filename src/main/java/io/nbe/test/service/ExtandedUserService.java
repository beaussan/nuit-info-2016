package io.nbe.test.service;

import io.nbe.test.domain.ExtandedUser;
import io.nbe.test.repository.ExtandedUserRepository;
import io.nbe.test.repository.search.ExtandedUserSearchRepository;
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
 * Service Implementation for managing ExtandedUser.
 */
@Service
@Transactional
public class ExtandedUserService {

    private final Logger log = LoggerFactory.getLogger(ExtandedUserService.class);
    
    @Inject
    private ExtandedUserRepository extandedUserRepository;

    @Inject
    private ExtandedUserSearchRepository extandedUserSearchRepository;

    /**
     * Save a extandedUser.
     *
     * @param extandedUser the entity to save
     * @return the persisted entity
     */
    public ExtandedUser save(ExtandedUser extandedUser) {
        log.debug("Request to save ExtandedUser : {}", extandedUser);
        ExtandedUser result = extandedUserRepository.save(extandedUser);
        extandedUserSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the extandedUsers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ExtandedUser> findAll(Pageable pageable) {
        log.debug("Request to get all ExtandedUsers");
        Page<ExtandedUser> result = extandedUserRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one extandedUser by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ExtandedUser findOne(Long id) {
        log.debug("Request to get ExtandedUser : {}", id);
        ExtandedUser extandedUser = extandedUserRepository.findOne(id);
        return extandedUser;
    }

    /**
     *  Delete the  extandedUser by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ExtandedUser : {}", id);
        extandedUserRepository.delete(id);
        extandedUserSearchRepository.delete(id);
    }

    /**
     * Search for the extandedUser corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ExtandedUser> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ExtandedUsers for query {}", query);
        Page<ExtandedUser> result = extandedUserSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
