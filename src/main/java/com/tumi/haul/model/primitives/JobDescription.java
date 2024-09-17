package com.tumi.haul.model.primitives;

import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;
@Getter
public class JobDescription implements Serializable {
    private final String value;
    public JobDescription(final String description){
        if (description.trim().length()<3)
            throw new IllegalArgumentException("Description must be at least 3 characters");
        if (description.trim().length()>200)
            throw new IllegalArgumentException("Description must not exceed 40 characters");
    this.value=description;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobDescription description = (JobDescription) o;
        return value.equals(description.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Description{" +
                "value='" + value + '\'' +
                '}';
    }

}
