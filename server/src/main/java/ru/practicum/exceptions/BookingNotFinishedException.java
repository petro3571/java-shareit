package ru.practicum.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Некорректное время")
public class BookingNotFinishedException extends RuntimeException {
    public BookingNotFinishedException(String message) {
        super(message);
    }
}
