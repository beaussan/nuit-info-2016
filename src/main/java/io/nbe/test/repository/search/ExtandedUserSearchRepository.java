package io.nbe.test.repository.search;

import io.nbe.test.domain.ExtandedUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ExtandedUser entity.
 */
public interface ExtandedUserSearchRepository extends ElasticsearchRepository<ExtandedUser, Long> {
}
