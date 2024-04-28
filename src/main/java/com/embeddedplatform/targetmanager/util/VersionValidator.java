package com.embeddedplatform.targetmanager.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class VersionValidator implements ConstraintValidator<com.embeddedplatform.targetmanager.util.validatior.VersionValidator, String> {
    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        return VersionUtil.isVersionTag(string) || string.equals("latest") || string.equals("*");
    }
}
