package com.tumi.haul.config;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class MultipartFilesSizeValidator implements ConstraintValidator<ValidImage, List<MultipartFile>> {

    private static final long MAX_SIZE = 2 * 1024 * 1024; // 2MB

    @Override
    public boolean isValid(List<MultipartFile> files, ConstraintValidatorContext context) {
        if (files == null || files.isEmpty()) {
            return true; // Or handle as per your requirements, e.g. allow empty list
        }

        for (MultipartFile file : files) {
            if (file != null && file.getSize() > MAX_SIZE) {
                return false; // Invalid file size, return false
            }
        }
        return true; // All files are valid
    }
}
