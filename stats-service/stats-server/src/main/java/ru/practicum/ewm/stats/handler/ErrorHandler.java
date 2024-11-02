package ru.practicum.ewm.stats.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.stats.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse invalidMethodArgument(Exception e) {
        String reason = "Invalid Method Argument";
        String message = "";
        LocalDateTime time = LocalDateTime.now();
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
        } else if (e instanceof ValidationException ex) {
            message = ex.getMessage();
            errors.add(ex.getMessage());
        }
        return new ErrorResponse(errors, message, reason, HttpStatus.BAD_REQUEST.name(), time);
    }

}
