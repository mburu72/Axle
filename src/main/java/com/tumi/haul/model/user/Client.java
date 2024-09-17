package com.tumi.haul.model.user;

import com.tumi.haul.model.converters.*;
import com.tumi.haul.model.enums.Roles;
import com.tumi.haul.model.job.Job;
import com.tumi.haul.model.primitives.*;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import static org.apache.commons.lang3.Validate.notNull;


@Entity
@Table(name = "users")
@Data
@NoArgsConstructor(force = true)
public class Client implements Serializable{

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter
    @Convert(converter=NameConverter.class)
    private Name firstName;
    @Getter
    @Convert(converter=NameConverter.class)
    private Name lastName;
    @Getter
    @Convert(converter = PhoneNumberConverter.class)
    private PhoneNumber phoneNumber;
    @Getter
    @Convert(converter = EmailConverter.class)
    private Email email;
    private String password;
    @Convert(converter = RoleConverter.class)
    private Role role;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Job> job;

    @CreationTimestamp
    @Column(updatable = false)
    @Convert(converter = CreationDateConverter.class)
    protected CreationDate creationDate;

    private Client(final Name firstName,
                   final Name lastName,
                   final PhoneNumber phoneNumber,
                   final Email email,
                   final String password,
                   final Role role,
                   final CreationDate creationDate) {
        this.firstName = notNull(firstName);
        this.lastName=notNull(lastName);
        this.phoneNumber = notNull(phoneNumber);
        this.email = email;
        this.password=notNull(password);
        this.role=role;
        this.creationDate = creationDate;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id.equals(client.id);
    }

    public String toString(){
        return "User{"+
                "id='" + id +"name=" + firstName +lastName +"phoneNumber=" + phoneNumber +"email"+email+"role=" + role +
                "creationDate=" + creationDate+"}";
    }

}