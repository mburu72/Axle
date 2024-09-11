package com.tumi.haul.model.converters;

import com.tumi.haul.model.primitives.PhoneNumber;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class PhoneNumberConverter implements AttributeConverter<PhoneNumber, String> {
    @Override
    public String convertToDatabaseColumn(PhoneNumber phoneNumber) {
        return phoneNumber.getValue();
    }

    @Override
    public PhoneNumber convertToEntityAttribute(String phoneNumber) {
        return new PhoneNumber(phoneNumber);
    }
}
