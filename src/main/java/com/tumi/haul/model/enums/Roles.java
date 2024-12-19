package com.tumi.haul.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.security.core.GrantedAuthority;

public enum Roles implements GrantedAuthority {

    CLIENT(Const.CLIENT),
    HAULER(Const.HAULER),
    ADMIN(Const.ADMIN);

    private final String authority;

    Roles(String authority) {
        this.authority = authority;
    }

    @JsonCreator
    public static Roles fromAuthority(String authority) {
        for (Roles b : Roles.values()) {
            if (b.authority.equals(authority)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + authority + "'");
    }

    @Override
    public String toString() {
        return String.valueOf(authority);
    }

    @Override
    @JsonValue
    public String getAuthority() {
        return authority;
    }

    public class Const {
        public static final String CLIENT = "CLIENT";
        public static final String HAULER = "HAULER";
        public static final String ADMIN = "ADMIN";
    }
}

