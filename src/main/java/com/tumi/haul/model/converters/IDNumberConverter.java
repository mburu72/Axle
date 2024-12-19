package com.tumi.haul.model.converters;

import com.tumi.haul.model.primitives.IDNumber;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class IDNumberConverter implements AttributeConverter<IDNumber, String> {
    @Override
    public String convertToDatabaseColumn(IDNumber idNumber) {
        return idNumber.getValue();
    }

    @Override
    public IDNumber convertToEntityAttribute(String s) {
        return new IDNumber(s);
    }
}
