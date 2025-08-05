package ru.practicum.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.exceptions.NotFoundUserException;
import ru.practicum.exceptions.NotFoundUserForItemException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ErrorHandler {
    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    @ExceptionHandler(NotFoundUserException.class)
    public ResponseEntity<Map<String, String>> handleNotFoundUser(NotFoundUserException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMap);
    }

    @ExceptionHandler(NotFoundUserForItemException.class)
    public ResponseEntity<Map<String, String>> handleNotFoundUserForItem(NotFoundUserForItemException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorMap);
    }
}