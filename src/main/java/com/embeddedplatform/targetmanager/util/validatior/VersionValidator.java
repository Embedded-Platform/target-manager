package com.embeddedplatform.targetmanager.util.validatior;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( { ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = com.embeddedplatform.targetmanager.util.VersionValidator.class)
public @interface VersionValidator {
    public String message() default "Invalid version tag format";
    public Class<?>[] groups() default {};
    public Class<? extends Payload>[] payload() default {};
}
