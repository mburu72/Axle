package com.tumi.haul.model.converters;


import com.tumi.haul.model.enums.Roles;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class RoleConverter
        implements AttributeConverter<Roles, String> {

    @Override
    public String convertToDatabaseColumn(Roles role) {
        return role.getAuthority();
    }

    @Override
    public Roles convertToEntityAttribute(String role) {
        return switch (role) {
            case "CLIENT" -> Roles.CLIENT;
            case "HAULER" -> Roles.HAULER;
            case "ADMIN" -> Roles.ADMIN;
            default -> throw new IllegalArgumentException("invalid role");
        };
    }}
