package com.tumi.haul.model.primitives;

import java.io.Serializable;
import java.util.Objects;


public class Name implements Serializable {
    private final String value;
    public Name (final String name){

        if (name.trim().length()<3)
            throw new IllegalArgumentException("Name must be at least 3 characters");
        if (name.trim().length()>40)
            throw new IllegalArgumentException("Name must not exceed 40 characters");
        if(!name.matches("[A-Z][-a-zA-Z&():.:/\\\\, ]{2,39}"))
            throw new IllegalArgumentException("name must start with capital letter" +
                    " and contain only characters and -&():/., and spaces");
        this.value=name;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name name = (Name) o;
        return value.equals(name.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Name{" +
                "value='" + value + '\'' +
                '}';
    }

    public String getValue() {
        return value;
    }
}
