package com.embeddedplatform.targetmanager.util;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

public class FileValidator implements ConstraintValidator<com.embeddedplatform.targetmanager.util.validatior.FileValidator, MultipartFile> {
    private String[] allowedExtensions;

    @Override
    public void initialize(com.embeddedplatform.targetmanager.util.validatior.FileValidator constraintAnnotation) {
        this.allowedExtensions = constraintAnnotation.extensions();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        if(file == null || file.isEmpty() || file.getOriginalFilename() == null)
            return false;

        return  Arrays.stream(allowedExtensions)
                .anyMatch(extension -> file.getOriginalFilename().endsWith(extension));
    }
}
