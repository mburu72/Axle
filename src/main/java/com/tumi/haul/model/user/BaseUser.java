package com.tumi.haul.model.user;

import com.tumi.haul.model.converters.*;
import com.tumi.haul.model.enums.Roles;
import com.tumi.haul.model.enums.UserStatus;
import com.tumi.haul.model.primitives.CreationDate;
import com.tumi.haul.model.primitives.Email;
import com.tumi.haul.model.primitives.Name;
import com.tumi.haul.model.primitives.PhoneNumber;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@NoArgsConstructor
@Setter
@Getter
@MappedSuperclass
public abstract class BaseUser {

    @Getter
    @Convert(converter = NameConverter.class)
    private Name firstName;

    @Getter
    @Convert(converter = NameConverter.class)
    private Name lastName;

    @Getter
    @Convert(converter = PhoneNumberConverter.class)
    private PhoneNumber phoneNumber;
    @CreationTimestamp
    @Column(updatable = false)
    @Convert(converter = CreationDateConverter.class)
    protected CreationDate creationDate;
    @Getter
    @Convert(converter = EmailConverter.class)
    private Email email;
    private String password;
    @Convert(converter = RoleConverter.class)
    private Roles role;
    private boolean verified = false;

    protected BaseUser(Name firstName, Name lastName, PhoneNumber phoneNumber, CreationDate creationDate, Email email, String password, boolean verified) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.creationDate = creationDate;
        this.email = email;
        this.password = password;
        this.verified = verified;

    }
}
