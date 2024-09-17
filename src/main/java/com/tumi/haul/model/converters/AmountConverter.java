package com.tumi.haul.model.converters;

import com.tumi.haul.model.primitives.Amount;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.math.BigDecimal;
@Converter
public class AmountConverter implements AttributeConverter<Amount, BigDecimal> {
    @Override
    public BigDecimal convertToDatabaseColumn(Amount amount) {
        return amount.getValue();
    }

    @Override
    public Amount convertToEntityAttribute(BigDecimal dbData) {
        return new Amount(dbData);
    }

}
