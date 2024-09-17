package com.tumi.haul.model.converters;

import com.tumi.haul.model.primitives.JobDescription;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class JobDescriptionConverter implements AttributeConverter<JobDescription, String> {
    @Override
    public String convertToDatabaseColumn(JobDescription description) {
        return description.getValue();
    }

    @Override
    public JobDescription convertToEntityAttribute(String description) {
        return new JobDescription(description);
    }
}
