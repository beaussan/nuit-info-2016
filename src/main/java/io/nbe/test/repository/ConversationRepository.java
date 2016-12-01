package io.nbe.test.repository;

import io.nbe.test.domain.Conversation;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Conversation entity.
 */
@SuppressWarnings("unused")
public interface ConversationRepository extends JpaRepository<Conversation,Long> {

    @Query("select distinct conversation from Conversation conversation left join fetch conversation.members")
    List<Conversation> findAllWithEagerRelationships();

    @Query("select conversation from Conversation conversation left join fetch conversation.members where conversation.id =:id")
    Conversation findOneWithEagerRelationships(@Param("id") Long id);

}
