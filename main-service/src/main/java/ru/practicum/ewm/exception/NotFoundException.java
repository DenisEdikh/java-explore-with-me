package ru.practicum.ewm.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private final String reason = "Not required object was not found.";

    public NotFoundException(Class<?> clazz, long id) {
        super("%s with id = %d not found".formatted(clazz.getSimpleName(), id));
    }
}
