package com.tumi.haul.model.primitives;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class CreationDate implements Serializable {
    private final LocalDateTime value;
    public CreationDate(final LocalDateTime creationDate){
        if(creationDate.isAfter(LocalDateTime.now()))
            throw new IllegalArgumentException("creation date cannot be in the future");
        this.value=creationDate;


    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreationDate that = (CreationDate) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "CreationDate{" +
                "value=" + value +
                '}';
    }

    public LocalDateTime getValue() {
        return value;
    }
}
