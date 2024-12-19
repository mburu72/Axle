package com.tumi.haul.model.user;

import com.tumi.haul.model.converters.IDNumberConverter;
import com.tumi.haul.model.converters.RoleConverter;
import com.tumi.haul.model.enums.Roles;
import com.tumi.haul.model.primitives.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "users")
@NoArgsConstructor(force = true)
@Getter
@Setter
public class User extends BaseUser implements Serializable {

    @Getter
    @Id
    private String id;
    @Convert(converter = RoleConverter.class)
    private Roles role = Roles.HAULER;
    @PrePersist
    private void generateId() {
        if (this.id == null) {
            this.id = "d_" + UUID.randomUUID().toString().substring(0, 8).toLowerCase();
        }
    }
    @Convert(converter = IDNumberConverter.class)
    private IDNumber idNumber;
    @Embedded
    private DriverDocuments documents;
    private User(final Name firstName,
                 final Name lastName,
                 final PhoneNumber phoneNumber,
                 final Email email,
                 final String password,
                 final boolean verified,
                 final IDNumber idNumber,
                 final Roles role,
                 final CreationDate creationDate) {
        super(firstName, lastName, phoneNumber,creationDate, email, password, verified);
        this.creationDate = creationDate;
        this.idNumber = idNumber;
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    public String toString() {
        return "User{" +
                "id='" + id + "', name=" + getFirstName() + " " + getLastName() +
                ", phoneNumber=" + getPhoneNumber() + ", email=" + getEmail() + ", creationDate=" + creationDate + "}";
    }
}
