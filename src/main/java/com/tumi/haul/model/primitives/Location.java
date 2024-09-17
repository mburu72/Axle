package com.tumi.haul.model.primitives;

import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Getter
public class Location implements Serializable {
    private final String value;
    public Location(final String location){
        if (location.trim().length()<3)
            throw new IllegalArgumentException("Location must be at least 3 characters");
        if (location.trim().length()>40)
            throw new IllegalArgumentException("Name must not exceed 40 characters");

        this.value=location;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return value.equals(location.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Location{" +
                "value='" + value + '\'' +
                '}';
    }

}
