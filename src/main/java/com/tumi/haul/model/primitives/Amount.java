package com.tumi.haul.model.primitives;

import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
@Getter
public class Amount implements Serializable {
    private final BigDecimal value;
    public Amount (final BigDecimal value){
        this.value = value;
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("price must be a positive value");
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
