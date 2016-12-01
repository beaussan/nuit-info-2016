package io.nbe.test.repository.search;

import io.nbe.test.domain.ContactRequest;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ContactRequest entity.
 */
public interface ContactRequestSearchRepository extends ElasticsearchRepository<ContactRequest, Long> {
}
