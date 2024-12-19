package com.tumi.haul.model.admin.model;

import com.tumi.haul.model.converters.RoleConverter;
import com.tumi.haul.model.enums.Roles;
import com.tumi.haul.model.primitives.Email;
import com.tumi.haul.model.primitives.Name;
import com.tumi.haul.model.user.BaseUser;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;
@Entity
@Data
@Table(name = "users")
@Getter
public class Admin extends BaseUser implements Serializable {
    @Getter
    @Id
    private String id;
    @PrePersist
    private void generateId() {
        if (this.id == null) {
            this.id = "d_" + UUID.randomUUID().toString().substring(0, 8).toLowerCase();
        }
    }
    @Convert(converter = RoleConverter.class)
    private Roles role = Roles.ADMIN;
    @Convert(converter = Name.class)
    private Name firstName;
    @Convert(converter = Name.class)
    private Name lastName;
    @Convert(converter = Email.class)
    private Email email;
    private String password;
    private boolean accountLocked = false;
    private int failedLoginAttempts = 0;
}
