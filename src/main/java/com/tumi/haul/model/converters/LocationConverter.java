package com.tumi.haul.model.converters;

import com.tumi.haul.model.primitives.Location;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class LocationConverter implements AttributeConverter<Location, String> {
    @Override
    public String convertToDatabaseColumn(Location location) {
        return location.getValue();
    }

    @Override
    public Location convertToEntityAttribute(String location) {
        return new Location(location);
    }
}
