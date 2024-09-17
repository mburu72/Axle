package com.tumi.haul.model.primitives;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Getter
public class Email implements Serializable {
    @Column(name = "email")
        private final String value;
        public Email(final String email) {
            if (email.length() < 8)
                throw new IllegalArgumentException("email must be at least 8 characters");
            if (email.length() > 60)
                throw new IllegalArgumentException("email must not exceed 60 characters");
            if (!(email.matches("^[A-Za-z0-9+_.-]+@(.+)$")))
                throw new IllegalArgumentException("invalid email format");
            this.value = email;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Email emailBase = (Email) o;
            return value.equals(emailBase.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public String toString() {
            return "EmailBase{" +
                    "value='" + value + '\'' +
                    '}';
        }

}
