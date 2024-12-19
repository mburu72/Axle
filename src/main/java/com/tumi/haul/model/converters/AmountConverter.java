package com.tumi.haul.model.converters;

import com.tumi.haul.model.primitives.Amount;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.math.BigDecimal;
@Converter
public class AmountConverter implements AttributeConverter<Amount, String> {
    @Override
    public String convertToDatabaseColumn(Amount amount) {
        return amount != null ? amount.toString() : null ;
    }

    @Override
    public Amount convertToEntityAttribute(String dbData) {
        return dbData != null ? new Amount(new BigDecimal(dbData)) : null;
    }

}
