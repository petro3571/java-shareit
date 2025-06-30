package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Пользователь не является владельцем вещи")
public class NotFoundUserForItemException extends RuntimeException {
    public NotFoundUserForItemException(String message) {
        super(message);
    }
}