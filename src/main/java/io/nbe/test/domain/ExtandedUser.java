package io.nbe.test.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
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

    @Column(name = "last_connection")
    private ZonedDateTime lastConnection;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "sender")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ContactRequest> friendRequests = new HashSet<>();

    @OneToMany(mappedBy = "receiver")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ContactRequest> friendRequestReceiveds = new HashSet<>();

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

    public ZonedDateTime getLastConnection() {
        return lastConnection;
    }

    public ExtandedUser lastConnection(ZonedDateTime lastConnection) {
        this.lastConnection = lastConnection;
        return this;
    }

    public void setLastConnection(ZonedDateTime lastConnection) {
        this.lastConnection = lastConnection;
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

    public Set<ContactRequest> getFriendRequests() {
        return friendRequests;
    }

    public ExtandedUser friendRequests(Set<ContactRequest> contactRequests) {
        this.friendRequests = contactRequests;
        return this;
    }

    public ExtandedUser addFriendRequests(ContactRequest contactRequest) {
        friendRequests.add(contactRequest);
        contactRequest.setSender(this);
        return this;
    }

    public ExtandedUser removeFriendRequests(ContactRequest contactRequest) {
        friendRequests.remove(contactRequest);
        contactRequest.setSender(null);
        return this;
    }

    public void setFriendRequests(Set<ContactRequest> contactRequests) {
        this.friendRequests = contactRequests;
    }

    public Set<ContactRequest> getFriendRequestReceiveds() {
        return friendRequestReceiveds;
    }

    public ExtandedUser friendRequestReceiveds(Set<ContactRequest> contactRequests) {
        this.friendRequestReceiveds = contactRequests;
        return this;
    }

    public ExtandedUser addFriendRequestReceived(ContactRequest contactRequest) {
        friendRequestReceiveds.add(contactRequest);
        contactRequest.setReceiver(this);
        return this;
    }

    public ExtandedUser removeFriendRequestReceived(ContactRequest contactRequest) {
        friendRequestReceiveds.remove(contactRequest);
        contactRequest.setReceiver(null);
        return this;
    }

    public void setFriendRequestReceiveds(Set<ContactRequest> contactRequests) {
        this.friendRequestReceiveds = contactRequests;
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
            ", lastConnection='" + lastConnection + "'" +
            '}';
    }
}
