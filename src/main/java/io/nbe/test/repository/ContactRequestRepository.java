package io.nbe.test.repository;

import io.nbe.test.domain.ContactRequest;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ContactRequest entity.
 */
@SuppressWarnings("unused")
public interface ContactRequestRepository extends JpaRepository<ContactRequest,Long> {

}
