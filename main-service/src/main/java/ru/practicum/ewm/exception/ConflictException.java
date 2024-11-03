package ru.practicum.ewm.exception;

import lombok.Getter;

@Getter
public class ConflictException extends RuntimeException {
//    private final String reason = "Integrity constraint has been violated.";

    public ConflictException(String message) {
        super(message);
    }
}
