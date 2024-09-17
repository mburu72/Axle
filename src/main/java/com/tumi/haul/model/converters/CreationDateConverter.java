package com.tumi.haul.model.converters;

import com.tumi.haul.model.primitives.CreationDate;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.LocalDateTime;

@Converter
public class CreationDateConverter implements AttributeConverter<CreationDate, LocalDateTime> {

    @Override
    public LocalDateTime convertToDatabaseColumn(CreationDate creationDate) {
        return creationDate.getValue();
    }

    @Override
    public CreationDate convertToEntityAttribute(LocalDateTime date) {
        return new CreationDate(date);
    }
}
