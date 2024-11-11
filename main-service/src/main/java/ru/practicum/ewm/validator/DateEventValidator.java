package ru.practicum.ewm.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class DateEventValidator implements ConstraintValidator<DateEventConstraint, LocalDateTime> {
    private long hours;

    @Override
    public void initialize(DateEventConstraint constraintAnnotation) {
        hours = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(LocalDateTime localDateTime, ConstraintValidatorContext constraintValidatorContext) {
        if (localDateTime == null) {
            return true;
        }
        return localDateTime.isAfter(LocalDateTime.now().plusHours(hours));
    }
}
