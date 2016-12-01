package io.nbe.test.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.nbe.test.domain.Directory;
import io.nbe.test.service.DirectoryService;
import io.nbe.test.web.rest.util.HeaderUtil;
import io.nbe.test.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Directory.
 */
@RestController
@RequestMapping("/api")
public class DirectoryResource {

    private final Logger log = LoggerFactory.getLogger(DirectoryResource.class);
        
    @Inject
    private DirectoryService directoryService;

    /**
     * POST  /directories : Create a new directory.
     *
     * @param directory the directory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new directory, or with status 400 (Bad Request) if the directory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/directories")
    @Timed
    public ResponseEntity<Directory> createDirectory(@RequestBody Directory directory) throws URISyntaxException {
        log.debug("REST request to save Directory : {}", directory);
        if (directory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("directory", "idexists", "A new directory cannot already have an ID")).body(null);
        }
        Directory result = directoryService.save(directory);
        return ResponseEntity.created(new URI("/api/directories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("directory", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /directories : Updates an existing directory.
     *
     * @param directory the directory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated directory,
     * or with status 400 (Bad Request) if the directory is not valid,
     * or with status 500 (Internal Server Error) if the directory couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/directories")
    @Timed
    public ResponseEntity<Directory> updateDirectory(@RequestBody Directory directory) throws URISyntaxException {
        log.debug("REST request to update Directory : {}", directory);
        if (directory.getId() == null) {
            return createDirectory(directory);
        }
        Directory result = directoryService.save(directory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("directory", directory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /directories : get all the directories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of directories in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/directories")
    @Timed
    public ResponseEntity<List<Directory>> getAllDirectories(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Directories");
        Page<Directory> page = directoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/directories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /directories/:id : get the "id" directory.
     *
     * @param id the id of the directory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the directory, or with status 404 (Not Found)
     */
    @GetMapping("/directories/{id}")
    @Timed
    public ResponseEntity<Directory> getDirectory(@PathVariable Long id) {
        log.debug("REST request to get Directory : {}", id);
        Directory directory = directoryService.findOne(id);
        return Optional.ofNullable(directory)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /directories/:id : delete the "id" directory.
     *
     * @param id the id of the directory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/directories/{id}")
    @Timed
    public ResponseEntity<Void> deleteDirectory(@PathVariable Long id) {
        log.debug("REST request to delete Directory : {}", id);
        directoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("directory", id.toString())).build();
    }

    /**
     * SEARCH  /_search/directories?query=:query : search for the directory corresponding
     * to the query.
     *
     * @param query the query of the directory search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/directories")
    @Timed
    public ResponseEntity<List<Directory>> searchDirectories(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Directories for query {}", query);
        Page<Directory> page = directoryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/directories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
