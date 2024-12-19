package com.tumi.haul.model.converters;

import com.tumi.haul.model.primitives.UserId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class IdConverter implements AttributeConverter<UserId, String> {
    @Override
    public String convertToDatabaseColumn(UserId id) {
        return id.getValue();
    }

    @Override
    public UserId convertToEntityAttribute(String id) {
        if (id == null) return null;
        return new UserId(id);
    }
}
