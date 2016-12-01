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
 * A ExtandedUser.
 */
@Entity
@Table(name = "extanded_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "extandeduser")
public class ExtandedUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToMany(mappedBy = "members")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Conversation> messages = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public ExtandedUser user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Conversation> getMessages() {
        return messages;
    }

    public ExtandedUser messages(Set<Conversation> conversations) {
        this.messages = conversations;
        return this;
    }

    public ExtandedUser addMessages(Conversation conversation) {
        messages.add(conversation);
        conversation.getMembers().add(this);
        return this;
    }

    public ExtandedUser removeMessages(Conversation conversation) {
        messages.remove(conversation);
        conversation.getMembers().remove(this);
        return this;
    }

    public void setMessages(Set<Conversation> conversations) {
        this.messages = conversations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExtandedUser extandedUser = (ExtandedUser) o;
        if (extandedUser.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, extandedUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ExtandedUser{" +
            "id=" + id +
            '}';
    }
}
