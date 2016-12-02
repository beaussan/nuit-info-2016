package io.nbe.test.repository.search;

import io.nbe.test.domain.Directory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Directory entity.
 */
public interface DirectorySearchRepository extends ElasticsearchRepository<Directory, Long> {
}
