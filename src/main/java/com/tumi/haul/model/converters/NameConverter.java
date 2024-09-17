package com.tumi.haul.model.converters;

import com.tumi.haul.model.primitives.Name;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class NameConverter implements AttributeConverter<Name,String> {
    @Override
    public String convertToDatabaseColumn(Name name) {
        return name.getValue();
    }

    @Override
    public Name convertToEntityAttribute(String name) {
        return new Name(name);
    }
}
