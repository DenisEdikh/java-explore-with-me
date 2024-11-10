package ru.practicum.ewm.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateEventValidator.class)
public @interface DateEventConstraint {
    long value();

    String message() default "The time difference is less than 2 hours";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
