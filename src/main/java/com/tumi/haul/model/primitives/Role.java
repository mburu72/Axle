package com.tumi.haul.model.primitives;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.tumi.haul.model.enums.Roles;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Objects;

import static org.apache.commons.lang3.Validate.notNull;

@Getter
public class Role implements Serializable {

    private final Roles value;
    @JsonCreator
    public Role(String authority){
        this.value=Roles.fromAuthority(authority);
    }
    public Role(final Roles role) {
        notNull(role);
        this.value = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return value == role.value;
    }
    @JsonValue
    public String getAuthority(){
        return value.getAuthority();
    }
  public Roles getRole(){
        return value;
  }
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Role{" +
                "value=" + value +
                '}';
    }

    public static Roles convertFromString(String role) {
        return switch (role) {
            case "CLIENT" -> Roles.CLIENT;
            case "HAULER" -> Roles.HAULER;
            default -> throw new IllegalArgumentException("invalid role " + role);
        };
    }
}

