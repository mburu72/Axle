package com.tumi.haul.model.converters;

import com.tumi.haul.model.enums.Roles;
import com.tumi.haul.model.primitives.Role;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class RoleConverter
        implements AttributeConverter<Role, String> {

    @Override
    public String convertToDatabaseColumn(Role role) {
        return role.getValue().toString();
    }

    @Override
    public Role convertToEntityAttribute(String role) {
        switch (role) {
            case "CLIENT":
                return new Role(Roles.CLIENT);
            case "HAULER":
                return new Role(Roles.HAULER);
            default:
                throw new IllegalArgumentException("invalid role");
        }
    }}
