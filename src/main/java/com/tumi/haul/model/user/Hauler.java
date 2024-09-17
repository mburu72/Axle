package com.tumi.haul.model.user;

import com.tumi.haul.model.converters.EmailConverter;
import com.tumi.haul.model.converters.NameConverter;
import com.tumi.haul.model.converters.PhoneNumberConverter;
import com.tumi.haul.model.converters.RoleConverter;
import com.tumi.haul.model.primitives.Email;
import com.tumi.haul.model.primitives.Name;
import com.tumi.haul.model.primitives.PhoneNumber;
import com.tumi.haul.model.primitives.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

import static org.apache.commons.lang3.Validate.notNull;
@Entity
@Table(name = "users")
@Data
@ToString
@NoArgsConstructor(force = true)
public class Hauler {

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
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner", cascade = CascadeType.ALL)
//    private List<Job> job;

    @CreationTimestamp
    @Column(updatable = false)
    protected LocalDateTime creationDate;

    private Hauler(final Name firstName,
                   final Name lastName,
                   final PhoneNumber phoneNumber,
                   final Email email,
                   final String password,
                   final Role role,
                   final LocalDateTime creationDate) {
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
        Hauler hauler = (Hauler) o;
        return id.equals(hauler.id);
    }

}
