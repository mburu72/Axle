package com.tumi.haul.model.primitives;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Price {
    private final BigDecimal value;
    public Price(BigDecimal value){
        if(value == null || value.compareTo(BigDecimal.ZERO)<=0){
            throw new IllegalArgumentException("price must be a positive value");
        }
        this.value=value;
    }

    @Override
    public String toString(){
        return value.toString();
    }
}
