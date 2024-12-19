package com.tumi.haul.model.primitives;

import jakarta.persistence.Column;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;
@Getter
public class IDNumber implements Serializable {
    @Column(name = "idNumber")
    private final String value;

    public IDNumber(String idNumber) {
        if ((idNumber.length() != 8))
            throw new IllegalArgumentException("phone number must be 8 digits");
        if ((!idNumber.matches("[0-9]{7}")))
            throw new IllegalArgumentException(" be made up of 8 digits");
        this.value = idNumber;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IDNumber that = (IDNumber) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "ID Number{" +
                "value='" + value + '\'' +
                '}';
    }
}
