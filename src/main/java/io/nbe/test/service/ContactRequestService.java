package io.nbe.test.service;

import io.nbe.test.domain.ContactRequest;
import io.nbe.test.domain.ExtandedUser;
import io.nbe.test.repository.ContactRequestRepository;
import io.nbe.test.repository.search.ContactRequestSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ContactRequest.
 */
@Service
@Transactional
public class ContactRequestService {

    private final Logger log = LoggerFactory.getLogger(ContactRequestService.class);
    
    @Inject
    private ContactRequestRepository contactRequestRepository;

    @Inject
    private ContactRequestSearchRepository contactRequestSearchRepository;

    @Inject
    private ExtandedUserService extendUserService;

    public ContactRequest createRequest(ContactRequest request){
        Optional<ExtandedUser> currentExtendedUser = extendUserService.getCurrentExtendedUser();
        if(!request.getReceiver().equals(currentExtendedUser)){
            request.setSender(currentExtendedUser.get());
            if(!extendUserService.findFriends().contains(request.getReceiver())){
                if(!contactRequestRepository.findAll().contains(request.getReceiver())){
                    request.setDateAsked(ZonedDateTime.now());
                    request.setDateAccepted(null);
                    request.setIsAccepted(false);
                    save(request);
                }
            }
        }
        return request;
    }

    public ContactRequest acceptRequest(ContactRequest request){
        request.setIsAccepted(true);
        request.setDateAccepted(ZonedDateTime.now());
        save(request);
        return request;
    }

    public ContactRequest denyRequest(ContactRequest request){
        delete(request.getId());
        return request;
    }



    /**
     * Save a contactRequest.
     *
     * @param contactRequest the entity to save
     * @return the persisted entity
     */
    public ContactRequest save(ContactRequest contactRequest) {
        log.debug("Request to save ContactRequest : {}", contactRequest);
        ContactRequest result = contactRequestRepository.save(contactRequest);
        contactRequestSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the contactRequests.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ContactRequest> findAll(Pageable pageable) {
        log.debug("Request to get all ContactRequests");
        Page<ContactRequest> result = contactRequestRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one contactRequest by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ContactRequest findOne(Long id) {
        log.debug("Request to get ContactRequest : {}", id);
        ContactRequest contactRequest = contactRequestRepository.findOne(id);
        return contactRequest;
    }

    /**
     *  Delete the  contactRequest by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ContactRequest : {}", id);
        contactRequestRepository.delete(id);
        contactRequestSearchRepository.delete(id);
    }

    /**
     * Search for the contactRequest corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ContactRequest> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ContactRequests for query {}", query);
        Page<ContactRequest> result = contactRequestSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
