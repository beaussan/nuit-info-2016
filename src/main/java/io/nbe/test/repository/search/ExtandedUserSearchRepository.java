package io.nbe.test.repository.search;

import io.nbe.test.domain.ExtandedUser;
import io.nbe.test.domain.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

/**
 * Spring Data ElasticSearch repository for the ExtandedUser entity.
 */
public interface ExtandedUserSearchRepository extends ElasticsearchRepository<ExtandedUser, Long> {

}
