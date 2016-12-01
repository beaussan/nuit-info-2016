package io.nbe.test.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Message.
 */
@Entity
@Table(name = "message")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "message")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "is_read")
    private Boolean isRead;

    @Column(name = "date_writen")
    private ZonedDateTime dateWriten;

    @Column(name = "date_seen")
    private ZonedDateTime dateSeen;

    @Column(name = "data")
    private String data;

    @OneToOne
    @JoinColumn(unique = true)
    private ExtandedUser source;

    @ManyToOne
    private Conversation conversation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isIsRead() {
        return isRead;
    }

    public Message isRead(Boolean isRead) {
        this.isRead = isRead;
        return this;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public ZonedDateTime getDateWriten() {
        return dateWriten;
    }

    public Message dateWriten(ZonedDateTime dateWriten) {
        this.dateWriten = dateWriten;
        return this;
    }

    public void setDateWriten(ZonedDateTime dateWriten) {
        this.dateWriten = dateWriten;
    }

    public ZonedDateTime getDateSeen() {
        return dateSeen;
    }

    public Message dateSeen(ZonedDateTime dateSeen) {
        this.dateSeen = dateSeen;
        return this;
    }

    public void setDateSeen(ZonedDateTime dateSeen) {
        this.dateSeen = dateSeen;
    }

    public String getData() {
        return data;
    }

    public Message data(String data) {
        this.data = data;
        return this;
    }

    public void setData(String data) {
        this.data = data;
    }

    public ExtandedUser getSource() {
        return source;
    }

    public Message source(ExtandedUser extandedUser) {
        this.source = extandedUser;
        return this;
    }

    public void setSource(ExtandedUser extandedUser) {
        this.source = extandedUser;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public Message conversation(Conversation conversation) {
        this.conversation = conversation;
        return this;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Message message = (Message) o;
        if (message.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, message.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Message{" +
            "id=" + id +
            ", isRead='" + isRead + "'" +
            ", dateWriten='" + dateWriten + "'" +
            ", dateSeen='" + dateSeen + "'" +
            ", data='" + data + "'" +
            '}';
    }
}
