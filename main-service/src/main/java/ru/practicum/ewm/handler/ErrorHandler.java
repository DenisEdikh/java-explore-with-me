package ru.practicum.ewm.handler;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, ValidationException.class, NumberFormatException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse invalidMethodArgument(Exception e) {
        String reason = "Incorrectly made request.";
        String message;
        List<String> errors = new ArrayList<>();

        if (e instanceof MethodArgumentNotValidException ex) {
            message = "Ошибка при валидации полей объекта";
            errors = ex.getFieldErrors().stream()
                    .map(field -> "Поле - %s / Ошибка - %s / Текущее значение - %s".formatted(
                                    field.getField(),
                                    field.getDefaultMessage(),
                                    field.getRejectedValue()
                            )
                    ).toList();
        } else {
            message = e.getMessage();
            errors.add(e.getMessage());
        }
        return new ErrorResponse(errors, message, reason, HttpStatus.BAD_REQUEST.name(), LocalDateTime.now());
    }

    @ExceptionHandler({DataIntegrityViolationException.class,
            ConflictException.class,
            ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    // TODO добавить обработку удаления внешнего ключа для category
    public ErrorResponse conflictException(RuntimeException e) {
        String reason = "Integrity constraint has been violated.";

        return new ErrorResponse(List.of(e.getMessage()),
                e.getMessage(),
                reason,
                HttpStatus.CONFLICT.name(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFoundException(NotFoundException e) {
        return new ErrorResponse(List.of(e.getMessage()),
                e.getMessage(),
                e.getReason(),
                HttpStatus.CONFLICT.name(),
                LocalDateTime.now());
    }

}
