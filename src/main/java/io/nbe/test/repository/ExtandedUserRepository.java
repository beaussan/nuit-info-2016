package io.nbe.test.repository;

import io.nbe.test.domain.ExtandedUser;

import io.nbe.test.domain.User;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the ExtandedUser entity.
 */
@SuppressWarnings("unused")
public interface ExtandedUserRepository extends JpaRepository<ExtandedUser,Long> {

    Optional<ExtandedUser> findOneByUser(User user);


}
