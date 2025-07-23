package ru.practicum.handler;

import lombok.Data;

@Data
public class ErrorResponse {
    private String error;

    public ErrorResponse(String error) {
        this.error = error;
    }
}