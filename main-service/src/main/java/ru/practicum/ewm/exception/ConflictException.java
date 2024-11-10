package ru.practicum.ewm.exception;

import lombok.Getter;

@Getter
public class ConflictException extends RuntimeException {
    public ConflictException(Class<?> clazz, Long id) {
        super("%s with id = %d is conflicted".formatted(clazz.getSimpleName(), id));
    }

    public ConflictException(Class<?> clazz, Long requesterId, Long eventId) {
        super("%s with requesterId = %d and eventId = %d is conflicted".formatted(
                clazz.getSimpleName(),
                requesterId,
                eventId)
        );
    }
}
