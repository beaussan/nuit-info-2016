package io.nbe.test.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A ContactRequest.
 */
@Entity
@Table(name = "contact_request")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "contactrequest")
public class ContactRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "is_accepted")
    private Boolean isAccepted;

    @Column(name = "date_accepted")
    private ZonedDateTime dateAccepted;

    @Column(name = "date_asked")
    private ZonedDateTime dateAsked;

    @Column(name = "message")
    private String message;

    @ManyToOne
    private ExtandedUser receiver;

    @ManyToOne
    private ExtandedUser sender;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isIsAccepted() {
        return isAccepted;
    }

    public ContactRequest isAccepted(Boolean isAccepted) {
        this.isAccepted = isAccepted;
        return this;
    }

    public void setIsAccepted(Boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

    public ZonedDateTime getDateAccepted() {
        return dateAccepted;
    }

    public ContactRequest dateAccepted(ZonedDateTime dateAccepted) {
        this.dateAccepted = dateAccepted;
        return this;
    }

    public void setDateAccepted(ZonedDateTime dateAccepted) {
        this.dateAccepted = dateAccepted;
    }

    public ZonedDateTime getDateAsked() {
        return dateAsked;
    }

    public ContactRequest dateAsked(ZonedDateTime dateAsked) {
        this.dateAsked = dateAsked;
        return this;
    }

    public void setDateAsked(ZonedDateTime dateAsked) {
        this.dateAsked = dateAsked;
    }

    public String getMessage() {
        return message;
    }

    public ContactRequest message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ExtandedUser getReceiver() {
        return receiver;
    }

    public ContactRequest receiver(ExtandedUser extandedUser) {
        this.receiver = extandedUser;
        return this;
    }

    public void setReceiver(ExtandedUser extandedUser) {
        this.receiver = extandedUser;
    }

    public ExtandedUser getSender() {
        return sender;
    }

    public ContactRequest sender(ExtandedUser extandedUser) {
        this.sender = extandedUser;
        return this;
    }

    public void setSender(ExtandedUser extandedUser) {
        this.sender = extandedUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContactRequest contactRequest = (ContactRequest) o;
        if (contactRequest.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, contactRequest.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ContactRequest{" +
            "id=" + id +
            ", isAccepted='" + isAccepted + "'" +
            ", dateAccepted='" + dateAccepted + "'" +
            ", dateAsked='" + dateAsked + "'" +
            ", message='" + message + "'" +
            '}';
    }
}
