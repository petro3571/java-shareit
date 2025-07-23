package ru.practicum.user;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.user.dto.NewUserRequest;

@Component
@RequiredArgsConstructor
public class ItemValidator {
    public void validate(NewUserRequest userDto) {
        if (userDto.getName().isBlank()) {
            throw new ValidationException("Название не может состоять из пробелов");
        }
        // Дополнительные проверки
    }
}