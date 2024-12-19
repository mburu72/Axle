package com.tumi.haul.model.primitives;

import lombok.Getter;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Objects;

import static org.apache.commons.lang3.BooleanUtils.isNotTrue;
import static org.apache.commons.lang3.Validate.notNull;

@Getter
public class UserId implements Serializable {

    private final String value;

    public UserId(final String id)
            throws IllegalArgumentException {
        notNull(id);
        if (isNotTrue(id.length() == 10))
            throw new IllegalArgumentException("id must be 10 characters");
        if (isNotTrue(id.matches("^[a-z0-9]+$")))
            throw new IllegalArgumentException("id must be made up of lowercase characters and numbers");
        this.value = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId = (UserId) o;
        return value.equals(userId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public static String generateUniqueID() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32).substring(0, 10);
    }
}

