package io.nbe.test.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Conversation.
 */
@Entity
@Table(name = "conversation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "conversation")
public class Conversation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "conversation")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Message> messages = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "conversation_members",
               joinColumns = @JoinColumn(name="conversations_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="members_id", referencedColumnName="ID"))
    private Set<ExtandedUser> members = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public Conversation messages(Set<Message> messages) {
        this.messages = messages;
        return this;
    }

    public Conversation addMessages(Message message) {
        messages.add(message);
        message.setConversation(this);
        return this;
    }

    public Conversation removeMessages(Message message) {
        messages.remove(message);
        message.setConversation(null);
        return this;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public Set<ExtandedUser> getMembers() {
        return members;
    }

    public Conversation members(Set<ExtandedUser> extandedUsers) {
        this.members = extandedUsers;
        return this;
    }

    public Conversation addMembers(ExtandedUser extandedUser) {
        members.add(extandedUser);
        extandedUser.getMessages().add(this);
        return this;
    }

    public Conversation removeMembers(ExtandedUser extandedUser) {
        members.remove(extandedUser);
        extandedUser.getMessages().remove(this);
        return this;
    }

    public void setMembers(Set<ExtandedUser> extandedUsers) {
        this.members = extandedUsers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Conversation conversation = (Conversation) o;
        if (conversation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, conversation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Conversation{" +
            "id=" + id +
            '}';
    }
}
