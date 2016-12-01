package io.nbe.test.repository;

import io.nbe.test.domain.Directory;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Directory entity.
 */
@SuppressWarnings("unused")
public interface DirectoryRepository extends JpaRepository<Directory,Long> {

}
