package com.tumi.haul.model.converters;

import com.tumi.haul.model.primitives.Price;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.math.BigDecimal;
@Converter
public class PriceConverter implements AttributeConverter<Price, String> {
    @Override
    public String convertToDatabaseColumn(Price price) {
        return price.getValue().toString();
    }

    @Override
    public Price convertToEntityAttribute(String price) {
        return new Price(new BigDecimal(price));
    }
}
